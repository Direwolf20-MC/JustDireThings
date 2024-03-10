package com.direwolf20.justdirethings.common.events;

import com.direwolf20.justdirethings.common.items.tools.BlazegoldHoe;
import com.direwolf20.justdirethings.common.items.tools.FerricoreHoe;
import com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ToolActions;
import net.neoforged.neoforge.event.level.BlockEvent;

public class BlockToolModification {

    @SubscribeEvent
    public static void handleTickEndEvent(BlockEvent.BlockToolModificationEvent event) {
        if (event.getToolAction().equals(ToolActions.HOE_TILL)) {
            if (event.getHeldItemStack().getItem() instanceof FerricoreHoe && ToggleableTool.getEnabled(event.getHeldItemStack())) {
                BlockState modifiedState = event.getState().getBlock().getToolModifiedState(event.getState(), event.getContext(), event.getToolAction(), true);
                if (modifiedState != null && modifiedState.is(Blocks.FARMLAND))
                    event.setFinalState(Registration.GooSoil_Tier1.get().defaultBlockState());
            } else if (event.getHeldItemStack().getItem() instanceof BlazegoldHoe && ToggleableTool.getEnabled(event.getHeldItemStack())) {
                BlockState modifiedState = event.getState().getBlock().getToolModifiedState(event.getState(), event.getContext(), event.getToolAction(), true);
                if (modifiedState != null && modifiedState.is(Blocks.FARMLAND))
                    event.setFinalState(Registration.GooSoil_Tier2.get().defaultBlockState());
            }
        }
    }
}
