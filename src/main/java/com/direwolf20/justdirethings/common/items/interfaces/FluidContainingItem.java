package com.direwolf20.justdirethings.common.items.interfaces;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public interface FluidContainingItem {
    default int getMaxMB() {
        return 8000;
    }

    static int getAvailableFluid(ItemStack stack) {
        IFluidHandlerItem fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return -1;
        }
        return fluidHandler.getFluidInTank(0).getAmount();
    }

    default boolean isFluidBarVisible(ItemStack stack) {
        IFluidHandlerItem fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return false;
        }
        return (fluidHandler.getFluidInTank(0).getAmount() < fluidHandler.getTankCapacity(0));
    }

    default int getFluidBarWidth(ItemStack stack) {
        IFluidHandlerItem fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return 13;
        }
        return Math.min(13 * fluidHandler.getFluidInTank(0).getAmount() / fluidHandler.getTankCapacity(0), 13);
    }

    default int getFluidBarColor(ItemStack stack) {
        IFluidHandlerItem fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return -1; //Tell caller to call super
        }
        float hue = 0.55F; // Starting from a deeper blue, moving slightly darker as it fills
        float saturation = 1.0F; // Slightly reduce saturation as it fills

        return Mth.hsvToRgb(hue, saturation, 1.0F);
    }

    static boolean hasEnoughFluid(ItemStack itemStack, int amt) {
        IFluidHandlerItem fluidHandler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return false;
        }
        return fluidHandler.getFluidInTank(0).getAmount() >= amt;
    }

    static void consumeFluid(ItemStack itemStack, int amt) {
        IFluidHandlerItem fluidHandler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return;
        }
        fluidHandler.drain(amt, IFluidHandler.FluidAction.EXECUTE);
    }
}
