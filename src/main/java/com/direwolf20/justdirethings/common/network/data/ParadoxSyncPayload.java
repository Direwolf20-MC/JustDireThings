package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ParadoxSyncPayload(
        BlockPos blockPos,
        int runtime
) implements CustomPacketPayload {
    public static final Type<ParadoxSyncPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "paradox_sync"));

    @Override
    public Type<ParadoxSyncPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, ParadoxSyncPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ParadoxSyncPayload::blockPos,
            ByteBufCodecs.INT, ParadoxSyncPayload::runtime,
            ParadoxSyncPayload::new
    );
}
