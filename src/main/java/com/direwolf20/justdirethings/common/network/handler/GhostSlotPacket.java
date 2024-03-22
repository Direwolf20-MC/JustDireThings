package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.containers.slots.FilterBasicSlot;
import com.direwolf20.justdirethings.common.network.data.GhostSlotPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

public class GhostSlotPacket {
    public static final GhostSlotPacket INSTANCE = new GhostSlotPacket();

    public static GhostSlotPacket get() {
        return INSTANCE;
    }

    public void handle(final GhostSlotPayload payload, final PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            Optional<Player> senderOptional = context.player();
            if (senderOptional.isEmpty())
                return;
            Player sender = senderOptional.get();


            AbstractContainerMenu container = sender.containerMenu;
            if (container == null)
                return;

            Slot slot = container.slots.get(payload.slotNumber());
            ItemStack stack = payload.stack();
            stack.setCount(payload.count());
            if (slot instanceof FilterBasicSlot)
                slot.set(stack);
        });
    }
}
