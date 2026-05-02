package com.direwolf20.justdirethings.client.screens.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class NumberButton extends BaseButton {
    private static final int BORDER_ARGB = 0xFF353535;
    private static final int FILL_ARGB = 0xFFD8D8D8;
    private static final int TEXT_ARGB = 0xFF404040;

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
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, BORDER_ARGB);
        graphics.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.width - 1, this.getY() + this.height - 1, FILL_ARGB);
        Font font = Minecraft.getInstance().font;
        graphics.pose().pushMatrix();
        float scale = 0.75f;
        graphics.pose().scale(scale, scale);
        String msg = String.format("%,d", value);
        float x = (this.getX() + this.width / 2f) / scale - font.width(msg) / 2f;
        float y = (this.getY() + (this.height - font.lineHeight) / 2f / scale) / scale + 1;
        graphics.text(font, msg, (int) x, (int) y, TEXT_ARGB, false);
        graphics.pose().popMatrix();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
