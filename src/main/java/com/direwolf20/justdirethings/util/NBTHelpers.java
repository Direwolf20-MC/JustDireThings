package com.direwolf20.justdirethings.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NBTHelpers {
    public record GlobalVec3(ResourceKey<Level> dimension, Vec3 position) {
        public String toVec3ShortString() {
            return String.format("%.2f, %.2f, %.2f", position.x(), position.y(), position.z());
        }
    }

    public record BoundInventory(GlobalPos globalPos, Direction direction) {
        public static BoundInventory fromNBT(CompoundTag tag) {
            BoundInventory boundInventory = null;
            if (tag.contains("boundinventory") && tag.contains("boundinventory_side"))
                boundInventory = new BoundInventory(NBTHelpers.nbtToGlobalPos(tag.getCompound("boundinventory")), Direction.values()[tag.getInt("boundinventory_side")]);
            return boundInventory;
        }

        public static CompoundTag toNBT(BoundInventory boundInventory) {
            CompoundTag tag = new CompoundTag();
            tag.put("boundinventory", NBTHelpers.globalPosToNBT(boundInventory.globalPos));
            tag.putInt("boundinventory_side", boundInventory.direction.ordinal());
            return tag;
        }
    }

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
        tag.putString("dimension", globalPos.dimension().location().toString());
        tag.put("blockpos", NbtUtils.writeBlockPos(globalPos.pos()));
        return tag;
    }

    public static GlobalPos nbtToGlobalPos(CompoundTag tag) {
        ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("dimension")));
        BlockPos blockPos = NbtUtils.readBlockPos(tag.getCompound("blockpos"));
        return GlobalPos.of(levelKey, blockPos);
    }

    public static CompoundTag globalVec3ToNBT(GlobalVec3 globalVec3) {
        CompoundTag tag = new CompoundTag();
        tag.putString("dimension", globalVec3.dimension.location().toString());
        tag.putDouble("vec3x", globalVec3.position.x);
        tag.putDouble("vec3y", globalVec3.position.y);
        tag.putDouble("vec3z", globalVec3.position.z);
        return tag;
    }

    public static CompoundTag globalVec3ToNBT(ResourceKey<Level> level, Vec3 position) {
        CompoundTag tag = new CompoundTag();
        tag.putString("dimension", level.location().toString());
        tag.putDouble("vec3x", position.x);
        tag.putDouble("vec3y", position.y);
        tag.putDouble("vec3z", position.z);
        return tag;
    }

    public static GlobalVec3 nbtToGlobalVec3(CompoundTag tag) {
        ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("dimension")));
        double x = tag.getDouble("vec3x");
        double y = tag.getDouble("vec3y");
        double z = tag.getDouble("vec3z");
        return new GlobalVec3(levelKey, new Vec3(x, y, z));
    }
}
