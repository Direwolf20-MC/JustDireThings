package com.direwolf20.justdirethings.common.capabilities;

import com.direwolf20.justdirethings.common.blockentities.EnergyTransmitterBE;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;

public class TransmitterEnergyStorage extends MachineEnergyStorage {
    private final EnergyTransmitterBE energyTransmitterBE;

    public TransmitterEnergyStorage(int capacity, EnergyTransmitterBE energyTransmitterBE) {
        super(capacity);
        this.energyTransmitterBE = energyTransmitterBE;
    }

    @Override
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), Math.min(this.maxReceive, maxReceive));
        if (!simulate) {
            energyTransmitterBE.distributeEnergy(energyReceived);
            //energy += energyReceived;
        }
        return energyReceived;
    }

    public int realReceiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min(getEnergyStored(), Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    public int realExtractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return energyTransmitterBE.getTotalEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return energyTransmitterBE.getTotalMaxEnergyStored();
    }

    public int getRealEnergyStored() {
        return energy;
    }

    public int getRealMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }

    @Override
    public Tag serializeNBT() {
        return IntTag.valueOf(this.getRealEnergyStored());
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        if (!(nbt instanceof IntTag intNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.energy = intNbt.getAsInt();
    }
}