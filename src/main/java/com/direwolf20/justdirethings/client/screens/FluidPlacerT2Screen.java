package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.containers.FluidPlacerT2Container;
import com.direwolf20.justdirethings.common.network.data.DirectionSettingPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class FluidPlacerT2Screen extends BaseMachineScreen<FluidPlacerT2Container> {
    public FluidPlacerT2Screen(FluidPlacerT2Container container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    @Override
    public void setTopSection() {
        extraWidth = 60;
        extraHeight = 0;
    }

    public int getFluidBarOffset() {
        return 204;
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.DIRECTIONBUTTON(getGuiLeft() + 116, topSectionTop + 62, direction, b -> {
            direction = ((ToggleButton) b).getTexturePosition();
            PacketDistributor.sendToServer(new DirectionSettingPayload(direction));
        }));
    }
}
