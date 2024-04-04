package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.DireScollList;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.SensorT1BE;
import com.direwolf20.justdirethings.common.blockentities.basebe.FilterableBE;
import com.direwolf20.justdirethings.common.containers.SensorT1Container;
import com.direwolf20.justdirethings.common.containers.slots.FilterBasicSlot;
import com.direwolf20.justdirethings.common.network.data.SensorPayload;
import com.direwolf20.justdirethings.util.MiscTools;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class SensorT1Screen extends BaseMachineScreen<SensorT1Container> {
    public int senseTarget;
    public boolean strongSignal;
    public boolean showBlockStates;
    public int blockStateSlot = -1;
    private DireScollList scrollPanel;
    public ItemStack stateItemStack = ItemStack.EMPTY;

    public SensorT1Screen(SensorT1Container container, Inventory inv, Component name) {
        super(container, inv, name);
        if (baseMachineBE instanceof SensorT1BE sensor) {
            senseTarget = sensor.sense_target.ordinal();
            strongSignal = sensor.strongSignal;
        }
    }

    @Override
    public void addFilterButtons() {
        addRenderableWidget(ToggleButtonFactory.ALLOWLISTBUTTON(getGuiLeft() + 38, topSectionTop + 38, filterData.allowlist, b -> {
            filterData.allowlist = !filterData.allowlist;
            ((ToggleButton) b).toggleActive();
            saveSettings();
        }));
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.SENSORTARGETBUTTON(getGuiLeft() + 56, topSectionTop + 38, senseTarget, b -> {
            ((ToggleButton) b).nextTexturePosition();
            senseTarget = ((ToggleButton) b).getTexturePosition();
            saveSettings();
        }));
        addRenderableWidget(ToggleButtonFactory.STRONGWEAKREDSTONEBUTTON(getGuiLeft() + 20, topSectionTop + 38, strongSignal ? 1 : 0, b -> {
            ((ToggleButton) b).nextTexturePosition();
            strongSignal = ((ToggleButton) b).getTexturePosition() == 1;
            saveSettings();
        }));

        this.scrollPanel = new DireScollList(this, topSectionLeft - 95, 90, topSectionTop + 5, topSectionTop + topSectionHeight - 10);
    }

    @Override
    public void setTopSection() {
        extraWidth = 0;
        extraHeight = 0;
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        if (showBlockStates && MiscTools.inBounds(topSectionLeft - 101, topSectionTop, 100, topSectionHeight, mouseX, mouseY))
            return false;
        return super.hasClickedOutside(mouseX, mouseY, guiLeftIn, guiTopIn, mouseButton);
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
        PacketDistributor.SERVER.noArg().send(new SensorPayload(senseTarget, strongSignal));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        if (showBlockStates) {
            guiGraphics.blitSprite(SOCIALBACKGROUND, topSectionLeft - 100, topSectionTop, 100, topSectionHeight);
        }
    }

    public void refreshStateWindow() {
        if (!showBlockStates || blockStateSlot == -1) return;
        stateItemStack = container.filterHandler.getStackInSlot(blockStateSlot);
        scrollPanel.setStateStack(stateItemStack);
        scrollPanel.refreshList();
    }

    @Override
    public boolean mouseClicked(double x, double y, int btn) {
        if (baseMachineBE instanceof FilterableBE filterableBE) {
            if (hoveredSlot != null && (hoveredSlot instanceof FilterBasicSlot)) {
                if (btn == 1) {
                    if (showBlockStates) {
                        blockStateSlot = -1;
                        stateItemStack = ItemStack.EMPTY;
                        scrollPanel.setStateStack(ItemStack.EMPTY);
                        this.removeWidget(scrollPanel);
                    } else {
                        blockStateSlot = hoveredSlot.getSlotIndex();
                        stateItemStack = hoveredSlot.getItem();
                        scrollPanel.setStateStack(stateItemStack);
                        this.addRenderableWidget(scrollPanel);
                    }
                    showBlockStates = !showBlockStates;
                    this.scrollPanel.refreshList();
                    return true;
                }
            }
        }
        boolean ret = super.mouseClicked(x, y, btn);
        refreshStateWindow();
        return ret;
    }
}
