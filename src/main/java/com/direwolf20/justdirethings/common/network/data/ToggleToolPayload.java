package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ToggleToolPayload() implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(JustDireThings.MODID, "toggle_tool");

    public ToggleToolPayload(final FriendlyByteBuf buffer) {
        this();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {

    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}

