package com.direwolf20.justdirethings.common.items.interfaces;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.direwolf20.justdirethings.util.NBTHelpers.getBoolean;
import static com.direwolf20.justdirethings.util.NBTHelpers.setBoolean;

public interface ToggleableItem {
    default boolean getEnabled(ItemStack stack) {
        return getBoolean(stack, "enabled");
    }

    default boolean setEnabled(ItemStack stack, boolean enabled) {
        return setBoolean(stack, "enabled", enabled);
    }

    default boolean toggleEnabled(ItemStack stack) {
        return setBoolean(stack, "enabled", !getEnabled(stack));
    }

    static ItemStack getToggleableItem(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        if (mainHand.getItem() instanceof ToggleableItem)
            return mainHand;
        ItemStack offHand = player.getOffhandItem();
        if (offHand.getItem() instanceof ToggleableItem)
            return offHand;
        return ItemStack.EMPTY;
    }
}
