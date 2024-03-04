package com.direwolf20.justdirethings.client.screens.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ToggleButton extends Button {
    private ResourceLocation[] textures;
    private int texturePosition;

    public ToggleButton(int x, int y, int width, int height, ResourceLocation[] textures, int texturePosition, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);

        this.textures = textures;
        setTexturePosition(texturePosition);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, textures[texturePosition]);
        guiGraphics.blit(textures[texturePosition], this.getX(), this.getY(), 0, 0, width, height, width, height);
    }

    @Override
    public void onClick(double p_onClick_1_, double p_onClick_3_) {
        super.onClick(p_onClick_1_, p_onClick_3_);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        return super.mouseClicked(x, y, button);
    }

    public int getTexturePosition() {
        return texturePosition;
    }

    public void setTexturePosition(int texturePosition) {
        if (texturePosition > textures.length)
            this.texturePosition = textures.length;
        else
            this.texturePosition = texturePosition;
    }

    public void nextTexturePosition() {
        if (texturePosition == textures.length)
            texturePosition = 0;
        else
            texturePosition++;
    }
}
