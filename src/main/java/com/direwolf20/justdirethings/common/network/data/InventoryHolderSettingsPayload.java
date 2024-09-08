package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record InventoryHolderSettingsPayload(
        boolean compareNBT,
        boolean filtersOnly,
        boolean compareCounts,
        boolean automatedFiltersOnly,
        boolean automatedCompareCounts,
        boolean renderPlayer,
        int renderedSlot
) implements CustomPacketPayload {
    public static final Type<InventoryHolderSettingsPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "inventory_holder_settings"));

    @Override
    public Type<InventoryHolderSettingsPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, InventoryHolderSettingsPayload> STREAM_CODEC = NeoForgeStreamCodecs.composite(
            ByteBufCodecs.BOOL, InventoryHolderSettingsPayload::compareNBT,
            ByteBufCodecs.BOOL, InventoryHolderSettingsPayload::filtersOnly,
            ByteBufCodecs.BOOL, InventoryHolderSettingsPayload::compareCounts,
            ByteBufCodecs.BOOL, InventoryHolderSettingsPayload::automatedFiltersOnly,
            ByteBufCodecs.BOOL, InventoryHolderSettingsPayload::automatedCompareCounts,
            ByteBufCodecs.BOOL, InventoryHolderSettingsPayload::renderPlayer,
            ByteBufCodecs.INT, InventoryHolderSettingsPayload::renderedSlot,
            InventoryHolderSettingsPayload::new
    );
}
