package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.items.interfaces.LeftClickableTool;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableItem;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.common.network.data.LeftClickPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

public class LeftClickPacket {
    public static final LeftClickPacket INSTANCE = new LeftClickPacket();

    public static LeftClickPacket get() {
        return INSTANCE;
    }

    public void handle(final LeftClickPayload payload, final PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            Optional<Player> senderOptional = context.player();
            if (senderOptional.isEmpty())
                return;
            Player player = senderOptional.get();


            ItemStack toggleableItem = ToggleableItem.getToggleableItem(player);
            if (toggleableItem.getItem() instanceof LeftClickableTool && toggleableItem.getItem() instanceof ToggleableTool toggleableTool) {
                if (payload.type() == 0) { //Air
                    toggleableTool.useAbility(player.level(), player, payload.mainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, false);
                } else if (payload.type() == 1) { //Block
                    //Todo Attempt useOn first
                    toggleableTool.useAbility(player.level(), player, payload.mainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, false);
                }
            }

        });
    }
}
