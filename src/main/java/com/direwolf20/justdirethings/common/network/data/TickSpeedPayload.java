package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record TickSpeedPayload(
        int tickSpeed
) implements CustomPacketPayload {
    public static final Type<TickSpeedPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "tick_speed_packet"));

    @Override
    public Type<TickSpeedPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, TickSpeedPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, TickSpeedPayload::tickSpeed,
            TickSpeedPayload::new
    );
}
