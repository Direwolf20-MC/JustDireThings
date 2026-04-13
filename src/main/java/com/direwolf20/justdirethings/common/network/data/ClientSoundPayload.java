package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ClientSoundPayload(
        Identifier soundEvent,
        float pitch,
        float volume
) implements CustomPacketPayload {
    public static final Type<ClientSoundPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "client_sound_packet"));

    @Override
    public Type<ClientSoundPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, ClientSoundPayload> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, ClientSoundPayload::soundEvent,
            ByteBufCodecs.FLOAT, ClientSoundPayload::pitch,
            ByteBufCodecs.FLOAT, ClientSoundPayload::volume,
            ClientSoundPayload::new
    );
}
