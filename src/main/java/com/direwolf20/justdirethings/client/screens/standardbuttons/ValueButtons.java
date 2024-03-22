package com.direwolf20.justdirethings.client.screens.standardbuttons;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ValueButtons {
    private static final ResourceLocation add = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/add.png");
    private static final ResourceLocation subtract = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/remove.png");
    private static final int STANDARD_WIDTH = 12; // Example width
    private static final int STANDARD_HEIGHT = 12; // Example height
    private final Font font;
    private int value;
    private final int minValue;
    private final int maxValue;
    private final StringWidget valueDisplay;
    private final StringWidget labelDisplay;
    public List<AbstractWidget> widgetList = new ArrayList<>();
    private BiConsumer<ValueButtons, Integer> onPress;

    public ValueButtons(int x, int y, int startingValue, int minValue, int maxValue, Font font, BiConsumer<ValueButtons, Integer> onPress) {
        this(x, y, startingValue, minValue, maxValue, Component.empty(), font, onPress);
    }

    public ValueButtons(int x, int y, int startingValue, int minValue, int maxValue, Component label, Font font, BiConsumer<ValueButtons, Integer> onPress) {
        this.value = startingValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.font = font;
        this.onPress = onPress;

        // Value Display
        this.valueDisplay = new StringWidget(x + 11, y + 2, 20, font.lineHeight, Component.literal(String.valueOf(startingValue)), font);
        widgetList.add(this.valueDisplay);

        this.labelDisplay = new StringWidget(x, y - 8, 40, font.lineHeight, label, font);
        widgetList.add(this.labelDisplay);

        // Minus Button
        widgetList.add(new GrayscaleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, subtract, button -> {
            decrementValue();
            if (onPress != null) onPress.accept(this, this.value);
        }));

        // Plus Button
        widgetList.add(new GrayscaleButton(x + 30, y, STANDARD_WIDTH, STANDARD_HEIGHT, add, button -> {
            incrementValue();
            if (onPress != null) onPress.accept(this, this.value);
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
