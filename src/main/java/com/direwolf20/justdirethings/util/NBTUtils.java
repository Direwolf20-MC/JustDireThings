package com.direwolf20.justdirethings.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class NBTUtils {
    public static boolean toggleBoolean(ItemStack itemStack, String name) {
        CompoundTag tagCompound = itemStack.getOrCreateTag();
        tagCompound.putBoolean(name, !tagCompound.getBoolean(name));
        return tagCompound.getBoolean(name);
    }

    public static boolean getBoolean(ItemStack itemStack, String name) {
        return itemStack.getOrCreateTag().getBoolean(name);
    }

    public static boolean setBoolean(ItemStack itemStack, String name, boolean value) {
        itemStack.getOrCreateTag().putBoolean(name, value);
        return value;
    }

    public static int getIntValue(ItemStack itemStack, String name) {
        return itemStack.getOrCreateTag().getInt(name);
    }

    public static int setIntValue(ItemStack itemStack, String name, int value) {
        itemStack.getOrCreateTag().putInt(name, value);
        return value;
    }
}
