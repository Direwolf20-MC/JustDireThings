package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.InventoryHolderBE;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.network.data.InventoryHolderSettingsPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class InventoryHolderSettingsPacket {
    public static final InventoryHolderSettingsPacket INSTANCE = new InventoryHolderSettingsPacket();

    public static InventoryHolderSettingsPacket get() {
        return INSTANCE;
    }

    public void handle(final InventoryHolderSettingsPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof BaseMachineContainer baseMachineContainer && baseMachineContainer.baseMachineBE instanceof InventoryHolderBE inventoryHolderBE) {
                inventoryHolderBE.saveSettings(payload.compareNBT(), payload.filtersOnly(), payload.compareCounts(), payload.automatedFiltersOnly(), payload.automatedCompareCounts());
            }
        });
    }
}
