package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ToggleToolLeftRightClickPayload(
        int slot,
        String abilityName,
        int button,
        int keyCode,
        boolean isMouse
) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(JustDireThings.MODID, "tool_left_right_click_settings_packet");

    public ToggleToolLeftRightClickPayload(final FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readUtf(), buffer.readInt(), buffer.readInt(), buffer.readBoolean());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(slot);
        buffer.writeUtf(abilityName);
        buffer.writeInt(button);
        buffer.writeInt(keyCode);
        buffer.writeBoolean(isMouse);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
