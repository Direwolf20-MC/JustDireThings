package com.direwolf20.justdirethings.common.capabilities;

import net.neoforged.neoforge.energy.EnergyStorage;

public class MachineEnergyStorage extends EnergyStorage {

    public MachineEnergyStorage(int capacity) {
        super(capacity);
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}