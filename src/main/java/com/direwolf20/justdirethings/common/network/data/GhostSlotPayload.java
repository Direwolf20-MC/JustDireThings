package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record GhostSlotPayload(
        int slotNumber,
        ItemStack stack,
        int count,
        int mbAmt
) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(JustDireThings.MODID, "ghost_slot");

    public GhostSlotPayload(final FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readItem(), buffer.readInt(), buffer.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(slotNumber());
        buffer.writeItem(stack());
        buffer.writeInt(count());
        buffer.writeInt(mbAmt());
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
