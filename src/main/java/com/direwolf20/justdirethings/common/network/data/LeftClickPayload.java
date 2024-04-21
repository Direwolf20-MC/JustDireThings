package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record LeftClickPayload(
        int type, //0 for empty, 1 for block
        boolean mainHand,
        BlockPos blockPos,
        int direction
) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(JustDireThings.MODID, "left_click_packet");

    public LeftClickPayload(final FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readBoolean(), buffer.readBlockPos(), buffer.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(type);
        buffer.writeBoolean(mainHand);
        buffer.writeBlockPos(blockPos);
        buffer.writeInt(direction);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
