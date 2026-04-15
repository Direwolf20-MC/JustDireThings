package com.direwolf20.justdirethings.common.capabilities;

import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler;

public class EnergyStorageItemstack extends ItemAccessEnergyHandler {

    public EnergyStorageItemstack(ItemAccess itemAccess, int capacity) {
        super(itemAccess, JustDireDataComponents.FORGE_ENERGY.get(), capacity);
    }

    public EnergyStorageItemstack(ItemAccess itemAccess, int capacity, int maxInsert, int maxExtract) {
        super(itemAccess, JustDireDataComponents.FORGE_ENERGY.get(), capacity, maxInsert, maxExtract);
    }
}
