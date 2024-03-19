package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.screens.widgets.BaseButton;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.common.containers.ToolSettingContainer;
import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool;
import com.direwolf20.justdirethings.common.network.data.ToggleToolSlotPayload;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.EnumSet;

public class ToolSettingScreen extends AbstractContainerScreen<ToolSettingContainer> {
    private final ResourceLocation GUI = new ResourceLocation(JustDireThings.MODID, "textures/gui/settings.png");

    protected final ToolSettingContainer container;
    Player player;
    protected ItemStack tool;
    private EnumSet<Ability> abilities = EnumSet.noneOf(Ability.class);
    int buttonsStartX = getGuiLeft() + 5;
    int buttonsStartY = getGuiTop() + 15;
    int toolSlot;

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
                int currentValue = ToggleableTool.getToolValue(tool, toolAbility.getName());
                Button button = new GrayscaleButton(buttonsStartX + ((counter / 2) * 18), buttonsStartY + ((counter % 2) * 18), 16, 16, toolAbility.getIconLocation(), Component.translatable(toolAbility.getLocalization()), isActive, currentValue, (clicked) -> {
                    cycleSetting(toolAbility.getName());
                    ((GrayscaleButton) clicked).cyleValue(toolAbility, tool);
                });
                addRenderableWidget(button);
                counter++;
            }
        }
    }

    public void toggleSetting(String settingName) {
        PacketDistributor.SERVER.noArg().send(new ToggleToolSlotPayload(settingName, toolSlot, 0));
    }

    public void cycleSetting(String settingName) {
        PacketDistributor.SERVER.noArg().send(new ToggleToolSlotPayload(settingName, toolSlot, 1));
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
            if (renderable instanceof BaseButton button && !button.getLocalization(pX, pY).equals(Component.empty()))
                pGuiGraphics.renderTooltip(font, button.getLocalization(), pX, pY);
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
