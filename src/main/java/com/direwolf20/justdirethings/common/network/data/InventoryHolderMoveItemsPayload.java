package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record InventoryHolderMoveItemsPayload(
        int moveType
) implements CustomPacketPayload {
    public static final Type<InventoryHolderMoveItemsPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "inventory_holder_move_items"));

    @Override
    public Type<InventoryHolderMoveItemsPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, InventoryHolderMoveItemsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, InventoryHolderMoveItemsPayload::moveType,
            InventoryHolderMoveItemsPayload::new
    );
}
