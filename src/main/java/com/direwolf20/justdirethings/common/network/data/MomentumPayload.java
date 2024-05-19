package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.util.NBTHelpers;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public record MomentumPayload(
        Vec3 momentum,
        UUID portalUUID
) implements CustomPacketPayload {
    public static final Type<MomentumPayload> TYPE = new Type<>(new ResourceLocation(JustDireThings.MODID, "momentum_packet"));

    @Override
    public Type<MomentumPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, MomentumPayload> STREAM_CODEC = StreamCodec.composite(
            NBTHelpers.VEC3_STREAM_CODEC, MomentumPayload::momentum,
            UUIDUtil.STREAM_CODEC, MomentumPayload::portalUUID,
            MomentumPayload::new
    );
}
