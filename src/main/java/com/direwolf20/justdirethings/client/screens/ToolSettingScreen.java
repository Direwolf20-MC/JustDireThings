package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.BaseButton;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.common.containers.ToolSettingContainer;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.common.network.data.ToggleToolSlotPayload;
import com.direwolf20.justdirethings.util.MiscTools;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

public class ToolSettingScreen extends AbstractContainerScreen<ToolSettingContainer> {
    private final ResourceLocation GUI = new ResourceLocation(JustDireThings.MODID, "textures/gui/settings.png");

    protected final ToolSettingContainer container;
    Player player;
    protected ItemStack tool;
    private EnumSet<Ability> abilities = EnumSet.noneOf(Ability.class);
    int buttonsStartX = getGuiLeft() + 5;
    int buttonsStartY = getGuiTop() + 15;
    int toolSlot;
    protected final Map<Button, ExtendedSlider> sliders = new HashMap<>();
    protected ExtendedSlider shownSlider;

    public ToolSettingScreen(ToolSettingContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        this.container = container;
        this.player = container.playerEntity;
        if (player.getMainHandItem().getItem() instanceof ToggleableTool) {
            tool = player.getMainHandItem();
            toolSlot = player.getInventory().selected;
        } else if (player.getOffhandItem().getItem() instanceof ToggleableTool) {
            tool = player.getOffhandItem();
            toolSlot = 40;
        }
    }

    public void refreshButtons() {
        buttonsStartX = getGuiLeft() + 5;
        buttonsStartY = getGuiTop() + 25;
        clearWidgets();
        int counter = 0;
        for (Ability toolAbility : abilities) {
            if (toolAbility.getSettingType() == Ability.SettingType.TOGGLE) {
                boolean isActive = ToggleableTool.getSetting(tool, toolAbility.getName());
                Button button = new GrayscaleButton(buttonsStartX + ((counter / 2) * 18), buttonsStartY + ((counter % 2) * 18), 16, 16, toolAbility.getIconLocation(), Component.translatable(toolAbility.getLocalization()), isActive, (clicked) -> {
                    toggleSetting(toolAbility.getName());
                    ((GrayscaleButton) clicked).toggleActive();
                });
                addRenderableWidget(button);
                counter++;
            }
            if (toolAbility.getSettingType() == Ability.SettingType.CYCLE) {
                boolean isActive = ToggleableTool.getSetting(tool, toolAbility.getName());
                AbilityParams abilityParams = ((ToggleableTool) tool.getItem()).getAbilityParams(toolAbility);
                int possibleValues = ((abilityParams.maxSlider - abilityParams.minSlider) / abilityParams.increment) + 2;
                int currentValue = ToggleableTool.getToolValue(tool, toolAbility.getName());
                int buttonPosition = !isActive ? 0 : currentValue / abilityParams.increment;
                Button button = ToggleButtonFactory.ABILITYCYCLEBUTTON(buttonsStartX + ((counter / 2) * 18), buttonsStartY + ((counter % 2) * 18), toolAbility, buttonPosition, possibleValues, (clicked) -> {
                    cycleSetting(toolAbility.getName());
                });
                addRenderableWidget(button);
                counter++;
            }
            if (toolAbility.getSettingType() == Ability.SettingType.SLIDER) {
                boolean isActive = ToggleableTool.getSetting(tool, toolAbility.getName());
                Button button = new GrayscaleButton(buttonsStartX + ((counter / 2) * 18), buttonsStartY + ((counter % 2) * 18), 16, 16, toolAbility.getIconLocation(), Component.translatable(toolAbility.getLocalization()), isActive, (clicked) -> {
                    toggleSetting(toolAbility.getName());
                    ((GrayscaleButton) clicked).toggleActive();
                });
                addRenderableWidget(button);
                AbilityParams abilityParams = ((ToggleableTool) tool.getItem()).getAbilityParams(toolAbility);
                int currentValue = ToggleableTool.getToolValue(tool, toolAbility.getName());
                ExtendedSlider slider = new ExtendedSlider(buttonsStartX + 35, buttonsStartY - 18, 100, 15, Component.translatable(toolAbility.getLocalization()).append(": "), Component.empty(), abilityParams.minSlider, abilityParams.maxSlider, currentValue, true) {
                    @Override
                    protected void applyValue() {
                        setSetting(toolAbility.getName(), this.getValueInt());
                        super.applyValue();
                    }
                };
                sliders.put(button, slider);
                counter++;
            }
        }
    }

    /*public void showSlider(Button button) {
        clearSliders();
        ExtendedSlider slider = sliders.get(button);
        addRenderableWidget(slider);
    }*/

    protected void collectSlidersToRemove(List<AbstractWidget> widgetsToRemove) {
        for (ExtendedSlider slider : sliders.values()) {
            widgetsToRemove.add(slider);
        }
    }

    public void toggleSetting(String settingName) {
        PacketDistributor.SERVER.noArg().send(new ToggleToolSlotPayload(settingName, toolSlot, 0, -1));
    }

    public void cycleSetting(String settingName) {
        PacketDistributor.SERVER.noArg().send(new ToggleToolSlotPayload(settingName, toolSlot, 1, -1));
    }

    public void setSetting(String settingName, int value) {
        PacketDistributor.SERVER.noArg().send(new ToggleToolSlotPayload(settingName, toolSlot, 2, value));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        if (!tool.isEmpty()) {
            int x = getGuiLeft() + 5;
            int y = getGuiTop() + 5;
            int j1 = x + y * this.imageWidth;
            int k = tool.getMaxStackSize();
            String s = ChatFormatting.YELLOW.toString() + k;
            guiGraphics.renderFakeItem(tool, x, y, j1);
            guiGraphics.renderItemDecorations(this.font, tool, x, y, null);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
        for (Renderable renderable : this.renderables) {
            if (renderable instanceof BaseButton button && !button.getLocalization(pX, pY).equals(Component.empty())) {
                if (sliders.containsKey(button))
                    pGuiGraphics.renderTooltip(font, Language.getInstance().getVisualOrder(Arrays.asList(button.getLocalization(), Component.translatable("justdirethings.screen.rightclicksettings"))), pX, pY);
                else
                    pGuiGraphics.renderTooltip(font, button.getLocalization(), pX, pY);
            }
        }
    }

    @Override
    protected void renderSlot(GuiGraphics pGuiGraphics, Slot pSlot) {
        super.renderSlot(pGuiGraphics, pSlot);
    }

    @Override
    public void init() {
        super.init();
        if (this.tool.getItem() instanceof ToggleableTool toggleableTool) {
            this.abilities = toggleableTool.getAbilities();
            refreshButtons();
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        //super.renderLabels(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        InputConstants.Key mouseKey = InputConstants.getKey(p_keyPressed_1_, p_keyPressed_2_);
        if (p_keyPressed_1_ == 256 || minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            onClose();

            return true;
        }

        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public boolean mouseClicked(double x, double y, int btn) {
        if (hoveredSlot != null && hoveredSlot.getItem().getItem() instanceof ToggleableTool toggleableTool) {
            tool = hoveredSlot.getItem();
            toolSlot = hoveredSlot.getSlotIndex();
            this.abilities = toggleableTool.getAbilities();
            refreshButtons();
            return true;
        }
        if (btn == 1) {
            List<AbstractWidget> widgetsToRemove = new ArrayList<>();
            ExtendedSlider extendedSliderToAdd = null;
            for (Renderable renderable : new ArrayList<>(renderables)) {  // Create a copy of renderables to iterate over
                if (renderable instanceof GrayscaleButton grayscaleButton && sliders.containsKey(grayscaleButton) && MiscTools.inBounds(grayscaleButton.getX(), grayscaleButton.getY(), grayscaleButton.getWidth(), grayscaleButton.getHeight(), x, y)) {
                    collectSlidersToRemove(widgetsToRemove);
                    if (shownSlider == null || !shownSlider.equals(sliders.get(grayscaleButton)))
                        extendedSliderToAdd = sliders.get(grayscaleButton);
                    shownSlider = extendedSliderToAdd;
                    grayscaleButton.playDownSound(Minecraft.getInstance().getSoundManager());
                }
            }

            for (AbstractWidget abstractWidget : widgetsToRemove) {
                removeWidget(abstractWidget);
            }
            if (extendedSliderToAdd != null)
                addRenderableWidget(extendedSliderToAdd);
            if (widgetsToRemove.size() > 0 || extendedSliderToAdd != null)
                return true;
        }
        return super.mouseClicked(x, y, btn);
    }

    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double pScrollX, double pScrollY) {
        return super.mouseScrolled(mouseX, mouseY, pScrollX, pScrollY);
    }

    private static MutableComponent getTrans(String key, Object... args) {
        return Component.translatable(JustDireThings.MODID + "." + key, args);
    }

}
