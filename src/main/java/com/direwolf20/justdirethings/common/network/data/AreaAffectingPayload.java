package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record AreaAffectingPayload(
        double xRadius, double yRadius, double zRadius,
        int xOffset, int yOffset, int zOffset,
        boolean renderArea
) implements CustomPacketPayload {
    public static final Type<AreaAffectingPayload> TYPE = new Type<>(new ResourceLocation(JustDireThings.MODID, "area_affecting_packet"));

    @Override
    public Type<AreaAffectingPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, AreaAffectingPayload> STREAM_CODEC = NeoForgeStreamCodecs.composite(
            ByteBufCodecs.DOUBLE, AreaAffectingPayload::xRadius,
            ByteBufCodecs.DOUBLE, AreaAffectingPayload::yRadius,
            ByteBufCodecs.DOUBLE, AreaAffectingPayload::zRadius,
            ByteBufCodecs.INT, AreaAffectingPayload::xOffset,
            ByteBufCodecs.INT, AreaAffectingPayload::yOffset,
            ByteBufCodecs.INT, AreaAffectingPayload::zOffset,
            ByteBufCodecs.BOOL, AreaAffectingPayload::renderArea,
            AreaAffectingPayload::new
    );
}
