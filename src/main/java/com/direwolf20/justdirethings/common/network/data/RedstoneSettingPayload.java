package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record RedstoneSettingPayload(
        int redstoneMode
) implements CustomPacketPayload {
    public static final Type<RedstoneSettingPayload> TYPE = new Type<>(new ResourceLocation(JustDireThings.MODID, "redstone_setting_packet"));

    @Override
    public Type<RedstoneSettingPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, RedstoneSettingPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, RedstoneSettingPayload::redstoneMode,
            RedstoneSettingPayload::new
    );
}
