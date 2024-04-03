package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.common.containers.SensorT1Container;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SensorT1Screen extends BaseMachineScreen<SensorT1Container> {
    public SensorT1Screen(SensorT1Container container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void setTopSection() {
        extraWidth = 0;
        extraHeight = 0;
    }
}
