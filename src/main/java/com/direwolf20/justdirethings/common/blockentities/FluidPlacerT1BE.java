package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.FluidContainerData;
import com.direwolf20.justdirethings.common.blockentities.basebe.FluidMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.common.capabilities.JustDireFluidTank;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

public class FluidPlacerT1BE extends BaseMachineBE implements RedstoneControlledBE, FluidMachineBE {
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    public final FluidContainerData fluidContainerData;
    List<BlockPos> positionsToPlace = new ArrayList<>();

    public FluidPlacerT1BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 1; //Slot for a bucket
        fluidContainerData = new FluidContainerData(this);
    }

    public FluidPlacerT1BE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.FluidPlacerT1BE.get(), pPos, pBlockState);
    }

    public int getMaxMB() {
        return 8000;
    }

    @Override
    public ContainerData getFluidContainerData() {
        return fluidContainerData;
    }

    @Override
    public RedstoneControlData getRedstoneControlData() {
        return redstoneControlData;
    }

    @Override
    public BlockEntity getBlockEntity() {
        return this;
    }

    @Override
    public void tickClient() {
    }

    @Override
    public void tickServer() {
        super.tickServer();
        handleItemStack();
        doFluidPlace();
    }

    public void handleItemStack() {
        if (isFull()) return;
        ItemStack itemStack = getItemStack();
        if (!isStackValid(itemStack)) return;
        ItemAccess itemAccess = ItemAccess.forHandlerIndex(getMachineHandler(), 0);
        ResourceHandler<FluidResource> fluidHandler = itemAccess.getCapability(Capabilities.Fluid.ITEM);
        if (fluidHandler == null) return;

        // Find any non-empty fluid slot in the item and transfer into the tank
        try (Transaction tx = Transaction.openRoot()) {
            for (int i = 0; i < fluidHandler.size(); i++) {
                FluidResource resource = fluidHandler.getResource(i);
                if (resource.isEmpty()) continue;
                int avail = fluidHandler.getAmountAsInt(i);
                if (avail <= 0) continue;
                int capacityFit;
                try (Transaction simTx = Transaction.open(tx)) {
                    capacityFit = getFluidTank().insert(0, resource, Math.min(avail, 1000), simTx);
                }
                if (capacityFit <= 0) continue;
                int extracted = fluidHandler.extract(i, resource, capacityFit, tx);
                if (extracted > 0) {
                    getFluidTank().insert(0, resource, extracted, tx);
                    tx.commit();
                }
                return;
            }
        }
    }

    public ItemStack getItemStack() {
        return getMachineHandler().getResource(0).toStack(getMachineHandler().getAmountAsInt(0));
    }

    public boolean isStackValid(ItemStack itemStack) {
        if (itemStack.isEmpty())
            return false;
        ResourceHandler<FluidResource> fluidHandler = ItemAccess.forStack(itemStack).getCapability(Capabilities.Fluid.ITEM);
        if (fluidHandler == null)
            return false;
        // Probe for any non-empty fluid in the item
        for (int i = 0; i < fluidHandler.size(); i++) {
            FluidResource resource = fluidHandler.getResource(i);
            if (resource.isEmpty()) continue;
            if (!getFluidStack().isEmpty() && !getFluidStack().is(resource.getFluid()))
                continue;
            return true;
        }
        return false;
    }

    public FluidStack getPlaceStack() {
        return getFluidStack();
    }

    public JustDireFluidTank getFluidTank() {
        return getData(Registration.MACHINE_FLUID_HANDLER);
    }

    public boolean isStackValid(FluidStack fluidStack) {
        if (fluidStack.isEmpty())
            return false;
        if (fluidStack.getAmount() < 1000)
            return false;
        return true;
    }

    public boolean canPlace() {
        return true;
    }

    public boolean clearTrackerIfNeeded(FluidStack fluidStack) {
        if (positionsToPlace.isEmpty())
            return false;
        if (!isStackValid(fluidStack))
            return true;
        if (!canPlace())
            return true;
        if (!isActiveRedstone() && !redstoneControlData.redstoneMode.equals(MiscHelpers.RedstoneMode.PULSE))
            return true;
        return false;
    }

    public void doFluidPlace() {
        FluidStack placeStack = getPlaceStack();
        if (!isStackValid(placeStack)) {
            getRedstoneControlData().pulsed = false;
            return;
        }
        if (clearTrackerIfNeeded(placeStack)) {
            positionsToPlace.clear();
            return;
        }
        if (!canPlace()) return;
        FakePlayer fakePlayer = getFakePlayer((ServerLevel) level);
        if (isActiveRedstone() && canRun() && positionsToPlace.isEmpty())
            positionsToPlace = findSpotsToPlace(fakePlayer);
        if (positionsToPlace.isEmpty())
            return;
        if (canRun()) {
            BlockPos blockPos = positionsToPlace.removeFirst();
            placeFluid(placeStack, blockPos);
        }
    }

    public boolean placeFluid(FluidStack fluidStack, BlockPos blockPos) {
        if (fluidStack.isEmpty() || fluidStack.getAmount() < 1000) return false;
        Fluid fluid = fluidStack.getFluid();
        BlockState placementState = fluid.defaultFluidState().createLegacyBlock();
        if (placementState.isAir()) return false;
        if (!level.getBlockState(blockPos).canBeReplaced()) return false;

        // Drain 1000 mB from the tank on successful placement
        FluidResource resource = FluidResource.of(fluid);
        int drained;
        try (Transaction tx = Transaction.openRoot()) {
            drained = getFluidTank().extract(0, resource, 1000, tx);
            if (drained < 1000) return false;
            if (!level.setBlock(blockPos, placementState, 3)) return false;
            tx.commit();
        }
        return true;
    }

    public boolean isBlockPosValid(BlockPos blockPos, FakePlayer fakePlayer) {
        if (!level.getBlockState(blockPos).canBeReplaced())
            return false;
        if (level.getBlockState(blockPos).getBlock() instanceof LiquidBlock liquidBlock && level.getFluidState(blockPos).isSource())
            return false;
        if (!canBreakAndPlaceAt(level, blockPos, fakePlayer))
            return false;
        return true;
    }

    public List<BlockPos> findSpotsToPlace(FakePlayer fakePlayer) {
        List<BlockPos> returnList = new ArrayList<>();
        BlockPos blockPos = getBlockPos().relative(getBlockState().getValue(BlockStateProperties.FACING));
        if (isBlockPosValid(blockPos, fakePlayer))
            returnList.add(blockPos);
        return returnList;
    }
}
