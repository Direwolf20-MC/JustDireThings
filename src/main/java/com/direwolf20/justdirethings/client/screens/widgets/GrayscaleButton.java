package com.direwolf20.justdirethings.client.screens.widgets;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;

public class GrayscaleButton extends BaseButton {
    private static final int INACTIVE_TINT_ARGB = 0xFF545454;

    private Identifier texture;
    private boolean buttonActive;
    private int value;
    private Component localizationDisabled = Component.empty();

    public GrayscaleButton(int x, int y, int width, int height, Identifier texture, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.texture = texture;
        this.buttonActive = true; //AlwaysActive
        this.localization = localization;
        this.value = -1;
    }

    public GrayscaleButton(int x, int y, int width, int height, Identifier texture, Component localization, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.texture = texture;
        this.buttonActive = true; //AlwaysActive
        this.localization = localization;
        this.value = -1;
    }

    public GrayscaleButton(int x, int y, int width, int height, Identifier texture, Component localization, boolean active, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.texture = texture;
        this.buttonActive = active;
        this.localization = localization;
        this.value = -1;
    }

    public GrayscaleButton(int x, int y, int width, int height, Identifier texture, Component localizationOn, Component localizationOff, boolean active, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.texture = texture;
        this.buttonActive = active;
        this.localization = localizationOn;
        this.localizationDisabled = localizationOff;
        this.value = -1;
    }

    public GrayscaleButton(int x, int y, int width, int height, Identifier texture, Component localization, boolean active, int value, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.texture = texture;
        this.buttonActive = active;
        this.localization = localization;
        this.value = value;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        int color = buttonActive ? ARGB.white(this.alpha) : INACTIVE_TINT_ARGB;
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, this.getX(), this.getY(), 0.0F, 0.0F,
                width, height, width, height, color);
    }

    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        super.onClick(event, doubleClick);
    }

    public boolean getButtonActive() {
        return buttonActive;
    }

    public void toggleActive() {
        buttonActive = !buttonActive;
    }

    public void cyleValue(Ability toolAbility, ItemStack stack) {
        AbilityParams abilityParams = ((ToggleableTool) stack.getItem()).getAbilityParams(toolAbility);
        int nextValue = Math.min(abilityParams.maxSlider, value + abilityParams.increment);
        if (nextValue == value && buttonActive) { //If the next value is equal to the current one, its because we max'd out, so toggle it off
            buttonActive = false;
            nextValue = abilityParams.minSlider;
        } else if (value == abilityParams.minSlider && !buttonActive) {
            nextValue = abilityParams.minSlider;
            buttonActive = true;
        }
        value = nextValue;
    }

    @Override
    public Component getLocalization() {
        if (!localizationDisabled.equals(Component.empty()) && !getButtonActive())
            return localizationDisabled;
        if (getValue() == -1 || !getButtonActive())
            return localization;
        else
            return Component.translatable(((TranslatableContents) (localization).getContents()).getKey() + "value", getValue());
    }

    public int getValue() {
        return value;
    }
}
