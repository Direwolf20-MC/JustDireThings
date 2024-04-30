package com.direwolf20.justdirethings.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NBTHelpers {
    public static StreamCodec<ByteBuf, Vec3> VEC3_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            Vec3::x,
            ByteBufCodecs.DOUBLE,
            Vec3::y,
            ByteBufCodecs.DOUBLE,
            Vec3::z,
            Vec3::new
    );

    public record GlobalVec3(ResourceKey<Level> dimension, Vec3 position) {
        public String toVec3ShortString() {
            return String.format("%.2f, %.2f, %.2f", position.x(), position.y(), position.z());
        }

        public static final Codec<GlobalVec3> CODEC = RecordCodecBuilder.create(
                cooldownInstance -> cooldownInstance.group(
                                Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(GlobalVec3::dimension),
                                Vec3.CODEC.fieldOf("direction").forGetter(GlobalVec3::position)
                        )
                        .apply(cooldownInstance, GlobalVec3::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, GlobalVec3> STREAM_CODEC = StreamCodec.composite(
                ResourceKey.streamCodec(Registries.DIMENSION),
                GlobalVec3::dimension,
                VEC3_STREAM_CODEC,
                GlobalVec3::position,
                GlobalVec3::new
        );
    }

    public record BoundInventory(GlobalPos globalPos, Direction direction) {
        public static final Codec<BoundInventory> CODEC = RecordCodecBuilder.create(
                cooldownInstance -> cooldownInstance.group(
                                GlobalPos.CODEC.fieldOf("globalpos").forGetter(BoundInventory::globalPos),
                                Direction.CODEC.fieldOf("direction").forGetter(BoundInventory::direction)
                        )
                        .apply(cooldownInstance, BoundInventory::new)
        );
        public static final StreamCodec<FriendlyByteBuf, BoundInventory> STREAM_CODEC = StreamCodec.composite(
                GlobalPos.STREAM_CODEC,
                BoundInventory::globalPos,
                Direction.STREAM_CODEC,
                BoundInventory::direction,
                BoundInventory::new
        );

        public static BoundInventory fromNBT(CompoundTag tag) {
            BoundInventory boundInventory = null;
            if (tag.contains("boundinventory") && tag.contains("boundinventory_side")) {
                GlobalPos globalPos = NBTHelpers.nbtToGlobalPos(tag.getCompound("boundinventory"));
                if (globalPos == null) return null;
                boundInventory = new BoundInventory(globalPos, Direction.values()[tag.getInt("boundinventory_side")]);
            }
            return boundInventory;
        }

        public static CompoundTag toNBT(BoundInventory boundInventory) {
            CompoundTag tag = new CompoundTag();
            tag.put("boundinventory", NBTHelpers.globalPosToNBT(boundInventory.globalPos));
            tag.putInt("boundinventory_side", boundInventory.direction.ordinal());
            return tag;
        }
    }

    public static CompoundTag globalPosToNBT(GlobalPos globalPos) {
        CompoundTag tag = new CompoundTag();
        tag.putString("dimension", globalPos.dimension().location().toString());
        tag.put("blockpos", NbtUtils.writeBlockPos(globalPos.pos()));
        return tag;
    }

    public static GlobalPos nbtToGlobalPos(CompoundTag tag) {
        ResourceKey<Level> levelKey;
        if (tag.contains("dimension"))
            levelKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("dimension")));
        else
            return null;
        BlockPos blockPos = NbtUtils.readBlockPos(tag, "blockpos").orElse(BlockPos.ZERO);
        return blockPos.equals(BlockPos.ZERO) ? null : GlobalPos.of(levelKey, blockPos);
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
        if (!tag.contains("dimension")) return null;
        ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("dimension")));
        double x = tag.getDouble("vec3x");
        double y = tag.getDouble("vec3y");
        double z = tag.getDouble("vec3z");
        return new GlobalVec3(levelKey, new Vec3(x, y, z));
    }
}
