package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.FluidContainerData;
import com.direwolf20.justdirethings.common.blockentities.basebe.FluidMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.common.capabilities.JustDireFluidTank;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.block.LiquidBlock.LEVEL;

public class FluidCollectorT1BE extends BaseMachineBE implements RedstoneControlledBE, FluidMachineBE {
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    public final FluidContainerData fluidContainerData;
    List<BlockPos> positionsToPlace = new ArrayList<>();

    public FluidCollectorT1BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 1; //Slot for a bucket
        fluidContainerData = new FluidContainerData(this);
    }

    public FluidCollectorT1BE(BlockPos pPos, BlockState pBlockState) {
        this(JDTRegistration.FluidCollectorT1BE.get(), pPos, pBlockState);
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
        doFluidCollect();
    }

    public void handleItemStack() {
        FluidStack fluidStack = getFluidStack();
        if (fluidStack.isEmpty()) return;
        ItemStack itemStack = getItemStack();
        if (!isStackValid(itemStack, fluidStack)) return;
        ItemAccess itemAccess = ItemAccess.forHandlerIndex(getMachineHandler(), 0);
        ResourceHandler<FluidResource> fluidHandler = itemAccess.getCapability(Capabilities.Fluid.ITEM);
        if (fluidHandler == null) return;

        FluidResource tankResource = getFluidTank().getResource(0);
        if (tankResource.isEmpty()) return;

        // Find a slot on the item-side handler that will accept our fluid
        for (int i = 0; i < fluidHandler.size(); i++) {
            int capacityFit;
            try (Transaction simTx = Transaction.openRoot()) {
                capacityFit = fluidHandler.insert(i, tankResource, Math.min(fluidStack.getAmount(), 1000), simTx);
            }
            if (capacityFit <= 0) continue;
            try (Transaction tx = Transaction.openRoot()) {
                int extracted = getFluidTank().extract(0, tankResource, capacityFit, tx);
                if (extracted <= 0) return;
                int inserted = fluidHandler.insert(i, tankResource, extracted, tx);
                if (inserted > 0) tx.commit();
            }
            return;
        }
    }

    public ItemStack getItemStack() {
        return getMachineHandler().getResource(0).toStack(getMachineHandler().getAmountAsInt(0));
    }

    public boolean isStackValid(ItemStack itemStack, FluidStack fluidStack) {
        if (itemStack.isEmpty())
            return false;
        if (fluidStack.isEmpty())
            return false;
        ResourceHandler<FluidResource> fluidHandler = ItemAccess.forStack(itemStack).getCapability(Capabilities.Fluid.ITEM);
        if (fluidHandler == null)
            return false;
        FluidResource resource = FluidResource.of(fluidStack);
        try (Transaction tx = Transaction.openRoot()) {
            for (int i = 0; i < fluidHandler.size(); i++) {
                if (fluidHandler.insert(i, resource, fluidStack.getAmount(), tx) > 0)
                    return true;
            }
        }
        return false;
    }

    public FluidStack getTankStack() {
        return getFluidStack();
    }

    public JustDireFluidTank getFluidTank() {
        return getData(JDTRegistration.MACHINE_FLUID_HANDLER);
    }

    public boolean isStackValid(FluidStack fluidStack) {
        if (fluidStack.isEmpty())
            return false;
        if (fluidStack.getAmount() < 1000)
            return false;
        return true;
    }

    public boolean canCollect() {
        return true;
    }

    public boolean clearTrackerIfNeeded() {
        if (positionsToPlace.isEmpty())
            return false;
        if (!canCollect())
            return true;
        if (!isActiveRedstone() && !redstoneControlData.redstoneMode.equals(MiscHelpers.RedstoneMode.PULSE))
            return true;
        return false;
    }

    public void doFluidCollect() {
        if (clearTrackerIfNeeded()) {
            positionsToPlace.clear();
            return;
        }
        if (!canCollect()) return;
        FakePlayer fakePlayer = getFakePlayer((ServerLevel) level);
        if (isActiveRedstone() && canRun() && positionsToPlace.isEmpty())
            positionsToPlace = findSpotsToCollect(fakePlayer);
        if (positionsToPlace.isEmpty())
            return;
        if (canRun()) {
            BlockPos blockPos = positionsToPlace.removeFirst();
            collectFluid(blockPos);
        }
    }

    public LiquidBlock getLiquidBlockAt(BlockPos blockPos) {
        if (level.getBlockState(blockPos).getBlock() instanceof LiquidBlock liquidBlock)
            return liquidBlock;
        return null;
    }

    public boolean collectFluid(BlockPos blockPos) {
        LiquidBlock liquidBlock = getLiquidBlockAt(blockPos);
        if (liquidBlock == null) return false;
        if (!isBlockValidForTank(liquidBlock))
            return false;
        FluidResource resource = FluidResource.of(liquidBlock.fluid);
        int canInsert;
        try (Transaction simTx = Transaction.openRoot()) {
            canInsert = getFluidTank().insert(0, resource, 1000, simTx);
        }
        if (canInsert < 1000) return false;
        if (level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3)) {
            try (Transaction tx = Transaction.openRoot()) {
                getFluidTank().insert(0, resource, 1000, tx);
                tx.commit();
            }
            level.playSound(null, blockPos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1F, 1.0F);
            return true;
        }
        return false;
    }

    public boolean isBlockValidForTank(LiquidBlock liquidBlock) {
        if (!getFluidStack().isEmpty() && !getFluidStack().is(liquidBlock.fluid))
            return false;
        return true;
    }

    public boolean isBlockPosValid(BlockPos blockPos, FakePlayer fakePlayer) {
        BlockState blockState = level.getBlockState(blockPos);
        if (!(blockState.getBlock() instanceof LiquidBlock liquidBlock))
            return false;
        if (blockState.getValue(LEVEL) != 0)
            return false;
        if (!isBlockValidForTank(liquidBlock))
            return false;
        if (!canPlaceAt(level, blockPos, fakePlayer))
            return false;
        return true;
    }

    public List<BlockPos> findSpotsToCollect(FakePlayer fakePlayer) {
        List<BlockPos> returnList = new ArrayList<>();
        BlockPos blockPos = getBlockPos().relative(getBlockState().getValue(BlockStateProperties.FACING));
        if (isBlockPosValid(blockPos, fakePlayer))
            returnList.add(blockPos);
        return returnList;
    }

    public boolean isFull() {
        return getFluidStack().getAmount() >= getMaxMB();
    }
}
