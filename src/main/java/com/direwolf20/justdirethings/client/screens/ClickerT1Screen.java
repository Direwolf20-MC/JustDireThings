package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.client.screens.widgets.NumberButton;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.ClickerT1BE;
import com.direwolf20.justdirethings.common.containers.ClickerT1Container;
import com.direwolf20.justdirethings.common.network.data.ClickerPayload;
import com.direwolf20.justdirethings.common.network.data.DirectionSettingPayload;
import com.direwolf20.justdirethings.common.network.data.TickSpeedPayload;
import com.direwolf20.justdirethings.util.MiscHelpers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class ClickerT1Screen extends BaseMachineScreen<ClickerT1Container> {
    public int clickType;
    public int clickTarget;
    public boolean sneaking;
    public boolean showFakePlayer;
    public int maxHoldTicks;
    public NumberButton maxHoldTicksButton;
    public NumberButton tickSpeedButton;

    public ClickerT1Screen(ClickerT1Container container, Inventory inv, Component name) {
        super(container, inv, name);
        if (baseMachineBE instanceof ClickerT1BE clicker) {
            clickType = clicker.clickType;
            clickTarget = clicker.clickTarget.ordinal();
            sneaking = clicker.sneaking;
            showFakePlayer = clicker.showFakePlayer;
            maxHoldTicks = clicker.maxHoldTicks;
        }
    }

    public void showHoldClicksButton() {
        if (clickType == 2)
            widgetsToAdd.add(maxHoldTicksButton);
        else
            widgetsToRemove.add(maxHoldTicksButton);
        renderablesChanged = true;
    }

    @Override
    public void addTickSpeedButton() {
        if (tickSpeedButton != null && renderables.contains(tickSpeedButton))
            widgetsToRemove.add(tickSpeedButton);
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
        widgetsToAdd.add(tickSpeedButton);
        renderablesChanged = true;
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.DIRECTIONBUTTON(getGuiLeft() + 122, topSectionTop + 38, direction, b -> {
            direction = ((ToggleButton) b).getTexturePosition();
            PacketDistributor.SERVER.noArg().send(new DirectionSettingPayload(direction));
        }));

        addRenderableWidget(ToggleButtonFactory.CLICKTARGETBUTTON(getGuiLeft() + 56, topSectionTop + 38, clickTarget, b -> {
            clickTarget = ((ToggleButton) b).getTexturePosition();
            saveSettings();
        }));

        addRenderableWidget(ToggleButtonFactory.LEFTRIGHTCLICKBUTTON(getGuiLeft() + 38, topSectionTop + 38, clickType, b -> {
            clickType = ((ToggleButton) b).getTexturePosition();
            if (clickType == 2) {
                tickSpeed = Math.max(tickSpeed, maxHoldTicks + 1);
                PacketDistributor.SERVER.noArg().send(new TickSpeedPayload(tickSpeed));
            }
            addTickSpeedButton();
            saveSettings();
            showHoldClicksButton();
        }));

        maxHoldTicksButton = new NumberButton(getGuiLeft() + 34, topSectionTop + 55, 24, 12, maxHoldTicks, 1, 1200, Component.translatable("justdirethings.screen.click-hold-for"), b -> {
            maxHoldTicks = ((NumberButton) b).getValue(); //The value is updated in the mouseClicked method below
            if (clickType == 2) {
                tickSpeed = Math.max(tickSpeed, maxHoldTicks + 1);
                PacketDistributor.SERVER.noArg().send(new TickSpeedPayload(tickSpeed));
            }
            addTickSpeedButton();
            saveSettings();
        });

        showHoldClicksButton();

        addRenderableWidget(ToggleButtonFactory.SNEAKCLICKBUTTON(getGuiLeft() + 20, topSectionTop + 38, sneaking, b -> {
            sneaking = !sneaking;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));

        addRenderableWidget(ToggleButtonFactory.SHOWFAKEPLAYERBUTTON(getGuiLeft() + 2, topSectionTop + 38, showFakePlayer, b -> {
            showFakePlayer = !showFakePlayer;
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
            redstoneMode = MiscHelpers.RedstoneMode.values()[((ToggleButton) b).getTexturePosition()];
            saveSettings();
        }));
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
        PacketDistributor.SERVER.noArg().send(new ClickerPayload(clickType, clickTarget, sneaking, showFakePlayer, maxHoldTicks));
    }
}
