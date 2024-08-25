package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ParadoxMachineSnapshotPayload(

) implements CustomPacketPayload {
    public static final ParadoxMachineSnapshotPayload INSTANCE = new ParadoxMachineSnapshotPayload();
    public static final Type<ParadoxMachineSnapshotPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "paradox_machine_snapshot"));

    @Override
    public Type<ParadoxMachineSnapshotPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, ParadoxMachineSnapshotPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);
}

