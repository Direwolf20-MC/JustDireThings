package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.AreaAffectingBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineContainerData;
import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SensorT2BE extends SensorT1BE implements AreaAffectingBE, PoweredMachineBE {
    public AreaAffectingData areaAffectingData = new AreaAffectingData();
    public final PoweredMachineContainerData poweredMachineData;

    public SensorT2BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        poweredMachineData = new PoweredMachineContainerData(this);
    }

    public SensorT2BE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.SensorT2BE.get(), pPos, pBlockState);
    }

    @Override
    public FilterBasicHandler getFilterHandler() {
        return getData(Registration.HANDLER_BASIC_FILTER);
    }

    @Override
    public AreaAffectingData getAreaAffectingData() {
        return areaAffectingData;
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
        return 2;
    }

    public int getEnergyCost() {
        AABB aabb = getAABB(getBlockPos());
        int width = (int) Math.abs(Math.floor(aabb.maxX) - Math.floor(aabb.minX));
        int height = (int) Math.abs(Math.floor(aabb.maxY) - Math.floor(aabb.minY));
        int depth = (int) Math.abs(Math.floor(aabb.maxZ) - Math.floor(aabb.minZ));
        return (width * height * depth) * getStandardEnergyCost();
    }

    @Override
    public boolean canSense() {
        int cost = getEnergyCost();
        return extractEnergy(cost, false) >= cost; //Really extract because if we pass this we'll be scanning!
    }

    public List<Entity> findEntities(AABB aabb) {
        List<Entity> returnList = new ArrayList<>(level.getEntitiesOfClass(Entity.class, getAABB(getBlockPos()), this::isValidEntity));

        return returnList;
    }

    public AABB getAABB() {
        return getAABB(getBlockPos());
    }

    public List<BlockPos> findPositions() {
        AABB area = getAABB(getBlockPos());
        return BlockPos.betweenClosedStream((int) area.minX, (int) area.minY, (int) area.minZ, (int) area.maxX - 1, (int) area.maxY - 1, (int) area.maxZ - 1)
                .filter(this::isBlockPosValid)
                .map(BlockPos::immutable)
                .sorted(Comparator.comparingDouble(x -> x.distSqr(getBlockPos())))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isDefaultSettings() {
        if (!super.isDefaultSettings())
            return false;
        return true;
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("senseTarget", sense_target.ordinal());
        tag.putBoolean("strongSignal", strongSignal);
        tag.put("blockStateProps", saveBlockStateProperties());
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        this.sense_target = SENSE_TARGET.values()[tag.getInt("senseTarget")];
        this.strongSignal = tag.getBoolean("strongSignal");
        super.loadAdditional(tag, provider);
        loadBlockStateProperties(tag.getCompound("blockStateProps")); //Do this after the filter data comes in, so we know the itemstack in the filter
    }
}
