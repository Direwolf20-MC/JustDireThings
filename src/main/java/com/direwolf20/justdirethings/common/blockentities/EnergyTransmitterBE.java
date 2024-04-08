package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.*;
import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnergyTransmitterBE extends BaseMachineBE implements RedstoneControlledBE, PoweredMachineBE, AreaAffectingBE {
    public RedstoneControlData redstoneControlData = new RedstoneControlData();
    public final PoweredMachineContainerData poweredMachineData;
    private final Map<BlockPos, BlockCapabilityCache<IEnergyStorage, Direction>> energyHandlers = new HashMap<>();
    private List<BlockPos> blocksToCharge = new ArrayList<>();
    private List<BlockPos> transmittersToBalance = new ArrayList<>();
    public AreaAffectingData areaAffectingData = new AreaAffectingData();

    public EnergyTransmitterBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 1;
        poweredMachineData = new PoweredMachineContainerData(this);
    }

    public EnergyTransmitterBE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.EnergyTransmitterBE.get(), pPos, pBlockState);
    }

    @Override
    public AreaAffectingData getAreaAffectingData() {
        return areaAffectingData;
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
    public ContainerData getContainerData() {
        return poweredMachineData;
    }

    @Override
    public MachineEnergyStorage getEnergyStorage() {
        return getData(Registration.ENERGYSTORAGE_MACHINES);
    }

    @Override
    public int getStandardEnergyCost() {
        return 0;
    }

    @Override
    public void tickClient() {
    }

    @Override
    public void tickServer() {
        super.tickServer();
        providePower();
    }

    public IEnergyStorage getHandler(BlockPos blockPos) {
        var tempStorage = energyHandlers.get(blockPos);
        if (tempStorage == null) {
            boolean foundAcceptableSide = false;
            for (Direction direction : Direction.values()) {
                tempStorage = BlockCapabilityCache.create(
                        Capabilities.EnergyStorage.BLOCK, // capability to cache
                        (ServerLevel) level, // level
                        blockPos, // target position
                        direction // context (The side of the block we're trying to pull/push from?)
                );
                if (tempStorage.getCapability() != null && tempStorage.getCapability().canReceive()) {
                    energyHandlers.put(blockPos, tempStorage);
                    foundAcceptableSide = true;
                    break;
                }
            }
            if (!foundAcceptableSide)
                energyHandlers.put(blockPos, tempStorage); //Put the last one we checked, even if it can't receive!
        }
        return tempStorage.getCapability();
    }

    public void providePower() {
        if (getEnergyStorage().getEnergyStored() <= 0) return; //Don't bother if we're empty!
        for (BlockPos blockPos : blocksToCharge) {
            IEnergyStorage iEnergyStorage = getHandler(blockPos);
            if (iEnergyStorage == null) continue;
            int amtFit = iEnergyStorage.receiveEnergy(fePerTick(), true);
            if (amtFit <= 0) continue;
            int extractAmt = extractEnergy(amtFit, false);
            iEnergyStorage.receiveEnergy(extractAmt, false);
        }
        for (BlockPos blockPos : transmittersToBalance) { //TODO Logic
            IEnergyStorage iEnergyStorage = getHandler(blockPos);
            if (iEnergyStorage == null) continue;
            int amtFit = iEnergyStorage.receiveEnergy(fePerTick(), true);
            if (amtFit <= 0) continue;
            int extractAmt = extractEnergy(amtFit, false);
            iEnergyStorage.receiveEnergy(extractAmt, false);
        }
    }

    public void getBlocksToCharge() {

    }

    @Override
    public void handleTicks() {
        //NoOp
    }

    public int fePerTick() {
        return 1000; //Todo Config?
    }

    @Override
    public int getMaxEnergy() {
        return 1000000; //Todo Config?
    }

    @Override
    public boolean isDefaultSettings() {
        if (!super.isDefaultSettings())
            return false;
        return true;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
    }
}
