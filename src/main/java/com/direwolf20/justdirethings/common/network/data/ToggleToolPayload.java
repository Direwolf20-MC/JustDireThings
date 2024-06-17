package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ToggleToolPayload(
        String settingName
) implements CustomPacketPayload {
    public static final Type<ToggleToolPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "toggle_tool_setting"));

    @Override
    public Type<ToggleToolPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, ToggleToolPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ToggleToolPayload::settingName,
            ToggleToolPayload::new
    );
}

