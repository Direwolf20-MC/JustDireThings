package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SwapperPayload(
        boolean swapBlocks,
        int swap_entity_type
) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(JustDireThings.MODID, "swapper_packet");

    public SwapperPayload(final FriendlyByteBuf buffer) {
        this(buffer.readBoolean(), buffer.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(swapBlocks);
        buffer.writeInt(swap_entity_type);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
