package com.direwolf20.justdirethings.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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

    public static CompoundTag globalPosToNBT(GlobalPos globalPos) {
        CompoundTag tag = new CompoundTag();
        tag.putString("level", globalPos.dimension().location().toString());
        tag.put("blockpos", NbtUtils.writeBlockPos(globalPos.pos()));
        return tag;
    }

    public static GlobalPos nbtToGlobalPos(CompoundTag tag) {
        ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("level")));
        BlockPos blockPos = NbtUtils.readBlockPos(tag.getCompound("blockpos"));
        return GlobalPos.of(levelKey, blockPos);
    }
}
