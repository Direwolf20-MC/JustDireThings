package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ValueButtons;
import com.direwolf20.justdirethings.client.screens.widgets.BaseButton;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.ItemCollectorBE;
import com.direwolf20.justdirethings.common.containers.ItemCollectorContainer;
import com.direwolf20.justdirethings.common.containers.slots.FilterBasicSlot;
import com.direwolf20.justdirethings.common.network.data.GhostSlotPayload;
import com.direwolf20.justdirethings.common.network.data.ItemCollectorPayload;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class ItemCollectorScreen extends AbstractContainerScreen<ItemCollectorContainer> {
    private final ResourceLocation GUI = new ResourceLocation(JustDireThings.MODID, "textures/gui/itemcollector.png");

    protected final ItemCollectorContainer container;
    private int xRadius = 3, yRadius = 3, zRadius = 3;
    private int xOffset = 0, yOffset = 0, zOffset = 0;
    private boolean allowlist = false, compareNBT = false, renderArea = false;
    private MiscHelpers.RedstoneMode redstoneMode;
    private List<ValueButtons> valueButtonsList = new ArrayList<>();

    public ItemCollectorScreen(ItemCollectorContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        this.container = container;
        ItemCollectorBE itemCollectorBE = container.itemCollectorBE;
        this.xRadius = itemCollectorBE.xRadius;
        this.yRadius = itemCollectorBE.yRadius;
        this.zRadius = itemCollectorBE.zRadius;
        this.xOffset = itemCollectorBE.xOffset;
        this.yOffset = itemCollectorBE.yOffset;
        this.zOffset = itemCollectorBE.zOffset;
        this.allowlist = itemCollectorBE.allowlist;
        this.compareNBT = itemCollectorBE.compareNBT;
        this.renderArea = itemCollectorBE.renderArea;
        this.redstoneMode = itemCollectorBE.redstoneMode;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
        for (Renderable renderable : this.renderables) {
            if (renderable instanceof BaseButton button && !button.getLocalization(pX, pY).equals(Component.empty()))
                pGuiGraphics.renderTooltip(font, button.getLocalization(pX, pY), pX, pY);
        }
    }

    @Override
    protected void renderSlot(GuiGraphics pGuiGraphics, Slot pSlot) {
        super.renderSlot(pGuiGraphics, pSlot);
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.createStandardToggleButton(getGuiLeft() + 10, getGuiTop() + 10, redstoneMode.ordinal(), b -> {
            redstoneMode = redstoneMode.next();
            ((ToggleButton) b).nextTexturePosition();
            saveSettings();
        }));

        ValueButtons xRadiusButtons = new ValueButtons(getGuiLeft() + 30, getGuiTop() + 10, xRadius, 0, ItemCollectorBE.maxRadius, font, b -> {
            xRadius = valueButtonsList.get(0).getValue();
            saveSettings();
        });
        valueButtonsList.add(xRadiusButtons);

        ValueButtons yRadiusButtons = new ValueButtons(getGuiLeft() + 30, getGuiTop() + 30, yRadius, 0, ItemCollectorBE.maxRadius, font, b -> {
            yRadius = valueButtonsList.get(1).getValue();
            saveSettings();
        });
        valueButtonsList.add(yRadiusButtons);

        valueButtonsList.forEach(valueButtons -> valueButtons.widgetList.forEach(this::addRenderableWidget));
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
        saveSettings();
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

    public void saveSettings() {
        PacketDistributor.SERVER.noArg().send(new ItemCollectorPayload(xRadius, yRadius, zRadius, xOffset, yOffset, zOffset, allowlist, compareNBT, renderArea, redstoneMode.ordinal()));
    }

    @Override
    public boolean mouseClicked(double x, double y, int btn) {
        if (hoveredSlot == null || !(hoveredSlot instanceof FilterBasicSlot))
            return super.mouseClicked(x, y, btn);

        // By splitting the stack we can get air easily :) perfect removal basically
        ItemStack stack = this.menu.getCarried();// getMinecraft().player.inventoryMenu.getCarried();
        stack = stack.copy().split(hoveredSlot.getMaxStackSize()); // Limit to slot limit
        hoveredSlot.set(stack); // Temporarily update the client for continuity purposes
        PacketDistributor.SERVER.noArg().send(new GhostSlotPayload(hoveredSlot.index, stack, stack.getCount(), -1));

        return true;
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
