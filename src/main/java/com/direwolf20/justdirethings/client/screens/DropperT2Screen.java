package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ValueButtons;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.client.screens.widgets.NumberButton;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.DropperT2BE;
import com.direwolf20.justdirethings.common.blockentities.ItemCollectorBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.AreaAffectingBE;
import com.direwolf20.justdirethings.common.containers.DropperT2Container;
import com.direwolf20.justdirethings.common.network.data.DirectionSettingPayload;
import com.direwolf20.justdirethings.common.network.data.DropperSettingPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class DropperT2Screen extends BaseMachineScreen<DropperT2Container> {
    protected int dropCount;

    public DropperT2Screen(DropperT2Container container, Inventory inv, Component name) {
        super(container, inv, name);
        if (baseMachineBE instanceof DropperT2BE dropper) {
            this.dropCount = dropper.dropCount;
        }
    }

    public void addFilterButtons() {
        addRenderableWidget(ToggleButtonFactory.COMPARENBTBUTTON(getGuiLeft() + 8, topSectionTop + 62, filterData.compareNBT, b -> {
            filterData.compareNBT = !filterData.compareNBT;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
    }

    public void addAreaButtons() {
        addRenderableWidget(ToggleButtonFactory.RENDERAREABUTTON(getGuiLeft() + 152, topSectionTop + 62, renderArea, b -> {
            renderArea = !renderArea;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));

        valueButtonsList.add(new ValueButtons(getGuiLeft() + 25, topSectionTop + 12, xOffset, -ItemCollectorBE.maxOffset, ItemCollectorBE.maxOffset, font, (button, value) -> {
            xOffset = value;
            saveSettings();
        }));

        valueButtonsList.add(new ValueButtons(getGuiLeft() + 75, topSectionTop + 12, yOffset, -ItemCollectorBE.maxOffset, ItemCollectorBE.maxOffset, font, (button, value) -> {
            yOffset = value;
            saveSettings();
        }));

        valueButtonsList.add(new ValueButtons(getGuiLeft() + 125, topSectionTop + 12, zOffset, -ItemCollectorBE.maxOffset, ItemCollectorBE.maxOffset, font, (button, value) -> {
            zOffset = value;
            saveSettings();
        }));

        valueButtonsList.forEach(valueButtons -> valueButtons.widgetList.forEach(this::addRenderableWidget));
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.DIRECTIONBUTTON(getGuiLeft() + 122, topSectionTop + 38, direction, b -> {
            ((ToggleButton) b).nextTexturePosition();
            direction = ((ToggleButton) b).getTexturePosition();
            PacketDistributor.SERVER.noArg().send(new DirectionSettingPayload(direction));
        }));

        addRenderableWidget(new NumberButton(getGuiLeft() + 20, topSectionTop + 41, 24, 12, dropCount, 1, 64, Component.translatable("justdirethings.screen.dropcount"), b -> {
            dropCount = ((NumberButton) b).getValue(); //The value is updated in the mouseClicked method below
            saveSettings();
        }));
    }

    @Override
    public void setTopSection() {
        extraWidth = 60;
        extraHeight = 0;
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
        PacketDistributor.SERVER.noArg().send(new DropperSettingPayload(dropCount));
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Component title = baseMachineBE.getBlockState().getBlock().getName();
        int titleX = topSectionLeft - getGuiLeft() + 20 + ((topSectionWidth - 40) / 2) - this.font.width(title) / 2;
        guiGraphics.drawString(this.font, title, titleX, topSectionTop - getGuiTop() - 14, 4210752, false);
        if (baseMachineBE instanceof AreaAffectingBE) {
            int areaWidth = 158; //The width of the area buttons is 157, including labels
            int xStart = topSectionLeft + (topSectionWidth / 2) - (areaWidth / 2) - getGuiLeft();
            guiGraphics.drawString(this.font, Component.literal("Off"), xStart - 4, topSectionTop - getGuiTop() + 14, 4210752, false);
            guiGraphics.drawString(this.font, Component.literal("X"), xStart + 35, topSectionTop - getGuiTop() + 4, 4210752, false);
            guiGraphics.drawString(this.font, Component.literal("Y"), xStart + 85, topSectionTop - getGuiTop() + 4, 4210752, false);
            guiGraphics.drawString(this.font, Component.literal("Z"), xStart + 135, topSectionTop - getGuiTop() + 4, 4210752, false);
        }
    }
}
