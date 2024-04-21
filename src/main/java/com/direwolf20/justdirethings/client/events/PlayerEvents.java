package com.direwolf20.justdirethings.client.events;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.items.interfaces.LeftClickableTool;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@Mod.EventBusSubscriber(modid = JustDireThings.MODID, value = Dist.CLIENT)
public class PlayerEvents {

    @SubscribeEvent
    public static void LeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;

        ItemStack toggleableItem = LeftClickableTool.getLeftClickableItem(mc.player);
        if (!toggleableItem.isEmpty()) {

        }
    }

    @SubscribeEvent
    public static void LeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || !event.getAction().equals(PlayerInteractEvent.LeftClickBlock.Action.START))
            return;

        ItemStack toggleableItem = LeftClickableTool.getLeftClickableItem(mc.player);
        if (!toggleableItem.isEmpty()) {

        }
    }
}
