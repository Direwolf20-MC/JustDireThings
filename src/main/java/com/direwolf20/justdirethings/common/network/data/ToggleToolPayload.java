package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ToggleToolPayload(
        String settingName
) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(JustDireThings.MODID, "toggle_tool_setting");

    public ToggleToolPayload(final FriendlyByteBuf buffer) {
        this(buffer.readUtf());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(settingName);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}

