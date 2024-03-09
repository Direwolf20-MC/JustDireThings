package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ToggleToolSlotPayload(
        String settingName,
        int slot,
        int type
) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(JustDireThings.MODID, "toggle_tool_slot_setting");

    public ToggleToolSlotPayload(final FriendlyByteBuf buffer) {
        this(buffer.readUtf(), buffer.readInt(), buffer.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(settingName);
        buffer.writeInt(slot);
        buffer.writeInt(type);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}

