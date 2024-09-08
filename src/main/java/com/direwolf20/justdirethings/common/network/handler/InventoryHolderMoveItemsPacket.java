package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.containers.InventoryHolderContainer;
import com.direwolf20.justdirethings.common.network.data.InventoryHolderMoveItemsPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class InventoryHolderMoveItemsPacket {
    public static final InventoryHolderMoveItemsPacket INSTANCE = new InventoryHolderMoveItemsPacket();

    public static InventoryHolderMoveItemsPacket get() {
        return INSTANCE;
    }

    public void handle(final InventoryHolderMoveItemsPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof InventoryHolderContainer inventoryHolderContainer) {
                int moveType = payload.moveType();
                if (moveType == 0)
                    inventoryHolderContainer.sendAllItemsToMachine();
                else if (moveType == 1)
                    inventoryHolderContainer.sendAllItemsToPlayer();
                else if (moveType == 2)
                    inventoryHolderContainer.swapItems();
            }
        });
    }
}
