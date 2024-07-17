package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.containers.ToolSettingContainer;
import com.direwolf20.justdirethings.common.network.data.ToggleToolRefreshSlots;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ToggleToolRefreshSlotsPacket {
    public static final ToggleToolRefreshSlotsPacket INSTANCE = new ToggleToolRefreshSlotsPacket();

    public static ToggleToolRefreshSlotsPacket get() {
        return INSTANCE;
    }

    public void handle(final ToggleToolRefreshSlots payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();


            ItemStack stack = sender.getInventory().getItem(payload.slot());
            if (sender.containerMenu instanceof ToolSettingContainer container) {
                container.refreshSlots(stack);
            }
        });
    }
}
