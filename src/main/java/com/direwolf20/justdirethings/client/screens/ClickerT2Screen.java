package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.client.screens.widgets.NumberButton;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.ClickerT2BE;
import com.direwolf20.justdirethings.common.containers.ClickerT2Container;
import com.direwolf20.justdirethings.common.network.data.ClickerPayload;
import com.direwolf20.justdirethings.common.network.data.DirectionSettingPayload;
import com.direwolf20.justdirethings.common.network.data.TickSpeedPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class ClickerT2Screen extends BaseMachineScreen<ClickerT2Container> {
    public int clickType;
    public int clickTarget;
    public boolean sneaking;
    public boolean showFakePlayer;
    public int maxHoldTicks;
    public NumberButton maxHoldTicksButton;
    public NumberButton tickSpeedButton;

    public ClickerT2Screen(ClickerT2Container container, Inventory inv, Component name) {
        super(container, inv, name);
        if (baseMachineBE instanceof ClickerT2BE clicker) {
            clickType = clicker.clickType;
            clickTarget = clicker.clickTarget.ordinal();
            sneaking = clicker.sneaking;
            showFakePlayer = clicker.showFakePlayer;
            maxHoldTicks = clicker.maxHoldTicks;
        }
    }

    public void showHoldClicksButton() {
        if (clickType == 2)
            addRenderableWidget(maxHoldTicksButton);
        else
            removeWidget(maxHoldTicksButton);
    }

    @Override
    public void addTickSpeedButton() {
        if (tickSpeedButton != null && renderables.contains(tickSpeedButton))
            removeWidget(tickSpeedButton);
        if (clickType == 2) {
            tickSpeedButton = ToggleButtonFactory.TICKSPEEDBUTTON(getGuiLeft() + 144, topSectionTop + 40, tickSpeed, maxHoldTicks + 1, b -> {
                tickSpeed = ((NumberButton) b).getValue(); //The value is updated in the mouseClicked method below
                PacketDistributor.SERVER.noArg().send(new TickSpeedPayload(tickSpeed));
            });
        } else {
            tickSpeedButton = ToggleButtonFactory.TICKSPEEDBUTTON(getGuiLeft() + 144, topSectionTop + 40, tickSpeed, b -> {
                tickSpeed = ((NumberButton) b).getValue(); //The value is updated in the mouseClicked method below
                PacketDistributor.SERVER.noArg().send(new TickSpeedPayload(tickSpeed));
            });
        }

        addRenderableWidget(tickSpeedButton);
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
            if (clickType == 2) {
                tickSpeed = Math.max(tickSpeed, maxHoldTicks + 1);
                PacketDistributor.SERVER.noArg().send(new TickSpeedPayload(tickSpeed));
            }
            addTickSpeedButton();
            saveSettings();
            showHoldClicksButton();
        }));

        maxHoldTicksButton = new NumberButton(getGuiLeft() + 64, topSectionTop + 46, 24, 12, maxHoldTicks, 1, 1200, Component.translatable("justdirethings.screen.click-hold-for"), b -> {
            maxHoldTicks = ((NumberButton) b).getValue(); //The value is updated in the mouseClicked method below
            if (clickType == 2) {
                tickSpeed = Math.max(tickSpeed, maxHoldTicks + 1);
                PacketDistributor.SERVER.noArg().send(new TickSpeedPayload(tickSpeed));
            }
            addTickSpeedButton();
            saveSettings();
        });

        showHoldClicksButton();

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
        PacketDistributor.SERVER.noArg().send(new ClickerPayload(clickType, clickTarget, sneaking, showFakePlayer, maxHoldTicks));
    }
}
