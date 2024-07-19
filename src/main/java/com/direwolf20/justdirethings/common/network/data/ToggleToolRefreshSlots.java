package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ToggleToolRefreshSlots(
        int slot
) implements CustomPacketPayload {
    public static final Type<ToggleToolRefreshSlots> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "toggle_tool_refresh_slots"));

    @Override
    public Type<ToggleToolRefreshSlots> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, ToggleToolRefreshSlots> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ToggleToolRefreshSlots::slot,
            ToggleToolRefreshSlots::new
    );
}

