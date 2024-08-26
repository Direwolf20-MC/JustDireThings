package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ParadoxRenderPayload(
        boolean renderParadox,
        int targetType
) implements CustomPacketPayload {
    public static final Type<ParadoxRenderPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "paradox_render"));

    @Override
    public Type<ParadoxRenderPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, ParadoxRenderPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, ParadoxRenderPayload::renderParadox,
            ByteBufCodecs.INT, ParadoxRenderPayload::targetType,
            ParadoxRenderPayload::new
    );
}
