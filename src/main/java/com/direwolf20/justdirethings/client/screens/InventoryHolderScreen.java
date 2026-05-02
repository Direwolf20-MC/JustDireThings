package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.common.blockentities.InventoryHolderBE;
import com.direwolf20.justdirethings.common.containers.InventoryHolderContainer;
import com.direwolf20.justdirethings.common.containers.slots.InventoryHolderSlot;
import com.direwolf20.justdirethings.common.network.data.InventoryHolderMoveItemsPayload;
import com.direwolf20.justdirethings.common.network.data.InventoryHolderSaveSlotPayload;
import com.direwolf20.justdirethings.common.network.data.InventoryHolderSettingsPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class InventoryHolderScreen extends BaseMachineScreen<InventoryHolderContainer> {
    private static final int SELECTED_BORDER_ARGB = 0xFFFF0000;
    private static final int GHOST_OVERLAY_ARGB = 0x80888888;

    private InventoryHolderBE inventoryHolderBE;
    private boolean compareNBT;
    private boolean filtersOnly;
    private boolean compareCounts;
    private boolean automatedFiltersOnly;
    private boolean automatedCompareCounts;
    private boolean renderPlayer;
    private int renderedSlot;

    public InventoryHolderScreen(InventoryHolderContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        if (container.baseMachineBE instanceof InventoryHolderBE inventoryHolderBE) {
            this.inventoryHolderBE = inventoryHolderBE;
            this.compareNBT = inventoryHolderBE.compareNBT;
            this.filtersOnly = inventoryHolderBE.filtersOnly;
            this.compareCounts = inventoryHolderBE.compareCounts;
            this.automatedFiltersOnly = inventoryHolderBE.automatedFiltersOnly;
            this.automatedCompareCounts = inventoryHolderBE.automatedCompareCounts;
            this.renderPlayer = inventoryHolderBE.renderPlayer;
            this.renderedSlot = inventoryHolderBE.renderedSlot;
        }
    }

    @Override
    public void addTickSpeedButton() {
        //No-Op
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.FILTERONLYBUTTON(leftPos + 134, topSectionTop + 22, filtersOnly, b -> {
            filtersOnly = !filtersOnly;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
        addRenderableWidget(ToggleButtonFactory.COMPARENBTBUTTON(leftPos + 152, topSectionTop + 22, compareNBT, b -> {
            compareNBT = !compareNBT;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
        addRenderableWidget(ToggleButtonFactory.COMPARECOUNTSBUTTON(leftPos + 134, topSectionTop + 4, compareCounts, b -> {
            compareCounts = !compareCounts;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
        addRenderableWidget(ToggleButtonFactory.SEND_INV_BUTTON(leftPos + 134, topSectionTop + 132, b -> {
            ClientPacketDistributor.sendToServer(new InventoryHolderMoveItemsPayload(0));
        }));
        addRenderableWidget(ToggleButtonFactory.PULL_INV_BUTTON(leftPos + 26, topSectionTop + 132, b -> {
            ClientPacketDistributor.sendToServer(new InventoryHolderMoveItemsPayload(1));
        }));
        addRenderableWidget(ToggleButtonFactory.SWAP_INV_BUTTON(leftPos + 152, topSectionTop + 132, b -> {
            ClientPacketDistributor.sendToServer(new InventoryHolderMoveItemsPayload(2));
        }));
        addRenderableWidget(ToggleButtonFactory.FILTERONLYBUTTON(leftPos + 26, topSectionTop + 22, automatedFiltersOnly, b -> {
            automatedFiltersOnly = !automatedFiltersOnly;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
        addRenderableWidget(ToggleButtonFactory.COMPARECOUNTSBUTTON(leftPos + 26, topSectionTop + 4, automatedCompareCounts, b -> {
            automatedCompareCounts = !automatedCompareCounts;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));

        addRenderableWidget(ToggleButtonFactory.SHOWFAKEPLAYERBUTTON(leftPos + 8, topSectionTop + 4, renderPlayer, b -> {
            renderPlayer = !renderPlayer;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
    }

    @Override
    public void setTopSection() {
        extraWidth = 0;
        extraHeight = 24;
    }

    private ItemStack ghostStack(int slotIndex) {
        return inventoryHolderBE.filterBasicHandler.getResource(slotIndex)
                .toStack(inventoryHolderBE.filterBasicHandler.getAmountAsInt(slotIndex));
    }

    @Override
    protected void extractSlot(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY) {
        if (slot instanceof InventoryHolderSlot && slot.getItem().isEmpty() && !ghostStack(slot.getSlotIndex()).isEmpty()) {
            ItemStack showStack = ghostStack(slot.getSlotIndex());
            graphics.fakeItem(showStack, slot.x, slot.y);
            graphics.itemDecorations(Minecraft.getInstance().font, showStack, slot.x, slot.y);
            graphics.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, GHOST_OVERLAY_ARGB);
        } else {
            super.extractSlot(graphics, slot, mouseX, mouseY);
        }

        if (slot.getSlotIndex() == renderedSlot && slot instanceof InventoryHolderSlot) {
            graphics.fill(slot.x, slot.y, slot.x + 16, slot.y + 1, SELECTED_BORDER_ARGB);
            graphics.fill(slot.x, slot.y + 15, slot.x + 16, slot.y + 16, SELECTED_BORDER_ARGB);
            graphics.fill(slot.x, slot.y, slot.x + 1, slot.y + 16, SELECTED_BORDER_ARGB);
            graphics.fill(slot.x + 15, slot.y, slot.x + 16, slot.y + 16, SELECTED_BORDER_ARGB);
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        Slot slot = this.hoveredSlot;
        if (slot instanceof InventoryHolderSlot && slot.getItem().isEmpty() && !ghostStack(slot.getSlotIndex()).isEmpty()) {
            ItemStack itemstack = ghostStack(slot.getSlotIndex());
            graphics.setTooltipForNextFrame(this.font, this.getTooltipFromContainerItem(itemstack), itemstack.getTooltipImage(), itemstack, mouseX, mouseY);
        } else {
            super.extractTooltip(graphics, mouseX, mouseY);
        }
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
        ClientPacketDistributor.sendToServer(new InventoryHolderSettingsPayload(compareNBT, filtersOnly, compareCounts, automatedFiltersOnly, automatedCompareCounts, renderPlayer, renderedSlot));
    }

    @Override
    public void renderInventorySection(GuiGraphicsExtractor graphics, int relX, int relY) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SOCIALBACKGROUND, relX, relY + 83 - 8, this.imageWidth, this.imageHeight - 55);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 0 && Minecraft.getInstance().hasControlDown() && hoveredSlot != null && hoveredSlot instanceof InventoryHolderSlot) {
            if (Minecraft.getInstance().hasShiftDown()) {
                if (hoveredSlot.getSlotIndex() >= 27 && hoveredSlot.getSlotIndex() <= 35) {
                    renderedSlot = hoveredSlot.getSlotIndex();
                    saveSettings();
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    return true;
                }
            } else {
                ClientPacketDistributor.sendToServer(new InventoryHolderSaveSlotPayload(hoveredSlot.getSlotIndex()));
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }
}
