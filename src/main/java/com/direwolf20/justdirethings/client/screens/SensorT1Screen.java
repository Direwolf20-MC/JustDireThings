package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.SensorT1BE;
import com.direwolf20.justdirethings.common.containers.SensorT1Container;
import com.direwolf20.justdirethings.common.network.data.SensorPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class SensorT1Screen extends BaseMachineScreen<SensorT1Container> {
    public int senseTarget;
    public boolean strongSignal;

    public SensorT1Screen(SensorT1Container container, Inventory inv, Component name) {
        super(container, inv, name);
        if (baseMachineBE instanceof SensorT1BE sensor) {
            senseTarget = sensor.sense_target.ordinal();
            strongSignal = sensor.strongSignal;
        }
    }

    @Override
    public void addFilterButtons() {
        addRenderableWidget(ToggleButtonFactory.ALLOWLISTBUTTON(getGuiLeft() + 38, topSectionTop + 38, filterData.allowlist, b -> {
            filterData.allowlist = !filterData.allowlist;
            ((ToggleButton) b).toggleActive();
            saveSettings();
        }));
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.SENSORTARGETBUTTON(getGuiLeft() + 56, topSectionTop + 38, senseTarget, b -> {
            ((ToggleButton) b).nextTexturePosition();
            senseTarget = ((ToggleButton) b).getTexturePosition();
            saveSettings();
        }));
        addRenderableWidget(ToggleButtonFactory.STRONGWEAKREDSTONEBUTTON(getGuiLeft() + 20, topSectionTop + 38, strongSignal ? 1 : 0, b -> {
            ((ToggleButton) b).nextTexturePosition();
            strongSignal = ((ToggleButton) b).getTexturePosition() == 1;
            saveSettings();
        }));
    }

    @Override
    public void setTopSection() {
        extraWidth = 0;
        extraHeight = 0;
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
        PacketDistributor.SERVER.noArg().send(new SensorPayload(senseTarget, strongSignal));
    }
}
