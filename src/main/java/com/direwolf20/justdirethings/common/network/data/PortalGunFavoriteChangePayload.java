package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.util.NBTHelpers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public record PortalGunFavoriteChangePayload(
        int favorite,
        boolean add,
        String name,
        boolean editing,
        Vec3 coordinates
) implements CustomPacketPayload {
    public static final Type<PortalGunFavoriteChangePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "portal_gun_favorite_change"));

    @Override
    public Type<PortalGunFavoriteChangePayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, PortalGunFavoriteChangePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PortalGunFavoriteChangePayload::favorite,
            ByteBufCodecs.BOOL, PortalGunFavoriteChangePayload::add,
            ByteBufCodecs.STRING_UTF8, PortalGunFavoriteChangePayload::name,
            ByteBufCodecs.BOOL, PortalGunFavoriteChangePayload::editing,
            NBTHelpers.VEC3_STREAM_CODEC, PortalGunFavoriteChangePayload::coordinates,
            PortalGunFavoriteChangePayload::new
    );
}
