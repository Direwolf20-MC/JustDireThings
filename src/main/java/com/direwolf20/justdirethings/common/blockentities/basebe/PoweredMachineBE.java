package com.direwolf20.justdirethings.common.blockentities.basebe;

import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import net.minecraft.world.inventory.ContainerData;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public interface PoweredMachineBE {
    default int getMaxEnergy() {
        return 100000;
    }

    ContainerData getContainerData();

    MachineEnergyStorage getEnergyStorage();

    default int getEnergyStored() {
        return getEnergyStorage().getEnergyStored();
    }

    default void setEnergyStored(int value) {
        getEnergyStorage().setEnergy(value);
    }

    int getStandardEnergyCost();

    default boolean hasEnoughPower(int power) {
        return getEnergyStorage().extractEnergy(power, true) >= power;
    }

    default int insertEnergy(int power, boolean simulate) {
        return getEnergyStorage().receiveEnergy(power, simulate);
    }

    default int extractEnergy(int power, boolean simulate) {
        return getEnergyStorage().extractEnergy(power, simulate);
    }

    default void chargeItemStack(ResourceHandler<ItemResource> handler, int slotIndex) {
        if (handler.getResource(slotIndex).isEmpty()) return;
        EnergyHandler slotEnergy = ItemAccess.forHandlerIndex(handler, slotIndex).getCapability(Capabilities.Energy.ITEM);
        if (slotEnergy == null) return;
        int acceptedEnergy;
        try (Transaction simTx = Transaction.openRoot()) {
            acceptedEnergy = slotEnergy.insert(5000, simTx);
        }
        if (acceptedEnergy <= 0) return;
        try (Transaction tx = Transaction.openRoot()) {
            int extractedEnergy = getEnergyStorage().extract(acceptedEnergy, tx);
            slotEnergy.insert(extractedEnergy, tx);
            tx.commit();
        }
    }
}
