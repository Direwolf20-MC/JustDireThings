package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.ParadoxMachineBE;
import com.direwolf20.justdirethings.common.containers.ParadoxMachineContainer;
import com.direwolf20.justdirethings.common.network.data.ParadoxMachineSnapshotPayload;
import com.direwolf20.justdirethings.common.network.data.ParadoxRenderPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class ParadoxMachineScreen extends BaseMachineScreen<ParadoxMachineContainer> {
    private boolean renderParadox = false;
    private int targetType = 0;
    public ParadoxMachineScreen(ParadoxMachineContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        if (container.baseMachineBE instanceof ParadoxMachineBE paradoxMachineBE) {
            renderParadox = paradoxMachineBE.renderParadox;
            targetType = paradoxMachineBE.targetType;
        }
    }

    @Override
    public void init() {
        super.init();
        addSnapshotButton();
        addRenderButton();
        addTargetButton();
    }

    @Override
    public void setTopSection() {
        extraWidth = 80;
        extraHeight = 0;
    }

    public void addSnapshotButton() {
        addRenderableWidget(ToggleButtonFactory.SNAPSHOT_AREA_BUTTON(getGuiLeft() + 116, topSectionTop + 62, b -> {
            PacketDistributor.sendToServer(new ParadoxMachineSnapshotPayload());
        }));
    }

    public void addRenderButton() {
        addRenderableWidget(ToggleButtonFactory.RENDERPARADOXBUTTON(getGuiLeft() + 98, topSectionTop + 62, renderParadox, b -> {
            renderParadox = !renderParadox;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
    }

    public void addTargetButton() {
        addRenderableWidget(ToggleButtonFactory.PARADOXTARGETBUTTON(getGuiLeft() + 56, topSectionTop + 38, targetType, b -> {
            targetType = ((ToggleButton) b).getTexturePosition();
            saveSettings();
        }));
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
        PacketDistributor.sendToServer(new ParadoxRenderPayload(renderParadox, targetType));
    }
}
