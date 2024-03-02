package com.direwolf20.justdirethings.common.events;

import com.direwolf20.justdirethings.common.items.tools.FerricoreHoe;
import com.direwolf20.justdirethings.setup.Registration;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public class BlockToolModification {

    @SubscribeEvent
    public static void handleTickEndEvent(BlockEvent.BlockToolModificationEvent event) {
        if (event.getHeldItemStack().getItem() instanceof FerricoreHoe) {
            event.setFinalState(Registration.GooSoil.get().defaultBlockState());
        }
    }
}
