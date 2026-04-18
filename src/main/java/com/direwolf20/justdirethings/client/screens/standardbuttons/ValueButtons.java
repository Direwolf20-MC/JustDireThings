package com.direwolf20.justdirethings.client.screens.standardbuttons;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ValueButtons {
    private static final Identifier add = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/buttons/add.png");
    private static final Identifier subtract = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/buttons/remove.png");
    private static final int STANDARD_WIDTH = 12; // Example width
    private static final int STANDARD_HEIGHT = 12; // Example height
    private final Font font;
    private int value;
    private final int minValue;
    private final int maxValue;
    private final StringWidget valueDisplay;
    private final StringWidget labelDisplay;
    private final int valueCenterX;
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

        // Value Display — StringWidget is left-aligned in 26.1, so center it on the gap between
        // the -/+ buttons (midpoint at x + 21) by sizing the widget to the text's actual width.
        this.valueCenterX = x + 21;
        this.valueDisplay = centeredStringWidget(valueCenterX, y + 2, Component.literal(String.valueOf(startingValue)), font);
        widgetList.add(this.valueDisplay);

        this.labelDisplay = centeredStringWidget(valueCenterX, y - 8, label, font);
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
        Component msg = Component.literal(String.valueOf(value));
        this.valueDisplay.setMessage(msg);
        int width = font.width(msg);
        this.valueDisplay.setWidth(width);
        this.valueDisplay.setX(valueCenterX - width / 2);
    }

    private static StringWidget centeredStringWidget(int centerX, int y, Component message, Font font) {
        int width = font.width(message);
        StringWidget widget = new StringWidget(centerX - width / 2, y, width, font.lineHeight, message, font);
        return widget;
    }

    public int getValue() {
        return value;
    }
}
