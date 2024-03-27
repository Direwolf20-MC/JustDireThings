package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record FilterSettingPayload(
        boolean allowList,
        boolean compareNBT,
        int blockItemFilter
) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(JustDireThings.MODID, "filter_setting_packet");

    public FilterSettingPayload(final FriendlyByteBuf buffer) {
        this(buffer.readBoolean(), buffer.readBoolean(), buffer.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(allowList);
        buffer.writeBoolean(compareNBT);
        buffer.writeInt(blockItemFilter);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
