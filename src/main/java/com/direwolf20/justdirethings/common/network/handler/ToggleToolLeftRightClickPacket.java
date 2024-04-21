package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.LeftClickableTool;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableItem;
import com.direwolf20.justdirethings.common.network.data.ToggleToolLeftRightClickPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Locale;
import java.util.Optional;

public class ToggleToolLeftRightClickPacket {
    public static final ToggleToolLeftRightClickPacket INSTANCE = new ToggleToolLeftRightClickPacket();

    public static ToggleToolLeftRightClickPacket get() {
        return INSTANCE;
    }

    public void handle(final ToggleToolLeftRightClickPayload payload, final PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            Optional<Player> senderOptional = context.player();
            if (senderOptional.isEmpty())
                return;
            Player player = senderOptional.get();


            ItemStack toggleableItem = ToggleableItem.getToggleableItem(player);
            if (toggleableItem.getItem() instanceof LeftClickableTool) {
                if (payload.button() == 1) //Left Click
                    LeftClickableTool.addToLeftClickList(toggleableItem, Ability.valueOf(payload.abilityName().toUpperCase(Locale.ROOT)));
                else //Anything else, which should always be zero...
                    LeftClickableTool.removeFromLeftClickList(toggleableItem, Ability.valueOf(payload.abilityName().toUpperCase(Locale.ROOT)));
            }

        });
    }
}
