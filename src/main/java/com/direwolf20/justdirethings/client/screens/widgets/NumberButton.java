package com.direwolf20.justdirethings.client.screens.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class NumberButton extends BaseButton {
    private int value;
    public int min;
    public int max;

    public NumberButton(int x, int y, int width, int height, int value, int min, int max, Component localization, OnPress onPress) {
        super(x, y, width, height, net.minecraft.network.chat.Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.value = value;
        this.localization = localization;
        this.min = min;
        this.max = max;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0xFF353535);
        guiGraphics.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.width - 1, this.getY() + this.height - 1, 0xFFD8D8D8);
        Font font = Minecraft.getInstance().font;
        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        float scale = 0.75f;//value > 99 || value < -99 ? 0.75f : 0.75f;
        stack.scale(scale, scale, scale);
        String msg = String.format("%,d", value);
        float x = (this.getX() + this.width / 2f) / scale - font.width(msg) / 2f;
        float y = (this.getY() + (this.height - font.lineHeight) / 2f / scale) / scale + 1;
        guiGraphics.drawString(font, msg, x, y, Color.DARK_GRAY.getRGB(), false);
        stack.popPose();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
