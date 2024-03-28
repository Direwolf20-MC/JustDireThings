package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.BlockBreakerT2BE;
import com.direwolf20.justdirethings.common.containers.BlockBreakerT2Container;
import com.direwolf20.justdirethings.common.network.data.DirectionSettingPayload;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class BlockBreakerT2Screen extends BaseMachineScreen<BlockBreakerT2Container> {
    public Direction direction;
    public BlockBreakerT2Screen(BlockBreakerT2Container container, Inventory inv, Component name) {
        super(container, inv, name);
        if (container.baseMachineBE instanceof BlockBreakerT2BE blockBreakerT2BE) {
            direction = blockBreakerT2BE.getDirection();
        }
    }

    @Override
    public void setTopSection() {
        extraWidth = 60;
        extraHeight = 0;
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.DIRECTIONBUTTON(getGuiLeft() + 116, topSectionTop + 62, direction.ordinal(), b -> {
            direction = Direction.values()[(direction.ordinal() == 5 ? 0 : direction.ordinal() + 1)];
            ((ToggleButton) b).nextTexturePosition();
            PacketDistributor.SERVER.noArg().send(new DirectionSettingPayload(direction.ordinal()));
        }));
    }
}
