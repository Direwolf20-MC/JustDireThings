package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.ClickerT1BE;
import com.direwolf20.justdirethings.common.containers.ClickerT1Container;
import com.direwolf20.justdirethings.common.network.data.ClickerPayload;
import com.direwolf20.justdirethings.common.network.data.DirectionSettingPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class ClickerT1Screen extends BaseMachineScreen<ClickerT1Container> {
    public int clickType;
    public int clickTarget;
    public boolean sneaking;

    public ClickerT1Screen(ClickerT1Container container, Inventory inv, Component name) {
        super(container, inv, name);
        if (baseMachineBE instanceof ClickerT1BE clicker) {
            clickType = clicker.clickType;
            clickTarget = clicker.clickTarget.ordinal();
            sneaking = clicker.sneaking;
        }
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.DIRECTIONBUTTON(getGuiLeft() + 122, topSectionTop + 38, direction, b -> {
            ((ToggleButton) b).nextTexturePosition();
            direction = ((ToggleButton) b).getTexturePosition();
            PacketDistributor.SERVER.noArg().send(new DirectionSettingPayload(direction));
        }));

        addRenderableWidget(ToggleButtonFactory.CLICKTARGETBUTTON(getGuiLeft() + 56, topSectionTop + 38, clickTarget, b -> {
            ((ToggleButton) b).nextTexturePosition();
            clickTarget = ((ToggleButton) b).getTexturePosition();
            saveSettings();
        }));

        addRenderableWidget(ToggleButtonFactory.LEFTRIGHTCLICKBUTTON(getGuiLeft() + 38, topSectionTop + 38, clickType, b -> {
            ((ToggleButton) b).nextTexturePosition();
            clickType = ((ToggleButton) b).getTexturePosition();
            saveSettings();
        }));

        addRenderableWidget(ToggleButtonFactory.SNEAKCLICKBUTTON(getGuiLeft() + 20, topSectionTop + 38, sneaking, b -> {
            sneaking = !sneaking;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
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

    @Override
    public void saveSettings() {
        super.saveSettings();
        PacketDistributor.SERVER.noArg().send(new ClickerPayload(clickType, clickTarget, sneaking));
    }
}
