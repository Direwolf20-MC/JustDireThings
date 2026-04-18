package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.AreaAffectingBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineContainerData;
import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SensorT2BE extends SensorT1BE implements AreaAffectingBE, PoweredMachineBE {
    public AreaAffectingData areaAffectingData = new AreaAffectingData(getBlockState().getValue(BlockStateProperties.FACING));
    public final PoweredMachineContainerData poweredMachineData;

    public SensorT2BE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        poweredMachineData = new PoweredMachineContainerData(this);
    }

    public SensorT2BE(BlockPos pPos, BlockState pBlockState) {
        this(JDTRegistration.SensorT2BE.get(), pPos, pBlockState);
    }

    @Override
    public FilterBasicHandler getFilterHandler() {
        return getData(JDTRegistration.HANDLER_BASIC_FILTER);
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
        return getData(JDTRegistration.ENERGYSTORAGE_MACHINES);
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
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("senseTarget", sense_target.ordinal());
        output.putBoolean("strongSignal", strongSignal);
        output.store("blockStateProps", CompoundTag.CODEC, saveBlockStateProperties());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        this.sense_target = SENSE_TARGET.values()[input.getIntOr("senseTarget", 0)];
        this.strongSignal = input.getBooleanOr("strongSignal", strongSignal);
        super.loadAdditional(input);
        loadBlockStateProperties(input.read("blockStateProps", CompoundTag.CODEC).orElseGet(CompoundTag::new)); //Do this after the filter data comes in, so we know the itemstack in the filter
    }
}
