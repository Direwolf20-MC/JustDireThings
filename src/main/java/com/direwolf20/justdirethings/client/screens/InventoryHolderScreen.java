package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.common.blockentities.InventoryHolderBE;
import com.direwolf20.justdirethings.common.containers.InventoryHolderContainer;
import com.direwolf20.justdirethings.common.containers.slots.InventoryHolderSlot;
import com.direwolf20.justdirethings.common.network.data.InventoryHolderSaveSlotPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class InventoryHolderScreen extends BaseMachineScreen<InventoryHolderContainer> {
    InventoryHolderBE inventoryHolderBE;

    public InventoryHolderScreen(InventoryHolderContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        if (container.baseMachineBE instanceof InventoryHolderBE inventoryHolderBE) {
            this.inventoryHolderBE = inventoryHolderBE;
        }
    }

    @Override
    public void addTickSpeedButton() {
        //No-Op
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void setTopSection() {
        extraWidth = 0;
        extraHeight = 24;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderSlot(GuiGraphics guiGraphics, Slot slot) {
        if (slot instanceof InventoryHolderSlot && slot.getItem().isEmpty() && inventoryHolderBE.savedItemStacks.containsKey(slot.getSlotIndex())) {
            ItemStack showStack = inventoryHolderBE.savedItemStacks.get(slot.getSlotIndex());
            guiGraphics.renderFakeItem(showStack, slot.x, slot.y, 0);
            guiGraphics.fill(RenderType.guiGhostRecipeOverlay(), slot.x, slot.y, slot.x + 16, slot.y + 16, 0x80888888);
        } else {
            super.renderSlot(guiGraphics, slot);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        Slot slot = this.hoveredSlot;
        if (slot instanceof InventoryHolderSlot && slot.getItem().isEmpty() && inventoryHolderBE.savedItemStacks.containsKey(slot.getSlotIndex())) {
            ItemStack itemstack = inventoryHolderBE.savedItemStacks.get(slot.getSlotIndex());
            guiGraphics.renderTooltip(this.font, this.getTooltipFromContainerItem(itemstack), itemstack.getTooltipImage(), itemstack, x, y);
        } else {
            super.renderTooltip(guiGraphics, x, y);
        }
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
    }

    public void renderInventorySection(GuiGraphics guiGraphics, int relX, int relY) {
        guiGraphics.blitSprite(SOCIALBACKGROUND, relX, relY + 83 - 8, this.imageWidth, this.imageHeight - 55); //Inventory Section
    }

    @Override
    public boolean mouseClicked(double x, double y, int btn) {
        if (btn == 1 && hoveredSlot != null && hoveredSlot instanceof InventoryHolderSlot) {
            PacketDistributor.sendToServer(new InventoryHolderSaveSlotPayload(hoveredSlot.getSlotIndex()));
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        }
        return super.mouseClicked(x, y, btn);
    }
}
