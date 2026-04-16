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
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class DropperT2Screen extends BaseMachineScreen<DropperT2Container> {
    private static final int LABEL_TEXT_ARGB = 0xFF404040;

    protected int dropCount;
    protected int pickupDelay;

    public DropperT2Screen(DropperT2Container container, Inventory inv, Component name) {
        super(container, inv, name);
        if (baseMachineBE instanceof DropperT2BE dropper) {
            this.dropCount = dropper.dropCount;
            this.pickupDelay = dropper.pickupDelay;
        }
    }

    public void addFilterButtons() {
        addRenderableWidget(ToggleButtonFactory.COMPARENBTBUTTON(leftPos + 8, topSectionTop + 62, filterData.compareNBT, b -> {
            filterData.compareNBT = !filterData.compareNBT;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
    }

    public void addAreaButtons() {
        addRenderableWidget(ToggleButtonFactory.RENDERAREABUTTON(leftPos + 152, topSectionTop + 62, renderArea, b -> {
            renderArea = !renderArea;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));

        valueButtonsList.add(new ValueButtons(leftPos + 25, topSectionTop + 12, xOffset, -ItemCollectorBE.maxOffset, ItemCollectorBE.maxOffset, font, (button, value) -> {
            xOffset = value;
            saveSettings();
        }));

        valueButtonsList.add(new ValueButtons(leftPos + 75, topSectionTop + 12, yOffset, -ItemCollectorBE.maxOffset, ItemCollectorBE.maxOffset, font, (button, value) -> {
            yOffset = value;
            saveSettings();
        }));

        valueButtonsList.add(new ValueButtons(leftPos + 125, topSectionTop + 12, zOffset, -ItemCollectorBE.maxOffset, ItemCollectorBE.maxOffset, font, (button, value) -> {
            zOffset = value;
            saveSettings();
        }));

        valueButtonsList.forEach(valueButtons -> valueButtons.widgetList.forEach(this::addRenderableWidget));
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.DIRECTIONBUTTON(leftPos + 122, topSectionTop + 38, direction, b -> {
            direction = ((ToggleButton) b).getTexturePosition();
            ClientPacketDistributor.sendToServer(new DirectionSettingPayload(direction));
        }));

        addRenderableWidget(new NumberButton(leftPos + 20, topSectionTop + 41, 24, 12, dropCount, 1, 64, Component.translatable("justdirethings.screen.dropcount"), b -> {
            dropCount = ((NumberButton) b).getValue();
            saveSettings();
        }));

        addRenderableWidget(ToggleButtonFactory.PICKUPDELAYBUTTON(leftPos + 20, topSectionTop + 27, pickupDelay, b -> {
            pickupDelay = ((NumberButton) b).getValue();
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
        ClientPacketDistributor.sendToServer(new DropperSettingPayload(dropCount, pickupDelay));
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        Component title = baseMachineBE.getBlockState().getBlock().getName();
        int titleX = topSectionLeft - leftPos + 20 + ((topSectionWidth - 40) / 2) - this.font.width(title) / 2;
        graphics.text(this.font, title, titleX, topSectionTop - topPos - 14, LABEL_TEXT_ARGB, false);
        if (baseMachineBE instanceof AreaAffectingBE) {
            int areaWidth = 158;
            int xStart = topSectionLeft + (topSectionWidth / 2) - (areaWidth / 2) - leftPos;
            graphics.text(this.font, Component.literal("Off"), xStart - 4, topSectionTop - topPos + 14, LABEL_TEXT_ARGB, false);
            graphics.text(this.font, Component.literal("X"), xStart + 35, topSectionTop - topPos + 4, LABEL_TEXT_ARGB, false);
            graphics.text(this.font, Component.literal("Y"), xStart + 85, topSectionTop - topPos + 4, LABEL_TEXT_ARGB, false);
            graphics.text(this.font, Component.literal("Z"), xStart + 135, topSectionTop - topPos + 4, LABEL_TEXT_ARGB, false);
        }
    }
}
