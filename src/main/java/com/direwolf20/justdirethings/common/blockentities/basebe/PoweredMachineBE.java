package com.direwolf20.justdirethings.common.blockentities.basebe;

import com.direwolf20.justdirethings.common.capabilities.MachineEnergyStorage;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public interface PoweredMachineBE {
    default int getMaxEnergy() {
        return 100000;
    }

    PoweredMachineContainerData getContainerData();

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

    default void chargeItemStack(ItemStack itemStack) {
        IEnergyStorage slotEnergy = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (slotEnergy != null) {
            int acceptedEnergy = slotEnergy.receiveEnergy(500, true); //Todo Config?
            if (acceptedEnergy > 0) {
                int extractedEnergy = getEnergyStorage().extractEnergy(acceptedEnergy, false);
                slotEnergy.receiveEnergy(extractedEnergy, false);
            }
        }
    }
}
