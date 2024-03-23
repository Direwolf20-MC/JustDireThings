package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.basebe.AreaAffectingBE;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.network.data.AreaAffectingPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

public class AreaAffectingPacket {
    public static final AreaAffectingPacket INSTANCE = new AreaAffectingPacket();

    public static AreaAffectingPacket get() {
        return INSTANCE;
    }

    public void handle(final AreaAffectingPayload payload, final PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            Optional<Player> senderOptional = context.player();
            if (senderOptional.isEmpty())
                return;
            Player sender = senderOptional.get();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof BaseMachineContainer baseMachineContainer && baseMachineContainer.baseMachineBE instanceof AreaAffectingBE areaAffectingBE) {
                areaAffectingBE.setAreaSettings(payload.xRadius(), payload.yRadius(), payload.zRadius(), payload.xOffset(), payload.yOffset(), payload.zOffset(), payload.renderArea());
            }
        });
    }
}
