package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.network.data.TickSpeedPayload;
import com.direwolf20.justdirethings.setup.Config;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

public class TickSpeedPacket {
    public static final TickSpeedPacket INSTANCE = new TickSpeedPacket();

    public static TickSpeedPacket get() {
        return INSTANCE;
    }

    public void handle(final TickSpeedPayload payload, final PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            Optional<Player> senderOptional = context.player();
            if (senderOptional.isEmpty())
                return;
            Player sender = senderOptional.get();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof BaseMachineContainer baseMachineContainer) {
                baseMachineContainer.baseMachineBE.setTickSpeed(Math.min(Math.max(payload.tickSpeed(), Config.MINIMUM_MACHINE_TICK_SPEED.get()), 1200));
            }
        });
    }
}
