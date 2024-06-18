package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SwapperPayload(
        boolean swapBlocks,
        int swap_entity_type
) implements CustomPacketPayload {
    public static final Type<SwapperPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "swapper_packet"));

    @Override
    public Type<SwapperPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, SwapperPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SwapperPayload::swapBlocks,
            ByteBufCodecs.INT, SwapperPayload::swap_entity_type,
            SwapperPayload::new
    );
}
