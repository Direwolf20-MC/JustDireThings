package com.direwolf20.justdirethings.common.events;

import com.direwolf20.justdirethings.common.items.tools.FerricoreHoe;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ToolActions;
import net.neoforged.neoforge.event.level.BlockEvent;

public class BlockToolModification {

    @SubscribeEvent
    public static void handleTickEndEvent(BlockEvent.BlockToolModificationEvent event) {
        if (event.getToolAction().equals(ToolActions.HOE_TILL) && event.getHeldItemStack().getItem() instanceof FerricoreHoe) {
            BlockState modifiedState = event.getState().getBlock().getToolModifiedState(event.getState(), event.getContext(), event.getToolAction(), event.isSimulated());
            if (modifiedState != null && modifiedState.is(Blocks.FARMLAND))
                event.setFinalState(Registration.GooSoil.get().defaultBlockState());
        }
    }
}
