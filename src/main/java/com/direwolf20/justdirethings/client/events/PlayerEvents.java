package com.direwolf20.justdirethings.client.events;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.LeftClickableTool;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.common.network.data.LeftClickPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Set;

@Mod.EventBusSubscriber(modid = JustDireThings.MODID, value = Dist.CLIENT)
public class PlayerEvents {

    @SubscribeEvent
    public static void LeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.getItem() instanceof ToggleableTool toggleableTool && itemStack.getItem() instanceof LeftClickableTool) {
            activateAbilities(itemStack, toggleableTool, event.getEntity(), event.getHand(), true, BlockPos.ZERO, Direction.DOWN);
        }
    }

    @SubscribeEvent
    public static void LeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.getItem() instanceof ToggleableTool toggleableTool && itemStack.getItem() instanceof LeftClickableTool && event.getFace() != null) {
            if (event.getAction() == PlayerInteractEvent.LeftClickBlock.Action.START) { //Only start has the 'proper' direction - this also runs both client AND server side!
                activateAbilities(itemStack, toggleableTool, event.getEntity(), event.getHand(), false, event.getPos(), event.getFace());
                //activateAbilities(itemStack, toggleableTool, event.getEntity(), event.getHand(), true, event.getPos(), event.getFace());
            } else if (event.getAction() == PlayerInteractEvent.LeftClickBlock.Action.ABORT) { //This runs server side only!
                //activateAbilities(itemStack, toggleableTool, event.getEntity(), event.getHand(), true, event.getPos(), event.getFace());
                //event.setCanceled(true);
            }
        }
    }

    private static void activateAbilities(ItemStack itemStack, ToggleableTool toggleableTool, Player player, InteractionHand hand, boolean air, BlockPos blockPos, Direction direction) {
        Set<Ability> abilities = LeftClickableTool.getLeftClickList(itemStack);
        if (!abilities.isEmpty()) {
            //Do them client side and Server side, since some abilities (like ore scanner) are client side activated.
            if (air) { //Air
                toggleableTool.useAbility(player.level(), player, hand, false);
                PacketDistributor.SERVER.noArg().send(new LeftClickPayload(0, hand == InteractionHand.MAIN_HAND, BlockPos.ZERO, -1, -1, -1, false)); //Type 0 == air
            } else { //Block - No need for a packet since this will run both client and server side! (See above!)
                UseOnContext useoncontext = new UseOnContext(player.level(), player, hand, itemStack, new BlockHitResult(Vec3.atCenterOf(blockPos), direction, blockPos, false));
                toggleableTool.useOnAbility(useoncontext, false);
                //Don't need the below because this actually runs on both client AND server.
                //PacketDistributor.SERVER.noArg().send(new LeftClickPayload(1, hand == InteractionHand.MAIN_HAND, blockPos, direction.ordinal(), -1, -1, false)); //Type 1 == Block
            }

        }
    }
}
