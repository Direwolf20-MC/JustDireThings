package com.direwolf20.justdirethings.client.screens.basescreens;

import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ValueButtons;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.common.blockentities.ItemCollectorBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.AreaAffectingBE;
import com.direwolf20.justdirethings.common.containers.basecontainers.AreaAffectingContainer;
import com.direwolf20.justdirethings.common.network.data.AreaAffectingPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public abstract class AreaAffectingScreen<T extends AreaAffectingContainer> extends BaseScreen<T> {
    protected AreaAffectingContainer container;
    protected AreaAffectingBE areaAffectingBE;
    protected int xRadius = 3, yRadius = 3, zRadius = 3;
    protected int xOffset = 0, yOffset = 0, zOffset = 0;
    protected boolean renderArea = false;
    protected List<ValueButtons> valueButtonsList = new ArrayList<>();

    public AreaAffectingScreen(T container, Inventory pPlayerInventory, Component pTitle) {
        super(container, pPlayerInventory, pTitle);
        this.container = container;
        areaAffectingBE = container.areaAffectingBE;
        this.xRadius = areaAffectingBE.getAreaAffectingData().xRadius;
        this.yRadius = areaAffectingBE.getAreaAffectingData().yRadius;
        this.zRadius = areaAffectingBE.getAreaAffectingData().zRadius;
        this.xOffset = areaAffectingBE.getAreaAffectingData().xOffset;
        this.yOffset = areaAffectingBE.getAreaAffectingData().yOffset;
        this.zOffset = areaAffectingBE.getAreaAffectingData().zOffset;
        this.renderArea = areaAffectingBE.getAreaAffectingData().renderArea;
    }

    @Override
    public void init() {
        super.init();
        valueButtonsList.clear();

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

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        //super.renderLabels(guiGraphics, mouseX, mouseY);
        guiGraphics.drawString(this.font, Component.literal("Rad"), 5, 12, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal("Off"), 5, 27, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal("X"), 43, 4, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal("Y"), 93, 4, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal("Z"), 143, 4, 4210752, false);
    }

    @Override
    public void onClose() {
        saveSettings();
        super.onClose();
    }

    public void saveSettings() {
        PacketDistributor.SERVER.noArg().send(new AreaAffectingPayload(xRadius, yRadius, zRadius, xOffset, yOffset, zOffset, renderArea));
    }
}
