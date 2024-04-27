package com.direwolf20.justdirethings.client.events;

import com.direwolf20.justdirethings.client.renderactions.MiscRenders;
import com.direwolf20.justdirethings.client.renderactions.ThingFinder;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
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

        if (heldItemMain.getItem() instanceof ToggleableTool toggleableTool) {
            ThingFinder.render(evt, player, heldItemMain);
            if (toggleableTool.canUseAbilityAndDurability(heldItemMain, Ability.VOIDSHIFT) && ToggleableTool.getSetting(heldItemMain, Ability.VOIDSHIFT.getName() + "_render"))
                MiscRenders.renderTransparentPlayer(evt, player, heldItemMain);
        }
        if (heldItemOff.getItem() instanceof ToggleableTool toggleableTool) {
            ThingFinder.render(evt, player, heldItemOff);
            if (toggleableTool.canUseAbilityAndDurability(heldItemOff, Ability.VOIDSHIFT) && ToggleableTool.getSetting(heldItemOff, Ability.VOIDSHIFT.getName() + "_render"))
                MiscRenders.renderTransparentPlayer(evt, player, heldItemOff);
        }
    }
}
