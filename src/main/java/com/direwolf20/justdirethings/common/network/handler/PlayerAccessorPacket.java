package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.PlayerAccessorBE;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.network.data.PlayerAccessorPayload;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PlayerAccessorPacket {
    public static final PlayerAccessorPacket INSTANCE = new PlayerAccessorPacket();

    public static PlayerAccessorPacket get() {
        return INSTANCE;
    }

    public void handle(final PlayerAccessorPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof BaseMachineContainer baseMachineContainer && baseMachineContainer.baseMachineBE instanceof PlayerAccessorBE playerAccessorBE) {
                playerAccessorBE.updateSidedInventory(Direction.values()[payload.direction()], payload.accessType());
            }
        });
    }
}
