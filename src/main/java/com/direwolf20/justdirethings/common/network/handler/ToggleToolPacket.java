package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.items.interfaces.ToggleableItem;
import com.direwolf20.justdirethings.common.network.data.ToggleToolPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

public class ToggleToolPacket {
    public static final ToggleToolPacket INSTANCE = new ToggleToolPacket();

    public static ToggleToolPacket get() {
        return INSTANCE;
    }

    public void handle(final ToggleToolPayload payload, final PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            Optional<Player> senderOptional = context.player();
            if (senderOptional.isEmpty())
                return;
            Player player = senderOptional.get();


            ItemStack toggleableItem = ToggleableItem.getToggleableItem(player);
            if (toggleableItem.getItem() instanceof ToggleableItem toggleableTool) {
                toggleableTool.toggleEnabled(toggleableItem);
            }

        });
    }
}
