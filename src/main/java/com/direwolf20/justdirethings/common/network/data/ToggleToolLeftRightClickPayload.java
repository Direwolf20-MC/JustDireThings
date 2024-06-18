package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ToggleToolLeftRightClickPayload(
        int slot,
        String abilityName,
        int button,
        int keyCode,
        boolean isMouse
) implements CustomPacketPayload {
    public static final Type<ToggleToolLeftRightClickPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "tool_left_right_click_settings_packet"));

    @Override
    public Type<ToggleToolLeftRightClickPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, ToggleToolLeftRightClickPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ToggleToolLeftRightClickPayload::slot,
            ByteBufCodecs.STRING_UTF8, ToggleToolLeftRightClickPayload::abilityName,
            ByteBufCodecs.INT, ToggleToolLeftRightClickPayload::button,
            ByteBufCodecs.INT, ToggleToolLeftRightClickPayload::keyCode,
            ByteBufCodecs.BOOL, ToggleToolLeftRightClickPayload::isMouse,
            ToggleToolLeftRightClickPayload::new
    );
}
