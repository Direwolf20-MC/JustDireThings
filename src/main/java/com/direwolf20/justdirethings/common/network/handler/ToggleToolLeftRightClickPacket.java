package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.LeftClickableTool;
import com.direwolf20.justdirethings.common.items.interfaces.ToolRecords;
import com.direwolf20.justdirethings.common.network.data.ToggleToolLeftRightClickPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Locale;

public class ToggleToolLeftRightClickPacket {
    public static final ToggleToolLeftRightClickPacket INSTANCE = new ToggleToolLeftRightClickPacket();

    public static ToggleToolLeftRightClickPacket get() {
        return INSTANCE;
    }

    public void handle(final ToggleToolLeftRightClickPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();


            ItemStack stack = sender.getInventory().getItem(payload.slot());
            if (stack.getItem() instanceof LeftClickableTool) {
                Ability ability = Ability.valueOf(payload.abilityName().toUpperCase(Locale.ROOT));
                LeftClickableTool.setBindingMode(stack, ability, payload.button());
                if (payload.button() == 0) //Right Click
                    LeftClickableTool.removeFromLeftClickList(stack, ability);
                else if (payload.button() == 1) //Left Click
                    LeftClickableTool.addToLeftClickList(stack, ability);
                else if (payload.button() == 2) { //Custom Keybind
                    if (payload.keyCode() == -1)
                        LeftClickableTool.removeFromCustomBindingList(stack, ability);
                    else
                        LeftClickableTool.addToCustomBindingList(stack, new ToolRecords.AbilityBinding(payload.abilityName(), payload.keyCode(), payload.isMouse(), payload.requireEquipped()));
                }
            }
        });
    }
}
