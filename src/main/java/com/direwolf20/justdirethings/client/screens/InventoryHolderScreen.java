package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.common.blockentities.InventoryHolderBE;
import com.direwolf20.justdirethings.common.containers.InventoryHolderContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class InventoryHolderScreen extends BaseMachineScreen<InventoryHolderContainer> {
    InventoryHolderBE inventoryHolderBE;

    public InventoryHolderScreen(InventoryHolderContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        if (container.baseMachineBE instanceof InventoryHolderBE inventoryHolderBE) {
            this.inventoryHolderBE = inventoryHolderBE;
        }
    }

    @Override
    public void addTickSpeedButton() {
        //No-Op
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void setTopSection() {
        extraWidth = 0;
        extraHeight = 24;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
    }

    public void renderInventorySection(GuiGraphics guiGraphics, int relX, int relY) {
        guiGraphics.blitSprite(SOCIALBACKGROUND, relX, relY + 83 - 8, this.imageWidth, this.imageHeight - 55); //Inventory Section
    }
}
