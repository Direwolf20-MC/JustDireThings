package com.direwolf20.justdirethings.common.blockentities.basebe;

import net.minecraft.world.inventory.ContainerData;

public interface PoweredMachineBE {
    default int getMaxEnergy() {
        return 100000;
    }

    int getEnergyStored();

    ContainerData getContainerData();
}
