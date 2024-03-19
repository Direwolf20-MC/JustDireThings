package com.direwolf20.justdirethings.client.screens.standardbuttons;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ValueButtons {
    private static final ResourceLocation add = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/add.png");
    private static final ResourceLocation subtract = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/remove.png");
    private static final int STANDARD_WIDTH = 16; // Example width
    private static final int STANDARD_HEIGHT = 16; // Example height
    private final Font font;
    private int value;
    private final int minValue;
    private final int maxValue;
    private final StringWidget valueDisplay;
    public List<AbstractWidget> widgetList = new ArrayList<>();
    private Button.OnPress onPress;

    public ValueButtons(int x, int y, int startingValue, int minValue, int maxValue, Font font, Button.OnPress onPress) {
        this.value = startingValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.font = font;
        this.onPress = onPress;

        // Value Display
        this.valueDisplay = new StringWidget(x + 18, y, 8, font.lineHeight + 3, Component.literal(String.valueOf(startingValue)), font);
        widgetList.add(this.valueDisplay);

        // Minus Button
        widgetList.add(new GrayscaleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, subtract, button -> {
            decrementValue();
            if (onPress != null) onPress.onPress(button);
        }));

        // Plus Button
        widgetList.add(new GrayscaleButton(x + 36, y, STANDARD_WIDTH, STANDARD_HEIGHT, add, button -> {
            incrementValue();
            if (onPress != null) onPress.onPress(button);
        }));
    }

    private void incrementValue() {
        if (value < maxValue) {
            value++;
            updateDisplay();
        }
    }

    private void decrementValue() {
        if (value > minValue) {
            value--;
            updateDisplay();
        }
    }

    private void updateDisplay() {
        this.valueDisplay.setMessage(Component.literal(String.valueOf(value)));
    }

    public int getValue() {
        return value;
    }
}
