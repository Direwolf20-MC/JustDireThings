package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.common.blockentities.PlayerAccessorBE;
import com.direwolf20.justdirethings.common.containers.PlayerAccessorContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class PlayerAccessorScreen extends BaseMachineScreen<PlayerAccessorContainer> {
    PlayerAccessorBE playerAccessorBE;

    public PlayerAccessorScreen(PlayerAccessorContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        if (container.baseMachineBE instanceof PlayerAccessorBE playerAccessorBE)
            this.playerAccessorBE = playerAccessorBE;
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
        extraHeight = 0;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Component title = playerAccessorBE.getBlockState().getBlock().getName();
        int titleX = topSectionLeft - getGuiLeft() + 20 + ((topSectionWidth - 40) / 2) - this.font.width(title) / 2;
        guiGraphics.drawString(this.font, title, titleX, topSectionTop - getGuiTop() - 14, 4210752, false);
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
    }
}
