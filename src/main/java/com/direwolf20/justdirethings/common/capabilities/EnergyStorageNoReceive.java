package com.direwolf20.justdirethings.common.capabilities;

public class EnergyStorageNoReceive extends MachineEnergyStorage {

    public EnergyStorageNoReceive(int capacity) {
        // maxInsert = 0 (blocks external insert), maxExtract = capacity (external pull allowed)
        super(capacity, 0, capacity);
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    /**
     * Pocket Generators need to be able to power themselves, but not receive energy from anything else.
     * Bypasses the {@code maxInsert=0} limit by writing directly through {@link #set}.
     */
    public int forceReceiveEnergy(int maxReceive, boolean simulate) {
        int current = getAmountAsInt();
        int capacityInt = getCapacityAsInt();
        int energyReceived = Math.min(capacityInt - current, Math.max(0, maxReceive));
        if (!simulate && energyReceived > 0) {
            set(current + energyReceived);
        }
        return energyReceived;
    }
}
