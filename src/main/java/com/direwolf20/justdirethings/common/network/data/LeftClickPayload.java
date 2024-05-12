package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record LeftClickPayload(
        int clickType, //0 for empty, 1 for block
        boolean mainHand,
        BlockPos blockPos,
        int direction,
        int inventorySlot,
        int keyCode, //-1 for left click
        boolean isMouse
) implements CustomPacketPayload {
    public static final Type<LeftClickPayload> TYPE = new Type<>(new ResourceLocation(JustDireThings.MODID, "left_click_packet"));

    @Override
    public Type<LeftClickPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, LeftClickPayload> STREAM_CODEC = NeoForgeStreamCodecs.composite(
            ByteBufCodecs.INT, LeftClickPayload::clickType,
            ByteBufCodecs.BOOL, LeftClickPayload::mainHand,
            BlockPos.STREAM_CODEC, LeftClickPayload::blockPos,
            ByteBufCodecs.INT, LeftClickPayload::direction,
            ByteBufCodecs.INT, LeftClickPayload::inventorySlot,
            ByteBufCodecs.INT, LeftClickPayload::keyCode,
            ByteBufCodecs.BOOL, LeftClickPayload::isMouse,
            LeftClickPayload::new
    );
}
