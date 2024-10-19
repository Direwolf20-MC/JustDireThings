package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ToolSettingsGUIPayload(

) implements CustomPacketPayload {
    public static final ToolSettingsGUIPayload INSTANCE = new ToolSettingsGUIPayload();
    public static final Type<ToolSettingsGUIPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "tool_settings_gui"));

    @Override
    public Type<ToolSettingsGUIPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, ToolSettingsGUIPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);
}

