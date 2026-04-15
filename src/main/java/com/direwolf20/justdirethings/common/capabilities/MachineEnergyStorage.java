package com.direwolf20.justdirethings.common.capabilities;

import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class MachineEnergyStorage extends SimpleEnergyHandler {

    public MachineEnergyStorage(int capacity) {
        super(capacity, capacity, capacity, 0);
    }

    public MachineEnergyStorage(int capacity, int maxInsert, int maxExtract) {
        super(capacity, maxInsert, maxExtract, 0);
    }

    public void setEnergy(int energy) {
        set(energy);
    }

    public int getEnergyStored() {
        return getAmountAsInt();
    }

    public int getMaxEnergyStored() {
        return getCapacityAsInt();
    }

    public int receiveEnergy(int maxReceive, boolean simulate) {
        try (Transaction tx = Transaction.openRoot()) {
            int inserted = insert(maxReceive, tx);
            if (!simulate) tx.commit();
            return inserted;
        }
    }

    public int extractEnergy(int maxExtract, boolean simulate) {
        try (Transaction tx = Transaction.openRoot()) {
            int extracted = extract(maxExtract, tx);
            if (!simulate) tx.commit();
            return extracted;
        }
    }

    public int receiveEnergy(int maxReceive, TransactionContext tx) {
        return insert(maxReceive, tx);
    }

    public int extractEnergy(int maxExtract, TransactionContext tx) {
        return extract(maxExtract, tx);
    }

    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    public boolean canReceive() {
        return this.maxInsert > 0;
    }
}
