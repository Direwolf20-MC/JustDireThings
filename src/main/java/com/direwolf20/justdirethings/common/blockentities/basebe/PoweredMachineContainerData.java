package com.direwolf20.justdirethings.common.blockentities.basebe;

import net.minecraft.world.inventory.ContainerData;

public class PoweredMachineContainerData implements ContainerData {
    PoweredMachineBE poweredMachineBE;

    public PoweredMachineContainerData(PoweredMachineBE poweredMachineBE) {
        this.poweredMachineBE = poweredMachineBE;
    }

    @Override
    public int get(int index) {
        return switch (index) {
            case 0 -> poweredMachineBE.getEnergyStored() & 0xFFFF;
            case 1 -> poweredMachineBE.getEnergyStored() >> 16;
            default -> throw new IllegalArgumentException("Invalid index: " + index);
        };
    }

    @Override
    public void set(int index, int value) {
        switch (index) {
            case 0 -> poweredMachineBE.setEnergyStored((poweredMachineBE.getEnergyStored() & 0xFFFF0000) | (value & 0xFFFF));
            case 1 -> poweredMachineBE.setEnergyStored((poweredMachineBE.getEnergyStored() & 0xFFFF) | (value << 16));
            default -> throw new IllegalArgumentException("Invalid index: " + index);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
