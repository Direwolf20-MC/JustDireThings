package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.common.blockentities.ExperienceHolderBE;
import com.direwolf20.justdirethings.common.containers.ExperienceHolderContainer;
import com.direwolf20.justdirethings.common.network.data.ExperienceHolderPayload;
import com.direwolf20.justdirethings.util.ExperienceUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class ExperienceHolderScreen extends BaseMachineScreen<ExperienceHolderContainer> {
    private ExperienceHolderBE experienceHolderBE;
    private int exp;
    private StringWidget expValue;
    private static final ResourceLocation EXPERIENCE_BAR_BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("hud/experience_bar_background");
    private static final ResourceLocation EXPERIENCE_BAR_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("hud/experience_bar_progress");

    public ExperienceHolderScreen(ExperienceHolderContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        if (container.baseMachineBE instanceof ExperienceHolderBE experienceHolderBE) {
            this.experienceHolderBE = experienceHolderBE;
            this.exp = experienceHolderBE.exp;
        }
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.STOREEXPBUTTON(topSectionLeft + (topSectionWidth / 2) - 15 - 18, topSectionTop + 62, true, b -> {
            int amt = 1;
            if (Screen.hasControlDown())
                amt = -1;
            else if (Screen.hasShiftDown())
                amt = amt * 10;
            PacketDistributor.sendToServer(new ExperienceHolderPayload(true, amt));
        }));
        addRenderableWidget(ToggleButtonFactory.EXTRACTEXPBUTTON(topSectionLeft + (topSectionWidth / 2) + 15, topSectionTop + 62, true, b -> {
            int amt = 1;
            if (Screen.hasControlDown())
                amt = -1;
            else if (Screen.hasShiftDown())
                amt = amt * 10;
            PacketDistributor.sendToServer(new ExperienceHolderPayload(false, amt));
        }));
        expValue = new StringWidget(topSectionLeft + (topSectionWidth / 2) - 15, topSectionTop + 62 + (font.lineHeight / 2), 30, font.lineHeight, Component.literal(String.valueOf(ExperienceUtils.getLevelFromTotalExperience(experienceHolderBE.exp))), font);

        addRenderableWidget(expValue);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        updateDisplay();
        renderXPAmount(guiGraphics, partialTick, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
        renderXPBar(guiGraphics, partialTicks, mouseX, mouseY);
    }

    public void renderXPBar(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        int barX = topSectionLeft + (topSectionWidth / 2) - (182 / 2);  // Position for the XP bar in your GUI
        int barY = topSectionTop + topSectionHeight - 15;  // Y position for the XP bar

        // Bind the vanilla experience bar texture (this is the same texture used by the player XP bar)
        guiGraphics.blitSprite(EXPERIENCE_BAR_BACKGROUND_SPRITE, barX, barY, 182, 5);
        int partialAmount = (int) (ExperienceUtils.getProgressToNextLevel(experienceHolderBE.exp) * 183.0F);
        if (partialAmount > 0) {
            guiGraphics.blitSprite(EXPERIENCE_BAR_PROGRESS_SPRITE, 182, 5, 0, 0, barX, barY, partialAmount, 5);
        }
    }

    public void renderXPAmount(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {

    }

    private void updateDisplay() {
        this.expValue.setMessage(Component.literal(String.valueOf(ExperienceUtils.getLevelFromTotalExperience(experienceHolderBE.exp))));
    }
}
