package com.direwolf20.justdirethings.client.screens.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GrayscaleButton extends Button {
    private ResourceLocation texture;
    boolean buttonActive;
    private String localization;

    public GrayscaleButton(int x, int y, int width, int height, ResourceLocation texture, String localization, boolean active, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.texture = texture;
        this.buttonActive = active;
        this.localization = localization;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        if (buttonActive)
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        else
            RenderSystem.setShaderColor(0.33f, 0.33f, 0.33f, 1.0f);
        RenderSystem.setShaderTexture(0, texture);
        guiGraphics.blit(texture, this.getX(), this.getY(), 0, 0, width, height, width, height);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    @Override
    public void onClick(double p_onClick_1_, double p_onClick_3_) {
        super.onClick(p_onClick_1_, p_onClick_3_);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        return super.mouseClicked(x, y, button);
    }

    public boolean getButtonActive() {
        return buttonActive;
    }

    public void toggleActive() {
        buttonActive = !buttonActive;
    }

    public String getLocalization() {
        return localization;
    }
}
