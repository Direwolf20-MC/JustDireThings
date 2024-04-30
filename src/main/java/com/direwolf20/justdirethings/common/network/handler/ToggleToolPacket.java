package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.items.interfaces.ToggleableItem;
import com.direwolf20.justdirethings.common.network.data.ToggleToolPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ToggleToolPacket {
    public static final ToggleToolPacket INSTANCE = new ToggleToolPacket();

    public static ToggleToolPacket get() {
        return INSTANCE;
    }

    public void handle(final ToggleToolPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();


            ItemStack toggleableItem = ToggleableItem.getToggleableItem(sender);
            if (toggleableItem.getItem() instanceof ToggleableItem toggleableTool) {
                toggleableTool.toggleEnabled(toggleableItem);
            }

        });
    }
}
