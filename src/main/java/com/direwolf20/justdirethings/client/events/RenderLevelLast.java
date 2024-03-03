package com.direwolf20.justdirethings.client.events;

import com.direwolf20.justdirethings.client.renderactions.ThingFinder;
import com.direwolf20.justdirethings.common.items.tools.utils.TieredGooItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class RenderLevelLast {
    @SubscribeEvent
    static void renderWorldLastEvent(RenderLevelStageEvent evt) {
        if (evt.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;

        ItemStack heldItemMain = player.getMainHandItem();
        ItemStack heldItemOff = player.getOffhandItem();

        if (heldItemMain.getItem() instanceof TieredGooItem) {
            ThingFinder.render(evt, player, heldItemMain);
        } else if (heldItemOff.getItem() instanceof TieredGooItem) {
            ThingFinder.render(evt, player, heldItemOff);
        }
    }
}
