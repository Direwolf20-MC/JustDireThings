package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.*;
import com.direwolf20.justdirethings.common.capabilities.JustDireFluidTank;
import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

public class ParadoxMachineBE extends BaseMachineBE implements PoweredMachineBE, AreaAffectingBE, FilterableBE, RedstoneControlledBE, FluidMachineBE {
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    public FilterData filterData = new FilterData();
    public final FluidContainerData fluidContainerData;
    public AreaAffectingData areaAffectingData = new AreaAffectingData(getBlockState().getValue(BlockStateProperties.FACING));
    public final PoweredMachineContainerData poweredMachineData;

    public ParadoxMachineBE(BlockPos pPos, BlockState pBlockState) {
        super(Registration.ParadoxMachineBE.get(), pPos, pBlockState);
        poweredMachineData = new PoweredMachineContainerData(this);
        fluidContainerData = new FluidContainerData(this);
    }

    @Override
    public void tickClient() {
    }

    @Override
    public void tickServer() {
        super.tickServer();
        doParadox();
    }

    public void doParadox() {

    }

    @Override
    public int getMaxMB() {
        return 16000;
    }

    @Override
    public JustDireFluidTank getFluidTank() {
        return getData(Registration.MACHINE_FLUID_HANDLER);
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
    public RedstoneControlData getDefaultRedstoneData() {
        return new RedstoneControlData(MiscHelpers.RedstoneMode.PULSE);
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
    public int getMaxEnergy() {
        return 10000000;
    }

    @Override
    public int getStandardEnergyCost() {
        return 5000;
    }

    public int getEnergyCost(int numBlocks) {
        return numBlocks * getStandardEnergyCost();
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

    public void setAreaOnly(double x, double y, double z) {
        getAreaAffectingData().xRadius = Math.max(0, Math.min(x, maxRadius));
        getAreaAffectingData().yRadius = Math.max(0, Math.min(y, maxRadius));
        getAreaAffectingData().zRadius = Math.max(0, Math.min(z, maxRadius));
        getAreaAffectingData().area = null;
        if (getBlockEntity() instanceof BaseMachineBE baseMachineBE)
            baseMachineBE.markDirtyClient();
    }

    public boolean canRun() {
        AABB aabb = getAABB(getBlockPos());
        int width = (int) Math.abs(Math.floor(aabb.maxX) - Math.floor(aabb.minX));
        int height = (int) Math.abs(Math.floor(aabb.maxY) - Math.floor(aabb.minY));
        int depth = (int) Math.abs(Math.floor(aabb.maxZ) - Math.floor(aabb.minZ));
        return hasEnoughPower(getEnergyCost((width * height * depth)));
    }

    public void postRun(int numBlocks) {
        extractEnergy(getEnergyCost(numBlocks), false);
    }

    public BlockPos getStartingPoint() {
        return getBlockPos().offset(getAreaAffectingData().xOffset, getAreaAffectingData().yOffset, getAreaAffectingData().zOffset);
    }

    public boolean isBlockPosValid(ServerLevel serverLevel, BlockPos blockPos) {
        return true;
    }
}
