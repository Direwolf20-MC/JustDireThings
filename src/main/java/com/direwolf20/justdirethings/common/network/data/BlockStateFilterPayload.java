package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record BlockStateFilterPayload(
        int slot,
        CompoundTag compoundTag
) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(JustDireThings.MODID, "block_state_filter_packet");

    public BlockStateFilterPayload(final FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readNbt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(slot);
        buffer.writeNbt(compoundTag);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
