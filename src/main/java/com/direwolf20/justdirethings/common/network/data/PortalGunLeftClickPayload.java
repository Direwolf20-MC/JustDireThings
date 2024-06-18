package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PortalGunLeftClickPayload(

) implements CustomPacketPayload {
    public static final PortalGunLeftClickPayload INSTANCE = new PortalGunLeftClickPayload();
    public static final Type<PortalGunLeftClickPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "portal_gun_left_click"));

    @Override
    public Type<PortalGunLeftClickPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, PortalGunLeftClickPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);
}

