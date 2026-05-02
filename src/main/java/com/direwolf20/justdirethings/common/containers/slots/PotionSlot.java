package com.direwolf20.justdirethings.common.containers.slots;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class PotionSlot extends ResourceHandlerSlot {
    private final int slotIndex;

    public PotionSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(handler, slotModifier, index, xPosition, yPosition);
        this.slotIndex = index;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return getResourceHandler().isValid(slotIndex, ItemResource.of(stack));
    }
}
