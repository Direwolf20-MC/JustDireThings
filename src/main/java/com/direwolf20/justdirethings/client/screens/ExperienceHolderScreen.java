package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.client.screens.widgets.NumberButton;
import com.direwolf20.justdirethings.common.blockentities.ExperienceHolderBE;
import com.direwolf20.justdirethings.common.containers.ExperienceHolderContainer;
import com.direwolf20.justdirethings.common.network.data.ExperienceHolderPayload;
import com.direwolf20.justdirethings.common.network.data.ExperienceHolderSettingsPayload;
import com.direwolf20.justdirethings.util.ExperienceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class ExperienceHolderScreen extends BaseMachineScreen<ExperienceHolderContainer> {
    private static final int LEVEL_OUTLINE_ARGB = 0xFF000000;
    private static final int LEVEL_TEXT_ARGB = 0xFF80FF20;

    private ExperienceHolderBE experienceHolderBE;
    private int exp;
    private int targetExp;
    private boolean ownerOnly;
    private boolean collectExp;
    public boolean showParticles = true;
    private static final Identifier EXPERIENCE_BAR_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/experience_bar_background");
    private static final Identifier EXPERIENCE_BAR_PROGRESS_SPRITE = Identifier.withDefaultNamespace("hud/experience_bar_progress");

    public ExperienceHolderScreen(ExperienceHolderContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        if (container.baseMachineBE instanceof ExperienceHolderBE experienceHolderBE) {
            this.experienceHolderBE = experienceHolderBE;
            this.exp = experienceHolderBE.exp;
            this.targetExp = experienceHolderBE.targetExp;
            this.ownerOnly = experienceHolderBE.ownerOnly;
            this.collectExp = experienceHolderBE.collectExp;
            this.showParticles = experienceHolderBE.showParticles;
        }
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.STOREEXPBUTTON(topSectionLeft + (topSectionWidth / 2) + 15, topSectionTop + 62, true, b -> {
            int amt = 1;
            Minecraft mc = Minecraft.getInstance();
            if (mc.hasControlDown())
                amt = -1;
            else if (mc.hasShiftDown())
                amt = amt * 10;
            ClientPacketDistributor.sendToServer(new ExperienceHolderPayload(true, amt));
        }));
        addRenderableWidget(ToggleButtonFactory.EXTRACTEXPBUTTON(topSectionLeft + (topSectionWidth / 2) - 15 - 18, topSectionTop + 62, true, b -> {
            int amt = 1;
            Minecraft mc = Minecraft.getInstance();
            if (mc.hasControlDown())
                amt = -1;
            else if (mc.hasShiftDown())
                amt = amt * 10;
            ClientPacketDistributor.sendToServer(new ExperienceHolderPayload(false, amt));
        }));
        addRenderableWidget(ToggleButtonFactory.TARGETEXPBUTTON(topSectionLeft + (topSectionWidth / 2) - 15 - 42, topSectionTop + 64, targetExp, b -> {
            targetExp = ((NumberButton) b).getValue();
            saveSettings();
        }));
        addRenderableWidget(ToggleButtonFactory.OWNERONLYBUTTON(topSectionLeft + (topSectionWidth / 2) - 15 - 60, topSectionTop + 62, ownerOnly, b -> {
            ownerOnly = !ownerOnly;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
        addRenderableWidget(ToggleButtonFactory.COLLECTEXPBUTTON(topSectionLeft + (topSectionWidth / 2) + 15, topSectionTop + 42, collectExp, b -> {
            collectExp = !collectExp;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
        addRenderableWidget(ToggleButtonFactory.SHOWPARTICLESBUTTON(topSectionLeft + (topSectionWidth / 2) + 31, topSectionTop + 42, showParticles, b -> {
            showParticles = !showParticles;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        renderXPBar(graphics);
    }

    public void renderXPBar(GuiGraphicsExtractor graphics) {
        int barX = topSectionLeft + (topSectionWidth / 2) - (182 / 2);
        int barY = topSectionTop + topSectionHeight - 15;

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EXPERIENCE_BAR_BACKGROUND_SPRITE, barX, barY, 182, 5);
        int partialAmount = (int) (ExperienceUtils.getProgressToNextLevel(experienceHolderBE.exp) * 183.0F);
        if (partialAmount > 0) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EXPERIENCE_BAR_PROGRESS_SPRITE, 182, 5, 0, 0, barX, barY, partialAmount, 5);
        }
        String s = String.valueOf(ExperienceUtils.getLevelFromTotalExperience(experienceHolderBE.exp));
        int j = topSectionLeft + (topSectionWidth / 2) - font.width(s) / 2;
        int k = topSectionTop + 62 + (font.lineHeight / 2);
        graphics.text(font, s, j + 1, k, LEVEL_OUTLINE_ARGB, false);
        graphics.text(font, s, j - 1, k, LEVEL_OUTLINE_ARGB, false);
        graphics.text(font, s, j, k + 1, LEVEL_OUTLINE_ARGB, false);
        graphics.text(font, s, j, k - 1, LEVEL_OUTLINE_ARGB, false);
        graphics.text(font, s, j, k, LEVEL_TEXT_ARGB, false);
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
        ClientPacketDistributor.sendToServer(new ExperienceHolderSettingsPayload(targetExp, ownerOnly, collectExp, showParticles));
    }
}
