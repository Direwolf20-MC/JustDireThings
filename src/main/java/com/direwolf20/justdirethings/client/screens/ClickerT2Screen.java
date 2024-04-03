package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.ClickerT2BE;
import com.direwolf20.justdirethings.common.containers.ClickerT2Container;
import com.direwolf20.justdirethings.common.network.data.ClickerPayload;
import com.direwolf20.justdirethings.common.network.data.DirectionSettingPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class ClickerT2Screen extends BaseMachineScreen<ClickerT2Container> {
    public int clickType;
    public int clickTarget;
    public boolean sneaking;
    public boolean showFakePlayer;

    public ClickerT2Screen(ClickerT2Container container, Inventory inv, Component name) {
        super(container, inv, name);
        if (baseMachineBE instanceof ClickerT2BE clicker) {
            clickType = clicker.clickType;
            clickTarget = clicker.clickTarget.ordinal();
            sneaking = clicker.sneaking;
            showFakePlayer = clicker.showFakePlayer;
        }
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.DIRECTIONBUTTON(getGuiLeft() + 116, topSectionTop + 62, direction, b -> {
            ((ToggleButton) b).nextTexturePosition();
            direction = ((ToggleButton) b).getTexturePosition();
            PacketDistributor.SERVER.noArg().send(new DirectionSettingPayload(direction));
        }));

        addRenderableWidget(ToggleButtonFactory.CLICKTARGETBUTTON(getGuiLeft() + 44, topSectionTop + 62, clickTarget, b -> {
            ((ToggleButton) b).nextTexturePosition();
            clickTarget = ((ToggleButton) b).getTexturePosition();
            saveSettings();
        }));

        addRenderableWidget(ToggleButtonFactory.LEFTRIGHTCLICKBUTTON(getGuiLeft() + 44, topSectionTop + 44, clickType, b -> {
            ((ToggleButton) b).nextTexturePosition();
            clickType = ((ToggleButton) b).getTexturePosition();
            saveSettings();
        }));

        addRenderableWidget(ToggleButtonFactory.SNEAKCLICKBUTTON(getGuiLeft() + 26, topSectionTop + 44, sneaking, b -> {
            sneaking = !sneaking;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));

        addRenderableWidget(ToggleButtonFactory.SHOWFAKEPLAYERBUTTON(getGuiLeft() + 8, topSectionTop + 44, showFakePlayer, b -> {
            showFakePlayer = !showFakePlayer;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
    }

    @Override
    public void setTopSection() {
        extraWidth = 60;
        extraHeight = 0;
    }


    @Override
    public void saveSettings() {
        super.saveSettings();
        PacketDistributor.SERVER.noArg().send(new ClickerPayload(clickType, clickTarget, sneaking, showFakePlayer));
    }
}
