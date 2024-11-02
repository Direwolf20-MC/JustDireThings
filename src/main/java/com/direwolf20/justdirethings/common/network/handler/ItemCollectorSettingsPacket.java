package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.ItemCollectorBE;
import com.direwolf20.justdirethings.common.containers.ItemCollectorContainer;
import com.direwolf20.justdirethings.common.network.data.ItemCollectorSettingsPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ItemCollectorSettingsPacket {
    public static final ItemCollectorSettingsPacket INSTANCE = new ItemCollectorSettingsPacket();

    public static ItemCollectorSettingsPacket get() {
        return INSTANCE;
    }

    public void handle(final ItemCollectorSettingsPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof ItemCollectorContainer itemCollectorContainer && itemCollectorContainer.baseMachineBE instanceof ItemCollectorBE itemCollectorBE) {
                itemCollectorBE.setSettings(payload.respectPickupDelay(), payload.showParticles());
            }
        });
    }
}
