package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record TickSpeedPayload(
        int tickSpeed
) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(JustDireThings.MODID, "tick_speed_packet");

    public TickSpeedPayload(final FriendlyByteBuf buffer) {
        this(buffer.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(tickSpeed);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
