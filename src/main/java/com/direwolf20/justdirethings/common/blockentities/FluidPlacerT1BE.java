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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

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
        IFluidHandlerItem fluidHandlerItem = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        FluidStack testExtract = fluidHandlerItem.drain(1000, IFluidHandler.FluidAction.SIMULATE);
        int insertAmt = getFluidTank().fill(testExtract, IFluidHandler.FluidAction.SIMULATE);
        if (insertAmt > 0) {
            FluidStack extractedStack = fluidHandlerItem.drain(insertAmt, IFluidHandler.FluidAction.EXECUTE);
            getFluidTank().fill(extractedStack, IFluidHandler.FluidAction.EXECUTE);
            if (itemStack.getItem() instanceof BucketItem)
                getMachineHandler().setStackInSlot(0, fluidHandlerItem.getContainer());
        }
    }

    public ItemStack getItemStack() {
        return getMachineHandler().getStackInSlot(0);
    }

    public boolean isStackValid(ItemStack itemStack) {
        if (itemStack.isEmpty())
            return false;
        IFluidHandlerItem fluidHandlerItem = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandlerItem == null)
            return false;
        FluidStack fluidStack = fluidHandlerItem.drain(1000, IFluidHandler.FluidAction.SIMULATE);
        if (fluidStack.getAmount() == 0)
            return false;
        if (!getFluidStack().isEmpty() && !getFluidStack().is(fluidStack.getFluid()))
            return false;
        return true;
    }

    public FluidStack getPlaceStack() {
        return getFluidTank().getFluid();
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
        return FluidUtil.tryPlaceFluid(null, level, InteractionHand.MAIN_HAND, blockPos, getFluidTank(), fluidStack);
    }

    public boolean isBlockPosValid(BlockPos blockPos, FakePlayer fakePlayer) {
        if (!level.getBlockState(blockPos).canBeReplaced())
            return false;
        if (level.getBlockState(blockPos).getBlock() instanceof LiquidBlock)
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
