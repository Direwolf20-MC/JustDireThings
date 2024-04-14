package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.common.blockentities.EnergyTransmitterBE;
import com.direwolf20.justdirethings.common.containers.EnergyTransmitterContainer;
import com.direwolf20.justdirethings.common.network.data.EnergyTransmitterSettingPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class EnergyTransmitterScreen extends BaseMachineScreen<EnergyTransmitterContainer> {
    public boolean showParticles;
    public EnergyTransmitterScreen(EnergyTransmitterContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        if (container.baseMachineBE instanceof EnergyTransmitterBE energyTransmitterBE) {
            showParticles = energyTransmitterBE.showParticles;
        }
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.SHOWPARTICLESBUTTON(getGuiLeft() + 116, topSectionTop + 62, showParticles, b -> {
            showParticles = !showParticles;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
    }

    @Override
    public void addRedstoneButtons() {
        super.addRedstoneButtons();
    }

    @Override
    public void setTopSection() {
        extraWidth = 60;
        extraHeight = 0;
    }

    @Override
    public void addTickSpeedButton() {
        //No-Op
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
        PacketDistributor.SERVER.noArg().send(new EnergyTransmitterSettingPayload(showParticles));
    }
}
