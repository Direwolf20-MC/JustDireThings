package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.BaseButton;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.containers.ToolSettingContainer;
import com.direwolf20.justdirethings.common.items.interfaces.*;
import com.direwolf20.justdirethings.common.network.data.ToggleToolLeftRightClickPayload;
import com.direwolf20.justdirethings.common.network.data.ToggleToolRefreshSlots;
import com.direwolf20.justdirethings.common.network.data.ToggleToolSlotPayload;
import com.direwolf20.justdirethings.util.MiscTools;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class ToolSettingScreen extends AbstractContainerScreen<ToolSettingContainer> {
    private final Identifier GUI = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/settings.png");
    protected final Identifier JUSTSLOT = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/justslot.png");

    protected final ToolSettingContainer container;
    Player player;
    protected ItemStack tool = ItemStack.EMPTY;
    private EnumSet<Ability> abilities = EnumSet.noneOf(Ability.class);
    int buttonsStartX;
    int buttonsStartY;
    int toolSlot;
    protected Button shownAbilityButton;
    protected final Map<Button, ExtendedSlider> sliders = new HashMap<>();
    protected final Map<Button, ToggleButton> leftRightClickButtons = new HashMap<>();
    protected final Map<Button, GrayscaleButton> bindingButtons = new HashMap<>();
    protected final Map<Button, ToggleButton> customSettingsButtons = new HashMap<>();
    protected final Map<Button, ToggleButton> requireEquippedButtons = new HashMap<>();
    protected final Map<Button, Ability> buttonToAbilityMap = new HashMap<>();
    protected Map<ToggleButton, ToolRecords.AbilityBinding> bindingMap = new HashMap<>();
    protected boolean bindingEnabled = false;
    protected boolean requireEquipped = true;
    protected Set<AbstractWidget> widgetsToRemove = new HashSet<>();
    protected Set<AbstractWidget> widgetsToAdd = new HashSet<>();
    protected boolean renderablesChanged = false;

    public ToolSettingScreen(ToolSettingContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        this.container = container;
        this.player = container.playerEntity;
        if (player.getMainHandItem().getItem() instanceof ToggleableTool) {
            tool = player.getMainHandItem();
            toolSlot = player.getInventory().getSelectedSlot();
        } else if (player.getOffhandItem().getItem() instanceof ToggleableTool) {
            tool = player.getOffhandItem();
            toolSlot = 40;
        }
    }

    public void clearMaps() {
        sliders.clear();
        leftRightClickButtons.clear();
        bindingButtons.clear();
        customSettingsButtons.clear();
        buttonToAbilityMap.clear();
        bindingMap.clear();
        bindingEnabled = false;
        shownAbilityButton = null;
    }

    public void refreshButtons() {
        buttonsStartX = leftPos + 5;
        buttonsStartY = topPos + 25;
        clearWidgets();
        clearMaps();
        int counter = 0;
        for (Ability toolAbility : abilities) {
            if (!ToggleableTool.hasUpgrade(tool, toolAbility))
                continue;
            Button button = null;
            if (toolAbility.getSettingType() == Ability.SettingType.TOGGLE) {
                boolean isActive = ToggleableTool.getSetting(tool, toolAbility.getName());
                button = new GrayscaleButton(buttonsStartX + ((counter / 2) * 18), buttonsStartY + ((counter % 2) * 18), 16, 16, toolAbility.getIconLocation(), Component.translatable(toolAbility.getLocalization()), isActive, (clicked) -> {
                    toggleSetting(toolAbility.getName());
                    ((GrayscaleButton) clicked).toggleActive();
                });
                addRenderableWidget(button);
                counter++;
            } else if (toolAbility.getSettingType() == Ability.SettingType.CYCLE) {
                boolean isActive = ToggleableTool.getSetting(tool, toolAbility.getName());
                AbilityParams abilityParams = ((ToggleableTool) tool.getItem()).getAbilityParams(toolAbility);
                int possibleValues = ((abilityParams.maxSlider - abilityParams.minSlider) / abilityParams.increment) + 2;
                int currentValue = ToggleableTool.getToolValue(tool, toolAbility.getName());
                int buttonPosition = !isActive ? 0 : currentValue / abilityParams.increment;
                button = ToggleButtonFactory.ABILITYCYCLEBUTTON(buttonsStartX + ((counter / 2) * 18), buttonsStartY + ((counter % 2) * 18), toolAbility, buttonPosition, possibleValues, (clicked) -> {
                    cycleSetting(toolAbility.getName());
                });
                addRenderableWidget(button);
                counter++;
            } else if (toolAbility.getSettingType() == Ability.SettingType.SLIDER) {
                boolean isActive = ToggleableTool.getSetting(tool, toolAbility.getName());
                button = new GrayscaleButton(buttonsStartX + ((counter / 2) * 18), buttonsStartY + ((counter % 2) * 18), 16, 16, toolAbility.getIconLocation(), Component.translatable(toolAbility.getLocalization()), isActive, (clicked) -> {
                    toggleSetting(toolAbility.getName());
                    ((GrayscaleButton) clicked).toggleActive();
                });
                addRenderableWidget(button);
                AbilityParams abilityParams = ((ToggleableTool) tool.getItem()).getAbilityParams(toolAbility);
                int currentValue = ToggleableTool.getToolValue(tool, toolAbility.getName());
                if (abilityParams.minSlider != abilityParams.maxSlider) {
                    ExtendedSlider slider = new ExtendedSlider(buttonsStartX + 20, buttonsStartY - 18, 100, 15, Component.translatable(toolAbility.getLocalization()).append(": "), Component.empty(), abilityParams.minSlider, abilityParams.maxSlider, currentValue, true) {
                        @Override
                        protected void applyValue() {
                            setSetting(toolAbility.getName(), this.getValueInt());
                            super.applyValue();
                        }
                    };
                    sliders.put(button, slider);
                }
                counter++;
            }
            if (button != null && tool.getItem() instanceof LeftClickableTool && toolAbility.isBindable()) {
                int currentValue = LeftClickableTool.getBindingMode(tool, toolAbility);
                ToggleButton toggleButton;
                if (toolAbility.getBindingType() == Ability.BindingType.LEFT_AND_CUSTOM) {
                    toggleButton = ToggleButtonFactory.LEFTRIGHTCUSTOMCLICKBUTTON(buttonsStartX + 125, buttonsStartY - 18, currentValue, (clicked) -> {
                        ToolRecords.AbilityBinding binding = bindingMap.get(((ToggleButton) clicked));
                        if (binding == null) {
                            sendBinding(toolAbility.getName(), ((ToggleButton) clicked).getTexturePosition(), -1, false);
                            requireEquipped = true;
                        } else {
                            sendBinding(toolAbility.getName(), ((ToggleButton) clicked).getTexturePosition(), binding.key(), binding.isMouse());
                            requireEquipped = binding.requireEquipped();
                        }
                        if (((ToggleButton) clicked).getTexturePosition() == 2) {
                            if (!renderables.contains(bindingButtons.get(shownAbilityButton)))
                                widgetsToAdd.add(bindingButtons.get(shownAbilityButton));
                            if (!renderables.contains(requireEquippedButtons.get(shownAbilityButton)))
                                widgetsToAdd.add(requireEquippedButtons.get(shownAbilityButton));
                        } else {
                            widgetsToRemove.add(bindingButtons.get(shownAbilityButton));
                            widgetsToRemove.add(requireEquippedButtons.get(shownAbilityButton));
                        }
                        renderablesChanged = true;
                    });
                    leftRightClickButtons.put(button, toggleButton);
                } else {
                    toggleButton = ToggleButtonFactory.CUSTOMCLICKBUTTON(buttonsStartX + 125, buttonsStartY - 18, 0, (clicked) -> {
                        ToolRecords.AbilityBinding binding = bindingMap.get(((ToggleButton) clicked));
                        if (binding == null) {
                            sendBinding(toolAbility.getName(), 2, -1, false);
                            requireEquipped = true;
                        } else {
                            sendBinding(toolAbility.getName(), 2, binding.key(), binding.isMouse());
                            requireEquipped = binding.requireEquipped();
                        }
                        if (!renderables.contains(bindingButtons.get(shownAbilityButton)))
                            widgetsToAdd.add(bindingButtons.get(shownAbilityButton));
                        if (!renderables.contains(requireEquippedButtons.get(shownAbilityButton)))
                            widgetsToAdd.add(requireEquippedButtons.get(shownAbilityButton));
                        renderablesChanged = true;
                    });
                    leftRightClickButtons.put(button, toggleButton);
                }

                GrayscaleButton bindingButton = ToggleButtonFactory.KEYBIND_BUTTON(buttonsStartX + 143, buttonsStartY - 18, false, (clicked) -> {
                    bindingEnabled = true;
                    ((GrayscaleButton) clicked).toggleActive();
                });
                bindingMap.put(toggleButton, LeftClickableTool.getAbilityBinding(tool, toolAbility));
                this.bindingButtons.put(button, bindingButton);

                requireEquipped = bindingMap.get(toggleButton) == null ? true : bindingMap.get(toggleButton).requireEquipped();
                ToggleButton requireEquippedButton = ToggleButtonFactory.REQUIRE_EQUIPPED_BUTTON(buttonsStartX + 125, buttonsStartY, requireEquipped ? 0 : 1, (clicked2) -> {
                    requireEquipped = !requireEquipped;
                    ToolRecords.AbilityBinding binding = bindingMap.get(toggleButton);
                    if (binding == null)
                        sendBinding(toolAbility.getName(), 2, -1, false);
                    else
                        sendBinding(toolAbility.getName(), 2, binding.key(), binding.isMouse());
                });
                this.requireEquippedButtons.put(button, requireEquippedButton);
            }
            if (button != null && toolAbility.hasCustomSetting()) {
                Ability.CustomSettingType customSettingType = toolAbility.getCustomSetting();
                int value = ToggleableTool.getCustomSetting(tool, toolAbility.getName());
                if (customSettingType == Ability.CustomSettingType.RENDER) {
                    ToggleButton hideRenderButton = ToggleButtonFactory.HIDE_RENDER_ABILITY_BUTTON(buttonsStartX + 143, buttonsStartY, value, (clicked) -> {
                        toggleRender(toolAbility.getName(), ((ToggleButton) clicked).getTexturePosition());
                    });
                    this.customSettingsButtons.put(button, hideRenderButton);
                } else if (customSettingType == Ability.CustomSettingType.TARGET) {
                    ToggleButton targetButton = ToggleButtonFactory.HOMING_TARGET_BUTTON(buttonsStartX + 143, buttonsStartY, value, (clicked) -> {
                        toggleRender(toolAbility.getName(), ((ToggleButton) clicked).getTexturePosition());
                    });
                    this.customSettingsButtons.put(button, targetButton);
                }
            }
            if (button != null)
                buttonToAbilityMap.put(button, toolAbility);
        }
    }

    protected void sendBinding(String abilityName, int buttonType, int keyCode, boolean isMouse) {
        ClientPacketDistributor.sendToServer(new ToggleToolLeftRightClickPayload(toolSlot, abilityName, buttonType, keyCode, isMouse, requireEquipped));
    }

    protected void collectButtonsToRemove() {
        widgetsToRemove.addAll(sliders.values());
        widgetsToRemove.addAll(leftRightClickButtons.values());
        widgetsToRemove.addAll(bindingButtons.values());
        widgetsToRemove.addAll(customSettingsButtons.values());
        widgetsToRemove.addAll(requireEquippedButtons.values());
    }

    public void toggleSetting(String settingName) {
        ClientPacketDistributor.sendToServer(new ToggleToolSlotPayload(settingName, toolSlot, 0, -1));
    }

    public void cycleSetting(String settingName) {
        ClientPacketDistributor.sendToServer(new ToggleToolSlotPayload(settingName, toolSlot, 1, -1));
    }

    public void setSetting(String settingName, int value) {
        ClientPacketDistributor.sendToServer(new ToggleToolSlotPayload(settingName, toolSlot, 2, value));
    }

    public void toggleRender(String settingName, int value) {
        ClientPacketDistributor.sendToServer(new ToggleToolSlotPayload(settingName, toolSlot, 3, value));
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractContents(graphics, mouseX, mouseY, partialTicks);
        if (!tool.isEmpty()) {
            int x = leftPos + 5;
            int y = topPos + 5;
            graphics.fakeItem(tool, x, y);
            graphics.itemDecorations(this.font, tool, x, y);
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);
        for (Renderable renderable : this.renderables) {
            if (renderable instanceof ToggleButton button && showCustomBinding() && !button.getLocalization(mouseX, mouseY).equals(Component.empty()) && !requireEquippedButtons.containsValue(button) && !customSettingsButtons.containsValue(button)) {
                List<Component> lines = new ArrayList<>();
                lines.add(button.getLocalization());
                if (bindingMap.get(button) == null) {
                    lines.add(Component.translatable("justdirethings.unbound-screen"));
                } else {
                    ToolRecords.AbilityBinding binding = bindingMap.get(button);
                    if (binding.isMouse()) {
                        lines.add(Component.translatable("justdirethings.bound-mouse", binding.key()));
                    } else {
                        String bindingName = InputConstants.Type.KEYSYM.getOrCreate(binding.key()).getDisplayName().getString();
                        lines.add(Component.translatable("justdirethings.bound-key", bindingName));
                    }
                }
                graphics.setTooltipForNextFrame(font, lines, java.util.Optional.empty(), mouseX, mouseY);
            } else if (renderable instanceof BaseButton button && !button.getLocalization(mouseX, mouseY).equals(Component.empty())) {
                if (sliders.containsKey(button) || leftRightClickButtons.containsKey(button)) {
                    List<Component> lines = new ArrayList<>();
                    lines.add(button.getLocalization());
                    lines.add(Component.translatable("justdirethings.screen.rightclicksettings"));
                    graphics.setTooltipForNextFrame(font, lines, java.util.Optional.empty(), mouseX, mouseY);
                } else {
                    graphics.setTooltipForNextFrame(font, button.getLocalization(), mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public void init() {
        super.init();
        if (this.tool.getItem() instanceof ToggleableTool toggleableTool) {
            this.abilities = toggleableTool.getAbilities();
            refreshButtons();
            refreshToolAndSlots();
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        //super.extractLabels(graphics, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI, relX, relY, 0.0F, 0.0F, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        if (renderablesChanged)
            updateRenderables();
        for (Slot slot : container.dynamicSlots) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, JUSTSLOT, leftPos + slot.x - 1, topPos + slot.y - 1, 0.0F, 0.0F, 18, 18, 256, 256);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int keyCode = event.key();
        if (shownAbilityButton != null && leftRightClickButtons.get(shownAbilityButton) != null && bindingButtons.get(shownAbilityButton) != null && this.bindingEnabled) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                bindingMap.put(leftRightClickButtons.get(shownAbilityButton), null);
                leftRightClickButtons.get(shownAbilityButton).onPress(event);
                bindingButtons.get(shownAbilityButton).toggleActive();
                this.bindingEnabled = false;
                return true;
            } else {
                bindingMap.put(leftRightClickButtons.get(shownAbilityButton), new ToolRecords.AbilityBinding(buttonToAbilityMap.get(shownAbilityButton).getName(), keyCode, false, requireEquipped));
                leftRightClickButtons.get(shownAbilityButton).onPress(event);
                bindingButtons.get(shownAbilityButton).toggleActive();
                this.bindingEnabled = false;
                return true;
            }
        }
        InputConstants.Key mouseKey = InputConstants.getKey(event);
        if (keyCode == 256 || minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            onClose();
            return true;
        }

        return super.keyPressed(event);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double x = event.x();
        double y = event.y();
        int btn = event.button();
        if (btn != 0 && btn != 1 && shownAbilityButton != null && leftRightClickButtons.get(shownAbilityButton) != null && bindingButtons.get(shownAbilityButton) != null && this.bindingEnabled) {
            bindingMap.put(leftRightClickButtons.get(shownAbilityButton), new ToolRecords.AbilityBinding(buttonToAbilityMap.get(shownAbilityButton).getName(), btn, true, requireEquipped));
            leftRightClickButtons.get(shownAbilityButton).onPress(event);
            bindingButtons.get(shownAbilityButton).toggleActive();
            this.bindingEnabled = false;
            return true;
        }
        if (hoveredSlot != null && hoveredSlot.getItem().getItem() instanceof ToggleableTool toggleableTool) {
            tool = hoveredSlot.getItem();
            toolSlot = hoveredSlot.getSlotIndex();
            this.abilities = toggleableTool.getAbilities();
            refreshButtons();
            refreshToolAndSlots();
            return true;
        }
        if (btn == 1) {
            for (Renderable renderable : new ArrayList<>(renderables)) {
                if (renderable instanceof Button button && (sliders.containsKey(button) || leftRightClickButtons.containsKey(button) || bindingButtons.containsKey(button) || customSettingsButtons.containsKey(button)) && MiscTools.inBounds(button.getX(), button.getY(), button.getWidth(), button.getHeight(), x, y)) {
                    collectButtonsToRemove();
                    if (button.equals(shownAbilityButton))
                        shownAbilityButton = null;
                    else
                        shownAbilityButton = button;
                    if (sliders.containsKey(shownAbilityButton)) {
                        widgetsToAdd.add(sliders.get(shownAbilityButton));
                    }
                    if (leftRightClickButtons.containsKey(shownAbilityButton)) {
                        widgetsToAdd.add(leftRightClickButtons.get(shownAbilityButton));
                    }
                    if (bindingButtons.containsKey(shownAbilityButton) && showCustomBinding()) {
                        widgetsToAdd.add(bindingButtons.get(shownAbilityButton));
                    }
                    if (requireEquippedButtons.containsKey(shownAbilityButton) && showCustomBinding()) {
                        widgetsToAdd.add(requireEquippedButtons.get(shownAbilityButton));
                        requireEquipped = bindingMap.get(leftRightClickButtons.get(shownAbilityButton)) == null ? true : bindingMap.get(leftRightClickButtons.get(shownAbilityButton)).requireEquipped();
                    }
                    if (customSettingsButtons.containsKey(shownAbilityButton)) {
                        widgetsToAdd.add(customSettingsButtons.get(shownAbilityButton));
                    }
                    button.playDownSound(Minecraft.getInstance().getSoundManager());
                }
            }

            if (widgetsToRemove.size() > 0 || widgetsToAdd.size() > 0) {
                renderablesChanged = true;
                return true;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    public boolean showCustomBinding() {
        return (shownAbilityButton != null && leftRightClickButtons.containsKey(shownAbilityButton) && (leftRightClickButtons.get(shownAbilityButton).getTexturePosition() == 2 || (leftRightClickButtons.get(shownAbilityButton).getTexturePosition() == 0 && buttonToAbilityMap.get(shownAbilityButton).getBindingType() == Ability.BindingType.CUSTOM_ONLY)));
    }

    public void refreshToolAndSlots() {
        this.container.refreshSlots(tool);
        ClientPacketDistributor.sendToServer(new ToggleToolRefreshSlots(toolSlot));
    }

    public void updateRenderables() {
        if (!widgetsToRemove.isEmpty()) {
            for (AbstractWidget abstractWidget : widgetsToRemove) {
                removeWidget(abstractWidget);
            }
            widgetsToRemove.clear();
        }
        if (!widgetsToAdd.isEmpty()) {
            for (AbstractWidget abstractWidget : widgetsToAdd) {
                addRenderableWidget(abstractWidget);
            }
            widgetsToAdd.clear();
        }
        renderablesChanged = false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double pScrollX, double pScrollY) {
        this.sliders.forEach((button, slider) -> {
            if (slider.isMouseOver(mouseX, mouseY)) {
                slider.setValue(slider.getValueInt() + (pScrollY > 0 ? 1 : -1));
            }
        });

        return false;
    }
}
