package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.containers.FuelCanisterContainer;
import com.direwolf20.justdirethings.common.items.FuelCanister;
import com.direwolf20.justdirethings.util.MagicHelpers;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.ArrayList;
import java.util.List;

public class FuelCanisterScreen extends AbstractContainerScreen<FuelCanisterContainer> {
    private static final int DARK_GRAY_ARGB = 0xFF404040;
    private static final int INVALID_SLOT_TINT_ARGB = 0x7FFF0000;

    private final Identifier GUI = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/fuelcanister.png");

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
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI, relX, relY, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractContents(graphics, mouseX, mouseY, partialTicks);
        MutableComponent msg = Component.translatable("justdirethings.fuelcanisteritemsamt",
                MagicHelpers.formatted((float) FuelCanister.getFuelLevel(fuelCanister) / 200));
        graphics.text(font, msg, this.leftPos + this.imageWidth / 2 - font.width(msg) / 2,
                this.topPos + 5, DARK_GRAY_ARGB, false);
        msg = Component.translatable("justdirethings.fuelcanisteramt",
                MagicHelpers.formatted(FuelCanister.getFuelLevel(fuelCanister)));
        graphics.text(font, msg, this.leftPos + this.imageWidth / 2 - font.width(msg) / 2,
                this.topPos + 15, DARK_GRAY_ARGB, false);
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            ItemStack itemstack = this.hoveredSlot.getItem();
            FuelValues fuelValues = minecraft != null && minecraft.level != null ? minecraft.level.fuelValues() : null;
            int fuelPerPiece = fuelValues != null ? itemstack.getBurnTime(RecipeType.SMELTING, fuelValues) : 0;
            List<Component> tooltip = new ArrayList<>(this.getTooltipFromContainerItem(itemstack));
            if (container.handler.isValid(0, ItemResource.of(itemstack))) {
                if (Minecraft.getInstance().hasShiftDown()) {
                    tooltip.add(Component.translatable("justdirethings.fuelcanisteramt",
                            MagicHelpers.formatted(fuelPerPiece)).withStyle(ChatFormatting.AQUA));
                    tooltip.add(Component.translatable("justdirethings.fuelcanisteramtstack",
                            MagicHelpers.formatted(fuelPerPiece * itemstack.getCount())).withStyle(ChatFormatting.AQUA));
                } else {
                    tooltip.add(Component.translatable("justdirethings.fuelcanisteritemsamt",
                            MagicHelpers.formatted((float) fuelPerPiece / 200)).withStyle(ChatFormatting.AQUA));
                    tooltip.add(Component.translatable("justdirethings.fuelcanisteritemsamtstack",
                            MagicHelpers.formatted((float) (fuelPerPiece * itemstack.getCount()) / 200)).withStyle(ChatFormatting.AQUA));
                }
            }
            graphics.setTooltipForNextFrame(this.font, tooltip, itemstack.getTooltipImage(), itemstack, mouseX, mouseY);
        }
    }

    @Override
    protected void extractSlot(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY) {
        super.extractSlot(graphics, slot, mouseX, mouseY);
        if (!slot.getItem().isEmpty()
                && !container.handler.isValid(slot.getSlotIndex(), ItemResource.of(slot.getItem()))) {
            graphics.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, INVALID_SLOT_TINT_ARGB);
        }
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
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
    public boolean keyPressed(KeyEvent event) {
        InputConstants.Key mouseKey = InputConstants.getKey(event);
        if (event.key() == 256 || minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            onClose();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double pScrollX, double pScrollY) {
        return super.mouseScrolled(mouseX, mouseY, pScrollX, pScrollY);
    }

    private static MutableComponent getTrans(String key, Object... args) {
        return Component.translatable(JustDireThings.MODID + "." + key, args);
    }

}
