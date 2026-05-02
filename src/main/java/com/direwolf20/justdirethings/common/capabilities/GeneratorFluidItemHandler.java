package com.direwolf20.justdirethings.common.capabilities;

import com.direwolf20.justdirethings.common.fluids.basefluids.RefinedFuel;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class GeneratorFluidItemHandler extends ItemStacksResourceHandler {
    public GeneratorFluidItemHandler() {
        super(1);
    }

    public GeneratorFluidItemHandler(int size) {
        super(size);
    }

    @Override
    public boolean isValid(int slot, ItemResource resource) {
        if (resource.isEmpty()) return false;
        ResourceHandler<FluidResource> fluidHandler = ItemAccess.forStack(resource.toStack()).getCapability(Capabilities.Fluid.ITEM);
        if (fluidHandler == null) return false;
        try (Transaction tx = Transaction.openRoot()) {
            for (int i = 0; i < fluidHandler.size(); i++) {
                FluidResource fluidResource = fluidHandler.getResource(i);
                if (fluidResource.isEmpty()) continue;
                int extracted = fluidHandler.extract(i, fluidResource, 1000, tx);
                if (extracted > 0 && fluidResource.getFluid() instanceof RefinedFuel)
                    return true;
            }
        }
        return false;
    }
}
