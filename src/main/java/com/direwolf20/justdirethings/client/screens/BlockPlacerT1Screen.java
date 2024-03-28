package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.BlockPlacerT1BE;
import com.direwolf20.justdirethings.common.containers.BlockPlacerT1Container;
import com.direwolf20.justdirethings.common.network.data.DirectionSettingPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class BlockPlacerT1Screen extends BaseMachineScreen<BlockPlacerT1Container> {
    public int placeDirection;
    public BlockPlacerT1Screen(BlockPlacerT1Container container, Inventory inv, Component name) {
        super(container, inv, name);
        if (container.baseMachineBE instanceof BlockPlacerT1BE blockPlacerT1BE) {
            placeDirection = blockPlacerT1BE.getPlaceDirection();
        }
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.DIRECTIONBUTTON(getGuiLeft() + 122, topSectionTop + 38, placeDirection, b -> {
            ((ToggleButton) b).nextTexturePosition();
            placeDirection = ((ToggleButton) b).getTexturePosition();
            PacketDistributor.SERVER.noArg().send(new DirectionSettingPayload(placeDirection));
        }));
    }

    @Override
    public void setTopSection() {
        extraWidth = 0;
        extraHeight = 0;
    }

    @Override
    public void addRedstoneButtons() {
        addRenderableWidget(ToggleButtonFactory.REDSTONEBUTTON(getGuiLeft() + 104, topSectionTop + 38, redstoneMode.ordinal(), b -> {
            redstoneMode = redstoneMode.next();
            ((ToggleButton) b).nextTexturePosition();
            saveSettings();
        }));
    }
}
