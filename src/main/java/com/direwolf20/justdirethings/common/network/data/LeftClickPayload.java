package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record LeftClickPayload(
        int type, //0 for empty, 1 for block
        boolean mainHand
) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(JustDireThings.MODID, "left_click_packet");

    public LeftClickPayload(final FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readBoolean());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(type);
        buffer.writeBoolean(mainHand);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
