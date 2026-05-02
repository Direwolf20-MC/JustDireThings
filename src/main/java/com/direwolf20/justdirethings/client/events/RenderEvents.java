package com.direwolf20.justdirethings.client.events;

import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;

public class RenderEvents {

    @SubscribeEvent
    public static void onRenderBlockScreenEffect(RenderBlockScreenEffectEvent event) {
        if (event.getOverlayType() != RenderBlockScreenEffectEvent.OverlayType.BLOCK) return;
        Player player = event.getPlayer();
        if (player.getAttributeValue(JDTRegistration.PHASE) > 0) {
            event.setCanceled(true);
        }
    }
}
