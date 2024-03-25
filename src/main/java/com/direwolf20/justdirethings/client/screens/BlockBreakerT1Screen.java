package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.common.containers.BlockBreakerT1Container;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BlockBreakerT1Screen extends BaseMachineScreen<BlockBreakerT1Container> {
    public BlockBreakerT1Screen(BlockBreakerT1Container container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    @Override
    public void init() {
        super.init();
    }
}
