package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PortalGunFavoritePayload(
        int favorite
) implements CustomPacketPayload {
    public static final Type<PortalGunFavoritePayload> TYPE = new Type<>(new ResourceLocation(JustDireThings.MODID, "portal_gun_favorite"));

    @Override
    public Type<PortalGunFavoritePayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, PortalGunFavoritePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PortalGunFavoritePayload::favorite,
            PortalGunFavoritePayload::new
    );
}
