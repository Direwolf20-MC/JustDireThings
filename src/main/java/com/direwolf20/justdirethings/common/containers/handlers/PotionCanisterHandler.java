package com.direwolf20.justdirethings.common.containers.handlers;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemAccessItemHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class PotionCanisterHandler extends ItemAccessItemHandler {
    public PotionCanisterHandler(ItemAccess itemAccess, DataComponentType<ItemContainerContents> component, int size) {
        super(itemAccess, component, size);
    }

    public PotionCanisterHandler(ItemStack parent, DataComponentType<ItemContainerContents> component, int size) {
        this(ItemAccess.forStack(parent), component, size);
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        if (!super.isValid(index, resource)) return false;
        if (resource.isEmpty()) return true;
        return resource.getItem() instanceof PotionItem || resource.getItem() instanceof BottleItem;
    }
}
