package com.direwolf20.justdirethings.common.blockentities.basebe;

import net.minecraft.world.inventory.ContainerData;

public interface PoweredMachineBE {
    default int getMaxEnergy() {
        return 100000;
    }

    int getEnergyStored();

    PoweredMachineContainerData getContainerData();

    // Static inner class that implements ContainerData
    class PoweredMachineContainerData implements ContainerData {
        private final PoweredMachineBE machine;

        public PoweredMachineContainerData(PoweredMachineBE machine) {
            this.machine = machine;
        }

        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return this.machine.getEnergyStored() / 32;
                default:
                    throw new IllegalArgumentException("Invalid index: " + index);
            }
        }

        @Override
        public void set(int index, int value) {
            throw new UnsupportedOperationException("Cannot set values through ContainerData");
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
}
