package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record EnergyTransmitterSettingPayload(
        boolean showParticles
) implements CustomPacketPayload {
    public static final Type<EnergyTransmitterSettingPayload> TYPE = new Type<>(new ResourceLocation(JustDireThings.MODID, "energy_transmitter_packet"));

    @Override
    public Type<EnergyTransmitterSettingPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, EnergyTransmitterSettingPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, EnergyTransmitterSettingPayload::showParticles,
            EnergyTransmitterSettingPayload::new
    );
}
