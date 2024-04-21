package com.direwolf20.justdirethings.client.events;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.items.interfaces.LeftClickableTool;
import com.direwolf20.justdirethings.common.network.data.LeftClickPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = JustDireThings.MODID, value = Dist.CLIENT)
public class PlayerEvents {

    @SubscribeEvent
    public static void LeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getItemStack().getItem() instanceof LeftClickableTool) {
            PacketDistributor.SERVER.noArg().send(new LeftClickPayload(0, event.getHand().equals(InteractionHand.MAIN_HAND), BlockPos.ZERO, -1, -1, -1, false)); //Type 0 == air
        }
    }

    @SubscribeEvent
    public static void LeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getItemStack().getItem() instanceof LeftClickableTool && event.getFace() != null) {
            if (event.getAction() == PlayerInteractEvent.LeftClickBlock.Action.START) { //Only start has the 'proper' direction
                PacketDistributor.SERVER.noArg().send(new LeftClickPayload(1, event.getHand().equals(InteractionHand.MAIN_HAND), event.getPos(), event.getFace().ordinal(), -1, -1, false)); //Type 1 == Block
            } else if (event.getAction() == PlayerInteractEvent.LeftClickBlock.Action.ABORT) {
                PacketDistributor.SERVER.noArg().send(new LeftClickPayload(0, event.getHand().equals(InteractionHand.MAIN_HAND), event.getPos(), event.getFace().ordinal(), -1, -1, false)); //Type 1 == Block
                event.setCanceled(true);
            }
        }
    }
}
