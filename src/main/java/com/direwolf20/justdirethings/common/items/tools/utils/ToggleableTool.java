package com.direwolf20.justdirethings.common.items.tools.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public interface ToggleableTool {
    EnumSet<ToolAbility> getAbilities();

    default void registerAbility(ToolAbility ability) {
        getAbilities().add(ability);
    }

    default boolean hasAbility(ToolAbility ability) {
        return getAbilities().contains(ability);
    }

    default boolean canUseAbility(ItemStack itemStack, ToolAbility toolAbility) {
        return hasAbility(toolAbility) && getEnabled(itemStack) && getSetting(itemStack, toolAbility.name);
    }

    static ItemStack getToggleableTool(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        if (mainHand.getItem() instanceof ToggleableTool)
            return mainHand;
        ItemStack offHand = player.getOffhandItem();
        if (offHand.getItem() instanceof ToggleableTool)
            return offHand;
        return ItemStack.EMPTY;
    }

    static boolean toggleSetting(ItemStack stack, String setting) {
        CompoundTag tagCompound = stack.getOrCreateTag();
        tagCompound.putBoolean(setting, !getSetting(stack, setting));
        return tagCompound.getBoolean(setting);
    }

    static boolean getSetting(ItemStack stack, String setting) {
        CompoundTag tagCompound = stack.getOrCreateTag();
        return !tagCompound.contains(setting) || tagCompound.getBoolean(setting); //Enabled by default
    }

    static boolean getEnabled(ItemStack stack) {
        CompoundTag tagCompound = stack.getOrCreateTag();
        return getSetting(stack, "enabled");
    }

    static boolean toggleEnabled(ItemStack stack) {
        return toggleSetting(stack, "enabled");
    }

    static void setToolValue(ItemStack stack, int value, String valueName) {
        stack.getOrCreateTag().putInt(valueName, value);
    }

    static int getToolValue(ItemStack stack, String valueName) {
        return stack.getOrCreateTag().getInt(valueName);
    }
}
