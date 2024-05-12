package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PlayerAccessorPayload(
        int direction,
        int accessType
) implements CustomPacketPayload {
    public static final Type<PlayerAccessorPayload> TYPE = new Type<>(new ResourceLocation(JustDireThings.MODID, "player_accessor_packet"));

    @Override
    public Type<PlayerAccessorPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, PlayerAccessorPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PlayerAccessorPayload::direction,
            ByteBufCodecs.INT, PlayerAccessorPayload::accessType,
            PlayerAccessorPayload::new
    );
}
