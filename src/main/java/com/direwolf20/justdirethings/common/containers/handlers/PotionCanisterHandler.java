package com.direwolf20.justdirethings.common.containers.handlers;

import com.direwolf20.justdirethings.common.items.PotionCanister;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.items.ComponentItemHandler;

public class PotionCanisterHandler extends ComponentItemHandler {
    private final ItemStack potionStack;

    public PotionCanisterHandler(ItemStack parent, DataComponentType<ItemContainerContents> component, int size) {
        super(parent, component, size);
        potionStack = parent;
    }

    @Override
    protected void onContentsChanged(int slot, ItemStack oldStack, ItemStack newStack) {
        if (!newStack.isEmpty() && newStack.getItem() instanceof PotionItem) {
            PotionCanister.attemptFill(potionStack);
        }
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.getItem() instanceof PotionItem || stack.getItem() instanceof BottleItem || stack.isEmpty();
    }
}
