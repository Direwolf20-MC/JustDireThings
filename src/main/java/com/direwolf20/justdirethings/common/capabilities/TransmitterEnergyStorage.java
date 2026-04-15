package com.direwolf20.justdirethings.common.capabilities;

import com.direwolf20.justdirethings.common.blockentities.EnergyTransmitterBE;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

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
        int energyReceived = Math.min(capacity - energy, Math.min(this.maxInsert, maxReceive));
        if (!simulate) {
            energyTransmitterBE.distributeEnergy(energyReceived);
        }
        return energyReceived;
    }

    @Override
    public int receiveEnergy(int maxReceive, TransactionContext tx) {
        // Keep shim behavior: forward to network distribution. Transactions are not used by
        // the transmitter's custom accounting, so we simulate + commit only for side-effects.
        return receiveEnergy(maxReceive, false);
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

    @Override
    public int extractEnergy(int maxExtract, TransactionContext tx) {
        return extractEnergy(maxExtract, false);
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
    public int insert(int amount, TransactionContext tx) {
        // Match receiveEnergy semantics: route through network distribution on commit.
        // Use a simulate-first pattern: we compute the acceptable amount from the local
        // buffer, then on commit forward the real distribution call.
        if (!canReceive()) return 0;
        int received = Math.min(capacity - energy, Math.min(this.maxInsert, amount));
        if (received > 0) {
            // Note: distribution happens outside transaction semantics. For most transmitter
            // call sites we use receiveEnergy(int, boolean) directly; this override exists
            // only so Transaction-based callers (Capabilities.Energy.BLOCK lookups) get
            // consistent behavior.
            try (Transaction nested = Transaction.open(tx)) {
                energyTransmitterBE.distributeEnergy(received);
                nested.commit();
            }
        }
        return received;
    }

    @Override
    public int extract(int amount, TransactionContext tx) {
        if (!canExtract()) return 0;
        int extracted = Math.min(getEnergyStored(), Math.min(this.maxExtract, amount));
        if (extracted > 0) {
            try (Transaction nested = Transaction.open(tx)) {
                energyTransmitterBE.extractEnergy(extracted);
                nested.commit();
            }
        }
        return extracted;
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
