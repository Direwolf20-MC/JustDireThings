package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record BlockStateFilterPayload(
        int slot,
        CompoundTag compoundTag
) implements CustomPacketPayload {
    public static final Type<BlockStateFilterPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "block_state_filter_packet"));

    @Override
    public Type<BlockStateFilterPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, BlockStateFilterPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, BlockStateFilterPayload::slot,
            ByteBufCodecs.COMPOUND_TAG, BlockStateFilterPayload::compoundTag,
            BlockStateFilterPayload::new
    );
}
