package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.DropperT1BE;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.network.data.DropperSettingPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

public class DropperSettingPacket {
    public static final DropperSettingPacket INSTANCE = new DropperSettingPacket();

    public static DropperSettingPacket get() {
        return INSTANCE;
    }

    public void handle(final DropperSettingPayload payload, final PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            Optional<Player> senderOptional = context.player();
            if (senderOptional.isEmpty())
                return;
            Player sender = senderOptional.get();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof BaseMachineContainer baseMachineContainer && baseMachineContainer.baseMachineBE instanceof DropperT1BE dropperT1BE) {
                dropperT1BE.setDropperSettings(payload.dropCount());
            }
        });
    }
}
