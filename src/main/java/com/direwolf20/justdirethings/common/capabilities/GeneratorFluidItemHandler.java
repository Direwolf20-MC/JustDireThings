package com.direwolf20.justdirethings.common.capabilities;

import com.direwolf20.justdirethings.common.fluids.basefluids.RefinedFuel;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

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
        ResourceHandler<FluidResource> fluidHandler = ItemAccess.forStack(itemStack).getCapability(Capabilities.Fluid.ITEM);
        if (fluidHandler == null)
            return false;
        try (Transaction tx = Transaction.openRoot()) {
            for (int i = 0; i < fluidHandler.size(); i++) {
                FluidResource resource = fluidHandler.getResource(i);
                if (resource.isEmpty()) continue;
                int extracted = fluidHandler.extract(i, resource, 1000, tx);
                if (extracted > 0 && resource.getFluid() instanceof RefinedFuel)
                    return true;
            }
        }
        return false;
    }
}
