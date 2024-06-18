package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.containers.FuelCanisterContainer;
import com.direwolf20.justdirethings.common.items.FuelCanister;
import com.direwolf20.justdirethings.util.MagicHelpers;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import java.awt.*;
import java.util.List;

public class FuelCanisterScreen extends AbstractContainerScreen<FuelCanisterContainer> {
    private final ResourceLocation GUI = ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/fuelcanister.png");

    protected final FuelCanisterContainer container;
    private ItemStack fuelCanister;
    private boolean isAllowList;
    private boolean isCompareNBT;

    public FuelCanisterScreen(FuelCanisterContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        this.container = container;
        this.fuelCanister = container.fuelCanisterItemstack;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        MutableComponent msg = Component.translatable("justdirethings.fuelcanisteritemsamt", MagicHelpers.formatted((float) FuelCanister.getFuelLevel(fuelCanister) / 200));
        guiGraphics.drawString(font, msg, this.getGuiLeft() + this.imageWidth / 2 - font.width(msg) / 2, getGuiTop() + 5, Color.DARK_GRAY.getRGB(), false);
        msg = Component.translatable("justdirethings.fuelcanisteramt", MagicHelpers.formatted(FuelCanister.getFuelLevel(fuelCanister)));
        guiGraphics.drawString(font, msg, (this.getGuiLeft() + this.imageWidth / 2) - font.width(msg) / 2, getGuiTop() + 15, Color.DARK_GRAY.getRGB(), false);
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            ItemStack itemstack = this.hoveredSlot.getItem();
            int fuelPerPiece = itemstack.getBurnTime(RecipeType.SMELTING);
            List<Component> tooltip = this.getTooltipFromContainerItem(itemstack);
            if (container.handler.isItemValid(0, itemstack)) {
                if (hasShiftDown()) {
                    tooltip.add(Component.translatable("justdirethings.fuelcanisteramt", MagicHelpers.formatted(fuelPerPiece)).withStyle(ChatFormatting.AQUA));
                    tooltip.add(Component.translatable("justdirethings.fuelcanisteramtstack", MagicHelpers.formatted(fuelPerPiece * itemstack.getCount())).withStyle(ChatFormatting.AQUA));
                } else {
                    tooltip.add(Component.translatable("justdirethings.fuelcanisteritemsamt", MagicHelpers.formatted((float) fuelPerPiece / 200)).withStyle(ChatFormatting.AQUA));
                    tooltip.add(Component.translatable("justdirethings.fuelcanisteritemsamtstack", MagicHelpers.formatted((float) (fuelPerPiece * itemstack.getCount()) / 200)).withStyle(ChatFormatting.AQUA));
                }
            }
            pGuiGraphics.renderTooltip(this.font, tooltip, itemstack.getTooltipImage(), itemstack, pX, pY);
        }
    }

    @Override
    protected void renderSlot(GuiGraphics pGuiGraphics, Slot pSlot) {
        super.renderSlot(pGuiGraphics, pSlot);
        if (!pSlot.getItem().isEmpty() && !container.handler.isItemValid(pSlot.getSlotIndex(), pSlot.getItem()))
            pGuiGraphics.fill(RenderType.guiOverlay(), pSlot.x, pSlot.y, pSlot.x + 16, pSlot.y + Mth.ceil(16.0F), 0x7FFF0000);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
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
