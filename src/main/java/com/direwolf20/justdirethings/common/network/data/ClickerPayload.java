package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ClickerPayload(
        int clickType,
        int clickTarget,
        boolean sneaking,
        boolean showFakePlayer,
        int maxHoldTicks
) implements CustomPacketPayload {
    public static final Type<ClickerPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "clicker_packet"));

    @Override
    public Type<ClickerPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, ClickerPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClickerPayload::clickType,
            ByteBufCodecs.INT, ClickerPayload::clickTarget,
            ByteBufCodecs.BOOL, ClickerPayload::sneaking,
            ByteBufCodecs.BOOL, ClickerPayload::showFakePlayer,
            ByteBufCodecs.INT, ClickerPayload::maxHoldTicks,
            ClickerPayload::new
    );
}
