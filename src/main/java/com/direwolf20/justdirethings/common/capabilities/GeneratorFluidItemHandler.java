package com.direwolf20.justdirethings.common.capabilities;

import com.direwolf20.justdirethings.common.fluids.basefluids.RefinedFuel;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.ItemStackHandler;

public class GeneratorFluidItemHandler extends ItemStackHandler {
    public GeneratorFluidItemHandler() {
        super(1);
    }

    public GeneratorFluidItemHandler(int size) {
        super(size);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack itemStack) {
        if (itemStack.isEmpty())
            return false;
        IFluidHandlerItem fluidHandlerItem = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandlerItem == null)
            return false;
        FluidStack fluidStack = fluidHandlerItem.drain(1000, IFluidHandler.FluidAction.SIMULATE);
        if (fluidStack.getAmount() == 0)
            return false;
        if (!(fluidStack.getFluid() instanceof RefinedFuel))
            return false;
        return true;
    }
}
