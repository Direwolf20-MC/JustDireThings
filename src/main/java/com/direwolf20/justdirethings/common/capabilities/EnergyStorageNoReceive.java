package com.direwolf20.justdirethings.common.capabilities;

public class EnergyStorageNoReceive extends MachineEnergyStorage {

    public EnergyStorageNoReceive(int capacity) {
        super(capacity);
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    /**
     * Pocket Generators need to be able to power themselves, but not receive energy from anything else. This is used to do this.
     */
    public int forceReceiveEnergy(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }
}
