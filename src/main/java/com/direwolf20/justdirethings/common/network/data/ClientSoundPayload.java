package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ClientSoundPayload(
        ResourceLocation soundEvent,
        float pitch,
        float volume
) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(JustDireThings.MODID, "client_sound_packet");

    public ClientSoundPayload(final FriendlyByteBuf buffer) {
        this(buffer.readResourceLocation(), buffer.readFloat(), buffer.readFloat());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(soundEvent);
        buffer.writeFloat(pitch);
        buffer.writeFloat(volume);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
