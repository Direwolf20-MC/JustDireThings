package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.network.data.DirectionSettingPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class DirectionSettingPacket {
    public static final DirectionSettingPacket INSTANCE = new DirectionSettingPacket();

    public static DirectionSettingPacket get() {
        return INSTANCE;
    }

    public void handle(final DirectionSettingPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof BaseMachineContainer baseMachineContainer) {
                baseMachineContainer.baseMachineBE.setDirection(payload.direction());
            }
        });
    }
}
