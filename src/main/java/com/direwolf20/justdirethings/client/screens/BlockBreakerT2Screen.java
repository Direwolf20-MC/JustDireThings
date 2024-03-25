package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.common.containers.BlockBreakerT2Container;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BlockBreakerT2Screen extends BaseMachineScreen<BlockBreakerT2Container> {
    public BlockBreakerT2Screen(BlockBreakerT2Container container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    @Override
    public void setTopSection() {
        extraWidth = 60;
        extraHeight = 0;
    }

    @Override
    public void init() {
        super.init();
    }
}
