package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool;
import com.direwolf20.justdirethings.common.network.data.ToggleToolSlotPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

public class ToggleToolSlotPacket {
    public static final ToggleToolSlotPacket INSTANCE = new ToggleToolSlotPacket();

    public static ToggleToolSlotPacket get() {
        return INSTANCE;
    }

    public void handle(final ToggleToolSlotPayload payload, final PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            Optional<Player> senderOptional = context.player();
            if (senderOptional.isEmpty())
                return;
            Player player = senderOptional.get();


            ItemStack stack = player.getInventory().getItem(payload.slot());
            if (stack.getItem() instanceof ToggleableTool)
                ToggleableTool.toggleSetting(stack, payload.settingName());
        });
    }
}
