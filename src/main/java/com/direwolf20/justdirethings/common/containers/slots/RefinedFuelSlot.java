package com.direwolf20.justdirethings.common.containers.slots;

import com.direwolf20.justdirethings.common.fluids.basefluids.RefinedFuel;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class RefinedFuelSlot extends ResourceHandlerSlot {
    public RefinedFuelSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(handler, slotModifier, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        if (itemStack.isEmpty())
            return false;
        ResourceHandler<FluidResource> fluidHandler = ItemAccess.forStack(itemStack).getCapability(Capabilities.Fluid.ITEM);
        if (fluidHandler == null)
            return false;
        try (Transaction tx = Transaction.openRoot()) {
            for (int i = 0; i < fluidHandler.size(); i++) {
                FluidResource r = fluidHandler.getResource(i);
                if (r.isEmpty()) continue;
                int extracted = fluidHandler.extract(i, r, 1000, tx);
                if (extracted > 0 && r.getFluid() instanceof RefinedFuel) {
                    return true;
                }
            }
        }
        return false;
    }
}
