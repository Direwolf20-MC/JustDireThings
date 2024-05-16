package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.BaseButton;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.common.items.MachineSettingsCopier;
import com.direwolf20.justdirethings.common.network.data.CopyMachineSettingsPayload;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class MachineSettingsCopierScreen extends Screen {
    public static final ResourceLocation SOCIALBACKGROUND = new ResourceLocation(JustDireThings.MODID, "background");
    private ItemStack copyMachineSettingsItemstack;
    boolean area, offset, filter, redstone;
    int topSectionWidth, topSectionHeight, topSectionLeft, topSectionTop;

    public MachineSettingsCopierScreen(ItemStack itemStack) {
        super(Component.literal(""));
        this.copyMachineSettingsItemstack = itemStack;
        area = MachineSettingsCopier.getCopyArea(itemStack);
        offset = MachineSettingsCopier.getCopyOffset(itemStack);
        filter = MachineSettingsCopier.getCopyFilter(itemStack);
        redstone = MachineSettingsCopier.getCopyRedstone(itemStack);
    }

    @Override
    public void init() {
        super.init();
        setPositions();
        addRenderableWidget(ToggleButtonFactory.COPY_AREA_BUTTON((topSectionLeft + topSectionWidth / 2) - 40, topSectionTop + 12, area, b -> {
            area = !area;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));

        addRenderableWidget(ToggleButtonFactory.COPY_OFFSET_BUTTON((topSectionLeft + topSectionWidth / 2) + 24, topSectionTop + 12, offset, b -> {
            offset = !offset;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));

        addRenderableWidget(ToggleButtonFactory.COPY_FILTER_BUTTON((topSectionLeft + topSectionWidth / 2) - 40, topSectionTop + 32, filter, b -> {
            filter = !filter;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));

        addRenderableWidget(ToggleButtonFactory.COPY_REDSTONE_BUTTON((topSectionLeft + topSectionWidth / 2) + 24, topSectionTop + 32, redstone, b -> {
            redstone = !redstone;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));

    }

    public int getGuiLeft() {
        return (this.width - 176) / 2;
    }

    public int getGuiHeight() {
        return (this.height - 166) / 2;
    }

    public void setPositions() {
        topSectionWidth = 140;
        topSectionHeight = 60;
        topSectionLeft = getGuiLeft() - 20 / 2;
        topSectionTop = getGuiHeight();
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        guiGraphics.blitSprite(SOCIALBACKGROUND, topSectionLeft, topSectionTop - 20, topSectionWidth, 20);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        guiGraphics.blitSprite(SOCIALBACKGROUND, topSectionLeft, topSectionTop, topSectionWidth, topSectionHeight);

        Component title = copyMachineSettingsItemstack.getItem().getName(copyMachineSettingsItemstack);
        int titleX = topSectionLeft + 20 + ((topSectionWidth - 40) / 2) - this.font.width(title) / 2;
        guiGraphics.drawString(this.font, title, titleX, topSectionTop - 14, 4210752, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        for (Renderable renderable : this.renderables) {
            if (renderable instanceof BaseButton button && !button.getLocalization(mouseX, mouseY).equals(Component.empty()))
                guiGraphics.renderTooltip(font, button.getLocalization(mouseX, mouseY), mouseX, mouseY);
        }
    }

    public void saveSettings() {
        PacketDistributor.sendToServer(new CopyMachineSettingsPayload(area, offset, filter, redstone));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        InputConstants.Key mouseKey = InputConstants.getKey(pKeyCode, pScanCode);
        if (super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
            return true;
        } else if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            this.onClose();
            return true;
        }
        return true;
    }

}
