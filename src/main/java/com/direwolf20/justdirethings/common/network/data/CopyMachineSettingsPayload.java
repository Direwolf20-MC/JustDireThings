package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CopyMachineSettingsPayload(
        boolean area, boolean offset,
        boolean filter, boolean redstone
) implements CustomPacketPayload {
    public static final Type<CopyMachineSettingsPayload> TYPE = new Type<>(new ResourceLocation(JustDireThings.MODID, "copy_machine_settings_packet"));

    @Override
    public Type<CopyMachineSettingsPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, CopyMachineSettingsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, CopyMachineSettingsPayload::area,
            ByteBufCodecs.BOOL, CopyMachineSettingsPayload::offset,
            ByteBufCodecs.BOOL, CopyMachineSettingsPayload::filter,
            ByteBufCodecs.BOOL, CopyMachineSettingsPayload::redstone,
            CopyMachineSettingsPayload::new
    );
}
