package com.direwolf20.justdirethings.client.events;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.KeyBindings;
import com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool;
import com.direwolf20.justdirethings.common.network.data.ToggleToolPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = JustDireThings.MODID, value = Dist.CLIENT)
public class EventKeyInput {

    @SubscribeEvent
    public static void handleEventInput(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || event.phase == TickEvent.Phase.START)
            return;

        ItemStack toggleableTool = ToggleableTool.getToggleableTool(mc.player);
        if (toggleableTool.isEmpty())
            return;

        if (KeyBindings.toggleTool.consumeClick()) {
            PacketDistributor.SERVER.noArg().send(new ToggleToolPayload());
        } /*else if (KeyBindings.anchor.consumeClick()) {
            PacketDistributor.SERVER.noArg().send(new GadgetActionPayload(ActionGadget.ANCHOR));
        } else if (KeyBindings.range.consumeClick()) {
            int oldRange = GadgetNBT.getToolRange(tool);
            int newRange = oldRange + 1 > 15 ? 1 : oldRange + 1;
            PacketDistributor.SERVER.noArg().send(new GadgetActionPayload(ActionGadget.RANGE_CHANGE, IntTag.valueOf(newRange)));
        }/*else if (KeyBindings.rotateMirror.consumeClick()) {
            PacketHandler.sendToServer(new PacketRotateMirror());
        } else if (KeyBindings.undo.consumeClick()) {
            PacketHandler.sendToServer(new PacketUndo());
        } else if (KeyBindings.anchor.consumeClick()) {
            PacketHandler.sendToServer(new PacketAnchor());
        } else if (KeyBindings.fuzzy.consumeClick()) {
            PacketHandler.sendToServer(new PacketToggleFuzzy());
        } else if (KeyBindings.connectedArea.consumeClick()) {
            PacketHandler.sendToServer(new PacketToggleConnectedArea());
        }*/
    }
}
