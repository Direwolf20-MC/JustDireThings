package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ClientSoundPayload(
        ResourceLocation soundEvent,
        float pitch,
        float volume
) implements CustomPacketPayload {
    public static final Type<ClientSoundPayload> TYPE = new Type<>(new ResourceLocation(JustDireThings.MODID, "client_sound_packet"));

    @Override
    public Type<ClientSoundPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, ClientSoundPayload> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, ClientSoundPayload::soundEvent,
            ByteBufCodecs.FLOAT, ClientSoundPayload::pitch,
            ByteBufCodecs.FLOAT, ClientSoundPayload::volume,
            ClientSoundPayload::new
    );
}
