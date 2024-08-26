package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.ParadoxMachineBE;
import com.direwolf20.justdirethings.common.network.data.ParadoxSyncPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ParadoxSyncPacket {
    public static final ParadoxSyncPacket INSTANCE = new ParadoxSyncPacket();

    public static ParadoxSyncPacket get() {
        return INSTANCE;
    }

    public void handle(final ParadoxSyncPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level == null) return;
            BlockEntity blockEntity = level.getBlockEntity(payload.blockPos());
            if (blockEntity instanceof ParadoxMachineBE paradoxMachineBE) {
                paradoxMachineBE.receiveRunTime(payload.runtime());
            }
        });
    }
}
