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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;

public class FluidPlacerT1BE extends BaseMachineBE implements RedstoneControlledBE, FluidMachineBE {
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    public final FluidContainerData fluidContainerData;
    List<BlockPos> positionsToPlace = new ArrayList<>();

    public FluidPlacerT1BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
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
        doFluidPlace();
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
        if (!isStackValid(placeStack)) return;
        if (clearTrackerIfNeeded(placeStack)) {
            positionsToPlace.clear();
            return;
        }
        if (!canPlace()) return;
        if (isActiveRedstone() && canRun() && positionsToPlace.isEmpty())
            positionsToPlace = findSpotsToPlace();
        if (positionsToPlace.isEmpty())
            return;
        if (canRun()) {
            BlockPos blockPos = positionsToPlace.removeFirst();
            placeFluid(placeStack, blockPos);
        }
    }

    public boolean placeFluid(FluidStack fluidStack, BlockPos blockPos) {
        Fluid fluid = fluidStack.getFluid();
        BlockState blockState = fluid.defaultFluidState().createLegacyBlock();
        if (level.setBlock(blockPos, blockState, 3)) {
            getFluidTank().drain(1000, IFluidHandler.FluidAction.EXECUTE);
            level.playSound(null, blockPos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1F, 1.0F);
            return true;
        }
        return false;
    }

    public boolean isBlockPosValid(BlockPos blockPos) {
        if (!level.getBlockState(blockPos).canBeReplaced())
            return false;
        return true;
    }

    public List<BlockPos> findSpotsToPlace() {
        List<BlockPos> returnList = new ArrayList<>();
        BlockPos blockPos = getBlockPos().relative(getBlockState().getValue(BlockStateProperties.FACING));
        if (isBlockPosValid(blockPos))
            returnList.add(blockPos);
        return returnList;
    }
}
