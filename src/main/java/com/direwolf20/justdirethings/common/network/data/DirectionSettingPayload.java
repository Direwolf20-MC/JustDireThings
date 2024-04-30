package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record DirectionSettingPayload(
        int direction
) implements CustomPacketPayload {
    public static final Type<DirectionSettingPayload> TYPE = new Type<>(new ResourceLocation(JustDireThings.MODID, "direction_setting_packet"));

    @Override
    public Type<DirectionSettingPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, DirectionSettingPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, DirectionSettingPayload::direction,
            DirectionSettingPayload::new
    );
}
