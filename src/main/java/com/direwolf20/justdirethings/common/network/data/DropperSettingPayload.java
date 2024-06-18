package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record DropperSettingPayload(
        int dropCount
) implements CustomPacketPayload {
    public static final Type<DropperSettingPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "dropper_setting_packet"));

    @Override
    public Type<DropperSettingPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, DropperSettingPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, DropperSettingPayload::dropCount,
            DropperSettingPayload::new
    );
}
