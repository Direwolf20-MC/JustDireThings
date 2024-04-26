package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.BaseButton;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.containers.ToolSettingContainer;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.LeftClickableTool;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.common.network.data.ToggleToolLeftRightClickPayload;
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
import org.lwjgl.glfw.GLFW;

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
    protected Button shownAbilityButton;
    protected final Map<Button, ExtendedSlider> sliders = new HashMap<>();
    protected final Map<Button, ToggleButton> leftRightClickButtons = new HashMap<>();
    protected final Map<Button, GrayscaleButton> bindingButtons = new HashMap<>();
    protected final Map<Button, GrayscaleButton> hideRenderButtons = new HashMap<>();
    protected final Map<Button, Ability> buttonToAbilityMap = new HashMap<>();
    protected Map<ToggleButton, LeftClickableTool.Binding> bindingMap = new HashMap<>();
    protected boolean bindingEnabled = false;
    protected Set<AbstractWidget> widgetsToRemove = new HashSet<>();
    protected Set<AbstractWidget> widgetsToAdd = new HashSet<>();
    protected boolean renderablesChanged = false;

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

    public void clearMaps() {
        sliders.clear();
        leftRightClickButtons.clear();
        bindingButtons.clear();
        hideRenderButtons.clear();
        buttonToAbilityMap.clear();
        bindingMap.clear();
        bindingEnabled = false;
        shownAbilityButton = null;
    }

    public void refreshButtons() {
        buttonsStartX = getGuiLeft() + 5;
        buttonsStartY = getGuiTop() + 25;
        clearWidgets();
        clearMaps();
        int counter = 0;
        for (Ability toolAbility : abilities) {
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
                ExtendedSlider slider = new ExtendedSlider(buttonsStartX + 20, buttonsStartY - 18, 100, 15, Component.translatable(toolAbility.getLocalization()).append(": "), Component.empty(), abilityParams.minSlider, abilityParams.maxSlider, currentValue, true) {
                    @Override
                    protected void applyValue() {
                        setSetting(toolAbility.getName(), this.getValueInt());
                        super.applyValue();
                    }
                };
                sliders.put(button, slider);
                counter++;
            }
            if (button != null && tool.getItem() instanceof LeftClickableTool && toolAbility.isBindable()) {
                int currentValue = LeftClickableTool.getBindingMode(tool, toolAbility);
                ToggleButton toggleButton;
                if (toolAbility.getBindingType() == Ability.BindingType.LEFT_AND_CUSTOM) {
                    toggleButton = ToggleButtonFactory.LEFTRIGHTCUSTOMCLICKBUTTON(buttonsStartX + 125, buttonsStartY - 18, currentValue, (clicked) -> {
                        LeftClickableTool.Binding binding = bindingMap.get(((ToggleButton) clicked));
                        if (binding == null)
                            sendBinding(toolAbility.getName(), ((ToggleButton) clicked).getTexturePosition(), -1, false);
                        else
                            sendBinding(toolAbility.getName(), ((ToggleButton) clicked).getTexturePosition(), binding.keyCode, binding.isMouse);
                        if (((ToggleButton) clicked).getTexturePosition() == 2) {
                            if (!renderables.contains(bindingButtons.get(shownAbilityButton)))
                                widgetsToAdd.add(bindingButtons.get(shownAbilityButton));
                        } else {
                            widgetsToRemove.add(bindingButtons.get(shownAbilityButton));
                        }
                        renderablesChanged = true;
                    });
                    leftRightClickButtons.put(button, toggleButton);
                } else {
                    toggleButton = ToggleButtonFactory.CUSTOMCLICKBUTTON(buttonsStartX + 125, buttonsStartY - 18, 0, (clicked) -> {
                        LeftClickableTool.Binding binding = bindingMap.get(((ToggleButton) clicked));
                        if (binding == null)
                            sendBinding(toolAbility.getName(), 2, -1, false); //Button Type 2 hardcoded to custom
                        else
                            sendBinding(toolAbility.getName(), 2, binding.keyCode, binding.isMouse);
                        if (!renderables.contains(bindingButtons.get(shownAbilityButton)))
                            widgetsToAdd.add(bindingButtons.get(shownAbilityButton));

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
            }
            if (button != null && toolAbility.hasRenderButton()) {
                boolean renderActive = true;
                GrayscaleButton hideRenderButton = ToggleButtonFactory.HIDE_RENDER_ABILITY_BUTTON(buttonsStartX + 143, buttonsStartY, renderActive, (clicked) -> {
                    toggleSetting(toolAbility.getName() + "_render");
                    ((GrayscaleButton) clicked).toggleActive();
                });
                this.hideRenderButtons.put(button, hideRenderButton);
            }
            if (button != null)
                buttonToAbilityMap.put(button, toolAbility);
        }
    }

    protected void sendBinding(String abilityName, int buttonType, int keyCode, boolean isMouse) {
        PacketDistributor.SERVER.noArg().send(new ToggleToolLeftRightClickPayload(toolSlot, abilityName, buttonType, keyCode, isMouse));
    }

    protected void collectButtonsToRemove() {
        widgetsToRemove.addAll(sliders.values());
        widgetsToRemove.addAll(leftRightClickButtons.values());
        widgetsToRemove.addAll(bindingButtons.values());
        widgetsToRemove.addAll(hideRenderButtons.values());
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
            if (renderable instanceof ToggleButton button && showCustomBinding() && !button.getLocalization(pX, pY).equals(Component.empty())) { //2 is custom
                if (bindingMap.get(button) == null) {
                    pGuiGraphics.renderTooltip(font, Language.getInstance().getVisualOrder(Arrays.asList(button.getLocalization(), Component.translatable("justdirethings.unbound-screen"))), pX, pY);
                } else {
                    LeftClickableTool.Binding binding = bindingMap.get(button);
                    if (binding.isMouse) {
                        pGuiGraphics.renderTooltip(font, Language.getInstance().getVisualOrder(Arrays.asList(button.getLocalization(), Component.translatable("justdirethings.bound-mouse", binding.keyCode))), pX, pY);
                    } else {
                        String bindingName = InputConstants.getKey(binding.keyCode, 0).getDisplayName().getString();
                        pGuiGraphics.renderTooltip(font, Language.getInstance().getVisualOrder(Arrays.asList(button.getLocalization(), Component.translatable("justdirethings.bound-key", bindingName))), pX, pY);
                    }
                }
            } else if (renderable instanceof BaseButton button && !button.getLocalization(pX, pY).equals(Component.empty())) {
                if (sliders.containsKey(button) || leftRightClickButtons.containsKey(button))
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
        if (renderablesChanged)
            updateRenderables();
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
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (shownAbilityButton != null && leftRightClickButtons.get(shownAbilityButton) != null && bindingButtons.get(shownAbilityButton) != null && this.bindingEnabled) {
            if (pKeyCode == GLFW.GLFW_KEY_ESCAPE) {
                bindingMap.put(leftRightClickButtons.get(shownAbilityButton), null);
                leftRightClickButtons.get(shownAbilityButton).onPress(); //This fires the packet to the server
                bindingButtons.get(shownAbilityButton).toggleActive();
                this.bindingEnabled = false;
                return true;
            } else {
                bindingMap.put(leftRightClickButtons.get(shownAbilityButton), new LeftClickableTool.Binding(pKeyCode, false));
                leftRightClickButtons.get(shownAbilityButton).onPress(); //This fires the packet to the server
                bindingButtons.get(shownAbilityButton).toggleActive();
                this.bindingEnabled = false;
                return true;
            }
        }
        InputConstants.Key mouseKey = InputConstants.getKey(pKeyCode, pScanCode);
        if (pKeyCode == 256 || minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            onClose();

            return true;
        }

        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean mouseClicked(double x, double y, int btn) {
        if (btn != 0 && btn != 1 && shownAbilityButton != null && leftRightClickButtons.get(shownAbilityButton) != null && bindingButtons.get(shownAbilityButton) != null && this.bindingEnabled) {
            bindingMap.put(leftRightClickButtons.get(shownAbilityButton), new LeftClickableTool.Binding(btn, true));
            leftRightClickButtons.get(shownAbilityButton).onPress();
            bindingButtons.get(shownAbilityButton).toggleActive();
            this.bindingEnabled = false;
            return true;
        }
        if (hoveredSlot != null && hoveredSlot.getItem().getItem() instanceof ToggleableTool toggleableTool) {
            tool = hoveredSlot.getItem();
            toolSlot = hoveredSlot.getSlotIndex();
            this.abilities = toggleableTool.getAbilities();
            refreshButtons();
            return true;
        }
        if (btn == 1) {
            for (Renderable renderable : new ArrayList<>(renderables)) {  // Create a copy of renderables to iterate over
                if (renderable instanceof Button button && (sliders.containsKey(button) || leftRightClickButtons.containsKey(button) || bindingButtons.containsKey(button) || hideRenderButtons.containsKey(button)) && MiscTools.inBounds(button.getX(), button.getY(), button.getWidth(), button.getHeight(), x, y)) { //If right click on any button, clear the optional buttons first.
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
                    if (hideRenderButtons.containsKey(shownAbilityButton)) {
                        widgetsToAdd.add(hideRenderButtons.get(shownAbilityButton));
                    }
                    button.playDownSound(Minecraft.getInstance().getSoundManager());
                }
            }

            if (widgetsToRemove.size() > 0 || widgetsToAdd.size() > 0) {
                renderablesChanged = true;
                return true;
            }
        }
        return super.mouseClicked(x, y, btn);
    }

    public boolean showCustomBinding() {
        return (shownAbilityButton != null & leftRightClickButtons.containsKey(shownAbilityButton) && (leftRightClickButtons.get(shownAbilityButton).getTexturePosition() == 2 || (leftRightClickButtons.get(shownAbilityButton).getTexturePosition() == 0 && buttonToAbilityMap.get(shownAbilityButton).getBindingType() == Ability.BindingType.CUSTOM_ONLY)));
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

    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double pScrollX, double pScrollY) {
        return super.mouseScrolled(mouseX, mouseY, pScrollX, pScrollY);
    }

    private static MutableComponent getTrans(String key, Object... args) {
        return Component.translatable(JustDireThings.MODID + "." + key, args);
    }

}
