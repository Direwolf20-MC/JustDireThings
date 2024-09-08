package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.InventoryHolderBE;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.network.data.InventoryHolderSaveSlotPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class InventoryHolderSaveSlotPacket {
    public static final InventoryHolderSaveSlotPacket INSTANCE = new InventoryHolderSaveSlotPacket();

    public static InventoryHolderSaveSlotPacket get() {
        return INSTANCE;
    }

    public void handle(final InventoryHolderSaveSlotPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof BaseMachineContainer baseMachineContainer && baseMachineContainer.baseMachineBE instanceof InventoryHolderBE inventoryHolderBE) {
                inventoryHolderBE.addSavedItem(payload.slot());
            }
        });
    }
}
