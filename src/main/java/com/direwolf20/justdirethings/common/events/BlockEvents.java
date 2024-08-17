package com.direwolf20.justdirethings.common.events;

import com.direwolf20.justdirethings.common.items.interfaces.Helpers;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableItem;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.common.items.tools.BlazegoldHoe;
import com.direwolf20.justdirethings.common.items.tools.CelestigemHoe;
import com.direwolf20.justdirethings.common.items.tools.EclipseAlloyHoe;
import com.direwolf20.justdirethings.common.items.tools.FerricoreHoe;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.ArrayList;
import java.util.List;

public class BlockEvents {
    public static boolean alreadyBreaking = false;
    public static final List<ItemStack> drops = new ArrayList<>();

    @SubscribeEvent
    public static void BlockToolModificationEvent(BlockEvent.BlockToolModificationEvent event) {
        ItemStack heldItem = event.getHeldItemStack();
        if (event.getItemAbility().equals(ItemAbilities.HOE_TILL) && heldItem.getItem() instanceof ToggleableItem toggleableItem) {
            if (heldItem.getItem() instanceof FerricoreHoe && toggleableItem.getEnabled(heldItem)) {
                BlockState modifiedState = event.getState().getBlock().getToolModifiedState(event.getState(), event.getContext(), event.getItemAbility(), true);
                if (modifiedState != null && modifiedState.is(Blocks.FARMLAND))
                    event.setFinalState(Registration.GooSoil_Tier1.get().defaultBlockState());
            } else if (heldItem.getItem() instanceof BlazegoldHoe && toggleableItem.getEnabled(heldItem)) {
                BlockState modifiedState = event.getState().getBlock().getToolModifiedState(event.getState(), event.getContext(), event.getItemAbility(), true);
                if (modifiedState != null && modifiedState.is(Blocks.FARMLAND))
                    event.setFinalState(Registration.GooSoil_Tier2.get().defaultBlockState());
            } else if (heldItem.getItem() instanceof CelestigemHoe && toggleableItem.getEnabled(heldItem)) {
                BlockState modifiedState = event.getState().getBlock().getToolModifiedState(event.getState(), event.getContext(), event.getItemAbility(), true);
                if (modifiedState != null && modifiedState.is(Blocks.FARMLAND))
                    event.setFinalState(Registration.GooSoil_Tier3.get().defaultBlockState());
            } else if (heldItem.getItem() instanceof EclipseAlloyHoe && toggleableItem.getEnabled(heldItem)) {
                BlockState modifiedState = event.getState().getBlock().getToolModifiedState(event.getState(), event.getContext(), event.getItemAbility(), true);
                if (modifiedState != null && modifiedState.is(Blocks.FARMLAND))
                    event.setFinalState(Registration.GooSoil_Tier4.get().defaultBlockState());
            }
        }
    }

    @SubscribeEvent
    public static void BlockBreakEvent(BlockEvent.BreakEvent event) {
        ItemStack itemStack = event.getPlayer().getMainHandItem();
        if (!alreadyBreaking && itemStack.getItem() instanceof ToggleableTool toggleableTool && itemStack.isCorrectToolForDrops(event.getState())) {
            alreadyBreaking = true;
            toggleableTool.mineBlocksAbility(itemStack, event.getPlayer().level(), event.getPos(), event.getPlayer());
            alreadyBreaking = false;
            event.setCanceled(true); //Cancel the original block broken, since its handled in the mineBlocksAbility
        }
    }

    @SubscribeEvent
    public static void BlockDrops(BlockDropsEvent event) {
        ItemStack itemStack = event.getTool();
        if (alreadyBreaking && itemStack.getItem() instanceof ToggleableTool toggleableTool) {
            List<ItemStack> newDrops = new ArrayList<>();
            for (ItemEntity drop : event.getDrops()) {
                newDrops.add(drop.getItem());
            }
            Helpers.combineDrops(drops, newDrops);
            event.getState().spawnAfterBreak(event.getLevel(), event.getPos(), itemStack, false);
            event.setCanceled(true);
        }
    }
}
