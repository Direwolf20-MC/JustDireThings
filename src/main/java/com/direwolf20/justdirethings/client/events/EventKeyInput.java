package com.direwolf20.justdirethings.client.events;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.KeyBindings;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.LeftClickableTool;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableItem;
import com.direwolf20.justdirethings.common.network.data.LeftClickPayload;
import com.direwolf20.justdirethings.common.network.data.ToggleToolPayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Set;

@Mod.EventBusSubscriber(modid = JustDireThings.MODID, value = Dist.CLIENT)
public class EventKeyInput {

    @SubscribeEvent
    public static void handleEventInput(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || event.phase == TickEvent.Phase.START)
            return;

        ItemStack toggleableItem = ToggleableItem.getToggleableItem(mc.player);
        if (!toggleableItem.isEmpty()) {
            if (KeyBindings.toggleTool.consumeClick()) {
                PacketDistributor.SERVER.noArg().send(new ToggleToolPayload("enabled"));
            }
        }
    }

    // Handling key presses
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null)
            return;
        if (event.getAction() == InputConstants.PRESS) {
            for (int i = 0; i < mc.player.getInventory().items.size(); i++) {
                ItemStack itemStack = mc.player.getInventory().getItem(i);
                if (itemStack.getItem() instanceof LeftClickableTool) {
                    Set<Ability> abilities = LeftClickableTool.getCustomBindingList(itemStack, new LeftClickableTool.Binding(event.getKey(), false));
                    if (!abilities.isEmpty()) {
                        PacketDistributor.SERVER.noArg().send(new LeftClickPayload(0, false, BlockPos.ZERO, -1, i, event.getKey(), false)); //Type 0 == air
                    }
                }
            }
        }
    }

    // Handling mouse clicks
    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null || event.getButton() == 0 || event.getButton() == 1 || event.getAction() != InputConstants.PRESS)
            return;
        for (int i = 0; i < mc.player.getInventory().items.size(); i++) {
            ItemStack itemStack = mc.player.getInventory().getItem(i);
            if (itemStack.getItem() instanceof LeftClickableTool) {
                Set<Ability> abilities = LeftClickableTool.getCustomBindingList(itemStack, new LeftClickableTool.Binding(event.getButton(), true));
                if (!abilities.isEmpty()) {
                    PacketDistributor.SERVER.noArg().send(new LeftClickPayload(0, false, BlockPos.ZERO, -1, i, event.getButton(), true)); //Type 0 == air
                }
            }
        }
    }
}
