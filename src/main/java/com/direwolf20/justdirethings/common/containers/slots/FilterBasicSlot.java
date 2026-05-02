package com.direwolf20.justdirethings.common.containers.slots;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class FilterBasicSlot extends ResourceHandlerSlot {
    public FilterBasicSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(handler, slotModifier, index, xPosition, yPosition);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }
}
