package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ToggleToolSlotPayload(
        String settingName,
        int slot,
        int typeTool,
        int value
) implements CustomPacketPayload {
    public static final Type<ToggleToolSlotPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "toggle_tool_slot_setting"));

    @Override
    public Type<ToggleToolSlotPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, ToggleToolSlotPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ToggleToolSlotPayload::settingName,
            ByteBufCodecs.INT, ToggleToolSlotPayload::slot,
            ByteBufCodecs.INT, ToggleToolSlotPayload::typeTool,
            ByteBufCodecs.INT, ToggleToolSlotPayload::value,
            ToggleToolSlotPayload::new
    );
}

