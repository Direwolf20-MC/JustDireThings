package com.direwolf20.justdirethings.client.screens.widgets;

import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.AbilityParams;
import com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class GrayscaleButton extends BaseButton {
    private ResourceLocation texture;
    private boolean buttonActive;
    private int value;
    private Component localizationDisabled = Component.empty();

    public GrayscaleButton(int x, int y, int width, int height, ResourceLocation texture, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.texture = texture;
        this.buttonActive = true; //AlwaysActive
        this.localization = localization;
        this.value = -1;
    }

    public GrayscaleButton(int x, int y, int width, int height, ResourceLocation texture, Component localization, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.texture = texture;
        this.buttonActive = true; //AlwaysActive
        this.localization = localization;
        this.value = -1;
    }

    public GrayscaleButton(int x, int y, int width, int height, ResourceLocation texture, Component localization, boolean active, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.texture = texture;
        this.buttonActive = active;
        this.localization = localization;
        this.value = -1;
    }

    public GrayscaleButton(int x, int y, int width, int height, ResourceLocation texture, Component localizationOn, Component localizationOff, boolean active, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.texture = texture;
        this.buttonActive = active;
        this.localization = localizationOn;
        this.localizationDisabled = localizationOff;
        this.value = -1;
    }

    public GrayscaleButton(int x, int y, int width, int height, ResourceLocation texture, Component localization, boolean active, int value, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.texture = texture;
        this.buttonActive = active;
        this.localization = localization;
        this.value = value;
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
            return Component.translatable(localization.getString() + "value", getValue());
    }

    public int getValue() {
        return value;
    }
}
