package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.ClickerT1BE;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.network.data.ClickerPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

public class ClickerPacket {
    public static final ClickerPacket INSTANCE = new ClickerPacket();

    public static ClickerPacket get() {
        return INSTANCE;
    }

    public void handle(final ClickerPayload payload, final PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            Optional<Player> senderOptional = context.player();
            if (senderOptional.isEmpty())
                return;
            Player sender = senderOptional.get();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof BaseMachineContainer baseMachineContainer && baseMachineContainer.baseMachineBE instanceof ClickerT1BE clicker) {
                clicker.setClickerSettings(payload.clickType(), payload.clickTarget(), payload.sneaking());
            }
        });
    }
}
