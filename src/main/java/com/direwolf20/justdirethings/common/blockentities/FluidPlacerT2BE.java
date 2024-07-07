package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.AreaAffectingBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.FilterableBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineContainerData;
import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FluidPlacerT2BE extends FluidPlacerT1BE implements PoweredMachineBE, AreaAffectingBE, FilterableBE {
    public FilterData filterData = new FilterData();
    public AreaAffectingData areaAffectingData = new AreaAffectingData();
    public final PoweredMachineContainerData poweredMachineData;

    public FluidPlacerT2BE(BlockPos pPos, BlockState pBlockState) {
        super(Registration.FluidPlacerT2BE.get(), pPos, pBlockState);
        poweredMachineData = new PoweredMachineContainerData(this);
    }

    @Override
    public int getMaxMB() {
        return 32000;
    }

    @Override
    public PoweredMachineContainerData getContainerData() {
        return poweredMachineData;
    }

    @Override
    public MachineEnergyStorage getEnergyStorage() {
        return getData(Registration.ENERGYSTORAGE_MACHINES);
    }

    @Override
    public int getStandardEnergyCost() {
        return 500;
    }

    @Override
    public AreaAffectingData getAreaAffectingData() {
        return areaAffectingData;
    }

    @Override
    public FilterBasicHandler getFilterHandler() {
        return getData(Registration.HANDLER_BASIC_FILTER);
    }

    @Override
    public FilterData getFilterData() {
        return filterData;
    }

    @Override
    public boolean canPlace() {
        return hasEnoughPower(getStandardEnergyCost());
    }

    @Override
    public boolean placeFluid(FluidStack fluidStack, BlockPos blockPos) {
        boolean success = super.placeFluid(fluidStack, blockPos);
        if (success)
            extractEnergy(getStandardEnergyCost(), false);
        return success;
    }

    @Override
    public List<BlockPos> findSpotsToPlace(FakePlayer fakePlayer) {
        AABB area = getAABB(getBlockPos());
        return BlockPos.betweenClosedStream((int) area.minX, (int) area.minY, (int) area.minZ, (int) area.maxX - 1, (int) area.maxY - 1, (int) area.maxZ - 1)
                .filter(blockPos -> isBlockPosValid(blockPos, fakePlayer))
                .map(BlockPos::immutable)
                .sorted(Comparator.comparingDouble(x -> x.distSqr(getBlockPos())))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isBlockPosValid(BlockPos blockPos, FakePlayer fakePlayer) {
        if (!super.isBlockPosValid(blockPos, fakePlayer))
            return false; //Do the same checks as normal, then check the filters
        ItemStack blockItemStack = level.getBlockState(blockPos.relative(getDirectionValue())).getCloneItemStack(new BlockHitResult(Vec3.ZERO, getDirectionValue(), blockPos, false), level, blockPos, null);
        return isStackValidFilter(blockItemStack);
    }
}
