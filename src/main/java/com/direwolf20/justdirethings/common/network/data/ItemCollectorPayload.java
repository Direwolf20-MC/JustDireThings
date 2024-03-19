package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ItemCollectorPayload(
        int xRadius, int yRadius, int zRadius,
        int xOffset, int yOffset, int zOffset,
        boolean allowlist, boolean compareNBT, boolean renderArea,
        int redstoneMode
) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(JustDireThings.MODID, "item_collector_packet");

    public ItemCollectorPayload(final FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(xRadius);
        buffer.writeInt(yRadius);
        buffer.writeInt(zRadius);
        buffer.writeInt(xOffset);
        buffer.writeInt(yOffset);
        buffer.writeInt(zOffset);
        buffer.writeBoolean(allowlist);
        buffer.writeBoolean(compareNBT);
        buffer.writeBoolean(renderArea);
        buffer.writeInt(redstoneMode);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
