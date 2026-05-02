package com.direwolf20.justdirethings.common.capabilities;

import com.direwolf20.justdirethings.common.blockentities.EnergyTransmitterBE;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class TransmitterEnergyStorage extends MachineEnergyStorage {
    private final EnergyTransmitterBE energyTransmitterBE;

    public TransmitterEnergyStorage(int capacity, EnergyTransmitterBE energyTransmitterBE) {
        super(capacity);
        this.energyTransmitterBE = energyTransmitterBE;
    }

    @Override
    public void setEnergy(int energy) {
        // Direct write, bypassing SimpleEnergyHandler.set so onEnergyChanged is NOT fired.
        // balanceEnergy() calls this to redistribute inside the network; re-triggering the
        // distribute hook here would undo the balance and recurse.
        this.energy = energy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;
        int energyReceived = Math.min(capacity - energy, Math.min(this.maxInsert, maxReceive));
        if (!simulate) {
            energyTransmitterBE.distributeEnergy(energyReceived);
        }
        return energyReceived;
    }

    public int realReceiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;
        int energyReceived = Math.min(capacity - energy, Math.min(this.maxInsert, maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;
        int energyExtracted = Math.min(getEnergyStored(), Math.min(this.maxExtract, maxExtract));
        if (!simulate) {
            energyTransmitterBE.extractEnergy(energyExtracted);
        }
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

    // insert/extract inherit SimpleEnergyHandler's journalled implementations.
    // Network rebalancing is deferred to onEnergyChanged, which fires only after
    // a real commit — so simulations don't leak side effects into other transmitters.

    @Override
    protected void onEnergyChanged(int previousAmount) {
        int delta = energy - previousAmount;
        if (delta == 0) return;
        // Undo the local change, then route it through the network so all transmitters stay balanced.
        if (delta > 0) {
            energy = previousAmount;
            energyTransmitterBE.distributeEnergy(delta);
        } else {
            energy = previousAmount;
            energyTransmitterBE.extractEnergy(-delta);
        }
    }

    @Override
    public int getEnergyStored() {
        return energyTransmitterBE.getTotalEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return energyTransmitterBE.getTotalMaxEnergyStored();
    }

    @Override
    public long getAmountAsLong() {
        return getEnergyStored();
    }

    @Override
    public long getCapacityAsLong() {
        return getMaxEnergyStored();
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
        return this.maxInsert > 0;
    }

    @Override
    public void serialize(ValueOutput output) {
        output.putInt("energy", getRealEnergyStored());
    }

    @Override
    public void deserialize(ValueInput input) {
        this.energy = input.getIntOr("energy", 0);
    }
}
