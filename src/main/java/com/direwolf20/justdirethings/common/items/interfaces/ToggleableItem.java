package com.direwolf20.justdirethings.common.items.interfaces;

import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool.getSetting;

public interface ToggleableItem {
    default boolean getEnabled(ItemStack stack) {
        return getSetting(stack, "enabled");
    }

    default void toggleEnabled(ItemStack stack) {
        stack.update(JustDireDataComponents.TOOL_ENABLED, true, v -> !v);
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
