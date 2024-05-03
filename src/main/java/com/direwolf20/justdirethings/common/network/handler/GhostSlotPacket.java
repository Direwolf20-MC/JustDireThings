package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.containers.slots.FilterBasicSlot;
import com.direwolf20.justdirethings.common.network.data.GhostSlotPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class GhostSlotPacket {
    public static final GhostSlotPacket INSTANCE = new GhostSlotPacket();

    public static GhostSlotPacket get() {
        return INSTANCE;
    }

    public void handle(final GhostSlotPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();

            AbstractContainerMenu container = sender.containerMenu;
            if (container == null)
                return;

            Slot slot = container.slots.get(payload.slotNumber());
            ItemStack stack = payload.stack();
            stack.setCount(payload.count());
            if (slot instanceof FilterBasicSlot)
                slot.set(stack);

            if (container instanceof BaseMachineContainer baseMachineContainer && baseMachineContainer.baseMachineBE instanceof BaseMachineBE baseMachineBE)
                baseMachineBE.markDirtyClient();
        });
    }
}
