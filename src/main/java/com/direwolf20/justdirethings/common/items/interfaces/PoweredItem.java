package com.direwolf20.justdirethings.common.items.interfaces;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public interface PoweredItem {
    static int getAvailableEnergy(ItemStack stack) {
        EnergyHandler energy = stack.getCapability(Capabilities.Energy.ITEM, null);
        if (energy == null) {
            return -1;
        }
        return energy.getAmountAsInt();
    }

    static int getMaxEnergyFor(ItemStack stack) {
        EnergyHandler energy = stack.getCapability(Capabilities.Energy.ITEM, null);
        if (energy == null) {
            return 0;
        }
        return energy.getCapacityAsInt();
    }

    default boolean isPowerBarVisible(ItemStack stack) {
        EnergyHandler energy = stack.getCapability(Capabilities.Energy.ITEM, null);
        if (energy == null) {
            return false;
        }
        return (energy.getAmountAsInt() < energy.getCapacityAsInt());
    }

    default int getPowerBarWidth(ItemStack stack) {
        EnergyHandler energy = stack.getCapability(Capabilities.Energy.ITEM, null);
        if (energy == null) {
            return 13;
        }
        int cap = energy.getCapacityAsInt();
        if (cap <= 0) return 13;
        return Math.min(13 * energy.getAmountAsInt() / cap, 13);
    }

    default int getPowerBarColor(ItemStack stack) {
        EnergyHandler energy = stack.getCapability(Capabilities.Energy.ITEM, null);
        if (energy == null) {
            return -1; //Tell caller to call super
        }
        int cap = energy.getCapacityAsInt();
        if (cap <= 0) return -1;
        return Mth.hsvToRgb(Math.max(0.0F, (float) energy.getAmountAsInt() / (float) cap) / 3.0F, 1.0F, 1.0F);
    }

    default int getMaxEnergy() {
        return 10000;
    }

    static boolean hasEnoughEnergy(ItemStack stack, int requiredAmt) {
        return getAvailableEnergy(stack) >= requiredAmt;
    }

    static boolean consumeEnergy(ItemStack itemStack, int amt) {
        EnergyHandler energy = itemStack.getCapability(Capabilities.Energy.ITEM, null);
        if (energy == null) return false;
        int amtExtracted;
        try (Transaction tx = Transaction.openRoot()) {
            amtExtracted = energy.extract(amt, tx);
            tx.commit();
        }
        return amtExtracted == amt;
    }
}
