package com.direwolf20.justdirethings.common.capabilities;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.ItemAccessFluidHandler;

/**
 * 26.1 replacement for the deprecated NeoForge {@code FluidHandlerItemStack} template.
 * Wraps {@link ItemAccessFluidHandler} and exposes an overridable {@link #isFluidValid(FluidResource)}
 * hook so per-item fluid restrictions (e.g. "only portal fluid") can still be declared inline.
 */
public class FluidHandlerItemStack extends ItemAccessFluidHandler {

    public FluidHandlerItemStack(ItemAccess itemAccess, DataComponentType<SimpleFluidContent> component, int capacity) {
        super(itemAccess, component, capacity);
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return super.isValid(index, resource) && isFluidValid(resource);
    }

    /**
     * Override to restrict which fluids this handler accepts. Default: allow any.
     */
    public boolean isFluidValid(FluidResource resource) {
        return true;
    }
}
