package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.common.network.data.ToggleToolSlotPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ToggleToolSlotPacket {
    public static final ToggleToolSlotPacket INSTANCE = new ToggleToolSlotPacket();

    public static ToggleToolSlotPacket get() {
        return INSTANCE;
    }

    public void handle(final ToggleToolSlotPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();


            ItemStack stack = sender.getInventory().getItem(payload.slot());
            if (stack.getItem() instanceof ToggleableTool) {
                if (payload.typeTool() == 0) //Toggle
                    ToggleableTool.toggleSetting(stack, payload.settingName());
                else if (payload.typeTool() == 1) //Cycle
                    ToggleableTool.cycleSetting(stack, payload.settingName());
                else if (payload.typeTool() == 2) //Slider
                    ToggleableTool.setToolValue(stack, payload.settingName(), payload.value());
                else if (payload.typeTool() == 3) //Render
                    ToggleableTool.setCustomSetting(stack, payload.settingName(), payload.value());
            }
        });
    }
}
