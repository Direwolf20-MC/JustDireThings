package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record GhostSlotPayload(
        int slotNumber,
        ItemStack stack,
        int count,
        int mbAmt
) implements CustomPacketPayload {
    public static final Type<GhostSlotPayload> TYPE = new Type<>(new ResourceLocation(JustDireThings.MODID, "ghost_slot"));

    @Override
    public Type<GhostSlotPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, GhostSlotPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, GhostSlotPayload::slotNumber,
            ItemStack.OPTIONAL_STREAM_CODEC, GhostSlotPayload::stack,
            ByteBufCodecs.INT, GhostSlotPayload::count,
            ByteBufCodecs.INT, GhostSlotPayload::mbAmt,
            GhostSlotPayload::new
    );
}
