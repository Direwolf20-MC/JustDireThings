package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record FilterSettingPayload(
        boolean allowList,
        boolean compareNBT,
        int blockItemFilter
) implements CustomPacketPayload {
    public static final Type<FilterSettingPayload> TYPE = new Type<>(new ResourceLocation(JustDireThings.MODID, "filter_setting_packet"));

    @Override
    public Type<FilterSettingPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, FilterSettingPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, FilterSettingPayload::allowList,
            ByteBufCodecs.BOOL, FilterSettingPayload::compareNBT,
            ByteBufCodecs.INT, FilterSettingPayload::blockItemFilter,
            FilterSettingPayload::new
    );
}
