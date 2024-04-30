package com.direwolf20.justdirethings.common.capabilities;

import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import net.minecraft.world.item.ItemStack;

public class EnergyStorageItemStackNoReceive extends EnergyStorageItemstack {

    public EnergyStorageItemStackNoReceive(int capacity, ItemStack itemStack) {
        super(capacity, itemStack);
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
        if (!simulate) {
            energy += energyReceived;
            itemStack.set(JustDireDataComponents.FORGE_ENERGY, energy);
        }
        return energyReceived;
    }
}
