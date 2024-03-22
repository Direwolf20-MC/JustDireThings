package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.containers.ItemCollectorContainer;
import com.direwolf20.justdirethings.common.network.data.ItemCollectorPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

public class ItemCollectorPacket {
    public static final ItemCollectorPacket INSTANCE = new ItemCollectorPacket();

    public static ItemCollectorPacket get() {
        return INSTANCE;
    }

    public void handle(final ItemCollectorPayload payload, final PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            Optional<Player> senderOptional = context.player();
            if (senderOptional.isEmpty())
                return;
            Player sender = senderOptional.get();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof ItemCollectorContainer itemCollectorContainer) {
                itemCollectorContainer.itemCollectorBE.setSettings(payload.xRadius(), payload.yRadius(), payload.zRadius(), payload.xOffset(), payload.yOffset(), payload.zOffset(), payload.allowlist(), payload.compareNBT(), payload.renderArea(), payload.redstoneMode());
            }
        });
    }
}
