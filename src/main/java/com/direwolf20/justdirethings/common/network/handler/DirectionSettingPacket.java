package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.BlockBreakerT2BE;
import com.direwolf20.justdirethings.common.blockentities.BlockPlacerT1BE;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.network.data.DirectionSettingPayload;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

public class DirectionSettingPacket {
    public static final DirectionSettingPacket INSTANCE = new DirectionSettingPacket();

    public static DirectionSettingPacket get() {
        return INSTANCE;
    }

    public void handle(final DirectionSettingPayload payload, final PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            Optional<Player> senderOptional = context.player();
            if (senderOptional.isEmpty())
                return;
            Player sender = senderOptional.get();
            AbstractContainerMenu container = sender.containerMenu;

            //Todo - if this is used outside BlockBreakerT2 make it an interface like redstone
            if (container instanceof BaseMachineContainer baseMachineContainer && baseMachineContainer.baseMachineBE instanceof BlockBreakerT2BE blockBreakerT2BE) {
                blockBreakerT2BE.setDirection(Direction.values()[payload.direction()]);
            }
            if (container instanceof BaseMachineContainer baseMachineContainer && baseMachineContainer.baseMachineBE instanceof BlockPlacerT1BE blockPlacerT1BE) {
                blockPlacerT1BE.setPlaceDirection(payload.direction());
            }
        });
    }
}
