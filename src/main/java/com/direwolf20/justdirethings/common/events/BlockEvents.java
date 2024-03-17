package com.direwolf20.justdirethings.common.events;

import com.direwolf20.justdirethings.common.items.tools.BlazegoldHoe;
import com.direwolf20.justdirethings.common.items.tools.CelestigemHoe;
import com.direwolf20.justdirethings.common.items.tools.EclipseAlloyHoe;
import com.direwolf20.justdirethings.common.items.tools.FerricoreHoe;
import com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ToolActions;
import net.neoforged.neoforge.event.level.BlockEvent;

public class BlockEvents {
    @SubscribeEvent
    public static void BlockToolModificationEvent(BlockEvent.BlockToolModificationEvent event) {
        if (event.getToolAction().equals(ToolActions.HOE_TILL)) {
            ItemStack heldItem = event.getHeldItemStack();
            if (heldItem.getItem() instanceof FerricoreHoe && ToggleableTool.getEnabled(heldItem)) {
                BlockState modifiedState = event.getState().getBlock().getToolModifiedState(event.getState(), event.getContext(), event.getToolAction(), true);
                if (modifiedState != null && modifiedState.is(Blocks.FARMLAND))
                    event.setFinalState(Registration.GooSoil_Tier1.get().defaultBlockState());
            } else if (heldItem.getItem() instanceof BlazegoldHoe && ToggleableTool.getEnabled(heldItem)) {
                BlockState modifiedState = event.getState().getBlock().getToolModifiedState(event.getState(), event.getContext(), event.getToolAction(), true);
                if (modifiedState != null && modifiedState.is(Blocks.FARMLAND))
                    event.setFinalState(Registration.GooSoil_Tier2.get().defaultBlockState());
            } else if (heldItem.getItem() instanceof CelestigemHoe && ToggleableTool.getEnabled(heldItem)) {
                BlockState modifiedState = event.getState().getBlock().getToolModifiedState(event.getState(), event.getContext(), event.getToolAction(), true);
                if (modifiedState != null && modifiedState.is(Blocks.FARMLAND))
                    event.setFinalState(Registration.GooSoil_Tier3.get().defaultBlockState());
            } else if (heldItem.getItem() instanceof EclipseAlloyHoe && ToggleableTool.getEnabled(heldItem)) {
                BlockState modifiedState = event.getState().getBlock().getToolModifiedState(event.getState(), event.getContext(), event.getToolAction(), true);
                if (modifiedState != null && modifiedState.is(Blocks.FARMLAND))
                    event.setFinalState(Registration.GooSoil_Tier4.get().defaultBlockState());
            }
        }
    }
}
