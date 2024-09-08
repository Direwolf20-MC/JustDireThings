package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record InventoryHolderSettingsPayload(
        boolean compareNBT,
        boolean filtersOnly,
        boolean compareCounts
) implements CustomPacketPayload {
    public static final Type<InventoryHolderSettingsPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "inventory_holder_settings"));

    @Override
    public Type<InventoryHolderSettingsPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, InventoryHolderSettingsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, InventoryHolderSettingsPayload::compareNBT,
            ByteBufCodecs.BOOL, InventoryHolderSettingsPayload::filtersOnly,
            ByteBufCodecs.BOOL, InventoryHolderSettingsPayload::compareCounts,
            InventoryHolderSettingsPayload::new
    );
}
