package com.direwolf20.justdirethings.client.screens.basescreens;

import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ValueButtons;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.common.blockentities.ItemCollectorBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.AreaAffectingBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.FilterableBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.containers.slots.FilterBasicSlot;
import com.direwolf20.justdirethings.common.network.data.AreaAffectingPayload;
import com.direwolf20.justdirethings.common.network.data.FilterSettingPayload;
import com.direwolf20.justdirethings.common.network.data.GhostSlotPayload;
import com.direwolf20.justdirethings.common.network.data.RedstoneSettingPayload;
import com.direwolf20.justdirethings.util.MiscHelpers;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMachineScreen<T extends BaseMachineContainer> extends BaseScreen<T> {
    protected BaseMachineContainer container;
    protected BaseMachineBE baseMachineBE;
    protected int xRadius = 3, yRadius = 3, zRadius = 3;
    protected int xOffset = 0, yOffset = 0, zOffset = 0;
    protected boolean renderArea = false;
    protected boolean allowlist = false;
    protected boolean compareNBT = false;
    protected MiscHelpers.RedstoneMode redstoneMode;
    protected List<ValueButtons> valueButtonsList = new ArrayList<>();

    public BaseMachineScreen(T container, Inventory pPlayerInventory, Component pTitle) {
        super(container, pPlayerInventory, pTitle);
        this.container = container;
        baseMachineBE = container.baseMachineBE;
        if (baseMachineBE instanceof AreaAffectingBE areaAffectingBE) {
            this.xRadius = areaAffectingBE.getAreaAffectingData().xRadius;
            this.yRadius = areaAffectingBE.getAreaAffectingData().yRadius;
            this.zRadius = areaAffectingBE.getAreaAffectingData().zRadius;
            this.xOffset = areaAffectingBE.getAreaAffectingData().xOffset;
            this.yOffset = areaAffectingBE.getAreaAffectingData().yOffset;
            this.zOffset = areaAffectingBE.getAreaAffectingData().zOffset;
            this.renderArea = areaAffectingBE.getAreaAffectingData().renderArea;
        }
        if (baseMachineBE instanceof FilterableBE filterableBE) {
            this.allowlist = filterableBE.getFilterData().allowlist;
            this.compareNBT = filterableBE.getFilterData().compareNBT;
        }
        if (baseMachineBE instanceof RedstoneControlledBE redstoneControlledBE) {
            this.redstoneMode = redstoneControlledBE.getRedstoneControlData().redstoneMode;
        }
    }

    @Override
    public void init() {
        super.init();
        valueButtonsList.clear();
        if (baseMachineBE instanceof AreaAffectingBE) {
            addRenderableWidget(ToggleButtonFactory.RENDERAREABUTTON(getGuiLeft() + 152, getGuiTop() + 45, renderArea, b -> {
                renderArea = !renderArea;
                ((GrayscaleButton) b).toggleActive();
                saveSettings();
            }));

            valueButtonsList.add(new ValueButtons(getGuiLeft() + 25, getGuiTop() + 10, xRadius, 0, ItemCollectorBE.maxRadius, font, (button, value) -> {
                xRadius = value;
                saveSettings();
            }));

            valueButtonsList.add(new ValueButtons(getGuiLeft() + 75, getGuiTop() + 10, yRadius, 0, ItemCollectorBE.maxRadius, font, (button, value) -> {
                yRadius = value;
                saveSettings();
            }));

            valueButtonsList.add(new ValueButtons(getGuiLeft() + 125, getGuiTop() + 10, zRadius, 0, ItemCollectorBE.maxRadius, font, (button, value) -> {
                zRadius = value;
                saveSettings();
            }));

            valueButtonsList.add(new ValueButtons(getGuiLeft() + 25, getGuiTop() + 25, xOffset, -ItemCollectorBE.maxOffset, ItemCollectorBE.maxOffset, font, (button, value) -> {
                xOffset = value;
                saveSettings();
            }));

            valueButtonsList.add(new ValueButtons(getGuiLeft() + 75, getGuiTop() + 25, yOffset, -ItemCollectorBE.maxOffset, ItemCollectorBE.maxOffset, font, (button, value) -> {
                yOffset = value;
                saveSettings();
            }));

            valueButtonsList.add(new ValueButtons(getGuiLeft() + 125, getGuiTop() + 25, zOffset, -ItemCollectorBE.maxOffset, ItemCollectorBE.maxOffset, font, (button, value) -> {
                zOffset = value;
                saveSettings();
            }));

            valueButtonsList.forEach(valueButtons -> valueButtons.widgetList.forEach(this::addRenderableWidget));
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        //super.renderLabels(guiGraphics, mouseX, mouseY);
        if (baseMachineBE instanceof AreaAffectingBE) {
            guiGraphics.drawString(this.font, Component.literal("Rad"), 5, 12, 4210752, false);
            guiGraphics.drawString(this.font, Component.literal("Off"), 5, 27, 4210752, false);
            guiGraphics.drawString(this.font, Component.literal("X"), 43, 4, 4210752, false);
            guiGraphics.drawString(this.font, Component.literal("Y"), 93, 4, 4210752, false);
            guiGraphics.drawString(this.font, Component.literal("Z"), 143, 4, 4210752, false);
        }
    }

    @Override
    public void onClose() {
        saveSettings();
        super.onClose();
    }

    @Override
    public boolean mouseClicked(double x, double y, int btn) {
        if (baseMachineBE instanceof FilterableBE filterableBE) {
            if (hoveredSlot == null || !(hoveredSlot instanceof FilterBasicSlot))
                return super.mouseClicked(x, y, btn);

            // By splitting the stack we can get air easily :) perfect removal basically
            ItemStack stack = this.menu.getCarried();// getMinecraft().player.inventoryMenu.getCarried();
            stack = stack.copy().split(hoveredSlot.getMaxStackSize()); // Limit to slot limit
            hoveredSlot.set(stack); // Temporarily update the client for continuity purposes
            PacketDistributor.SERVER.noArg().send(new GhostSlotPayload(hoveredSlot.index, stack, stack.getCount(), -1));
            return true;
        }
        return super.mouseClicked(x, y, btn);
    }

    public void saveSettings() {
        if (baseMachineBE instanceof AreaAffectingBE)
            PacketDistributor.SERVER.noArg().send(new AreaAffectingPayload(xRadius, yRadius, zRadius, xOffset, yOffset, zOffset, renderArea));
        if (baseMachineBE instanceof FilterableBE)
            PacketDistributor.SERVER.noArg().send(new FilterSettingPayload(allowlist, compareNBT));
        if (baseMachineBE instanceof RedstoneControlledBE)
            PacketDistributor.SERVER.noArg().send(new RedstoneSettingPayload(redstoneMode.ordinal()));
    }
}
