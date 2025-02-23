package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record BreakerPayload(
    boolean sneaking
) implements CustomPacketPayload {
    public static final Type<BreakerPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "breaker_packet"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, BreakerPayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.BOOL, BreakerPayload::sneaking,
        BreakerPayload::new
    );
}
