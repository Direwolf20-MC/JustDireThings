package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.GeneratorT1BE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.direwolf20.justdirethings.common.containers.GeneratorT1Container;
import com.direwolf20.justdirethings.util.MagicHelpers;
import com.direwolf20.justdirethings.util.MiscTools;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.Arrays;

public class GeneratorT1Screen extends BaseMachineScreen<GeneratorT1Container> {
    protected GeneratorT1Container container;
    protected GeneratorT1BE generatorBE;

    public GeneratorT1Screen(GeneratorT1Container container, Inventory inv, Component name) {
        super(container, inv, name);
        this.container = container;
        if (container.baseMachineBE instanceof GeneratorT1BE generatorT1BE) {
            this.generatorBE = generatorT1BE;
        }
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void setTopSection() {
        extraWidth = 0;
        extraHeight = 0;
    }

    @Override
    public void addTickSpeedButton() {
        //No-Op
    }

    @Override
    public void addRedstoneButtons() {
        addRenderableWidget(ToggleButtonFactory.REDSTONEBUTTON(getGuiLeft() + 104, topSectionTop + 38, redstoneMode.ordinal(), b -> {
            redstoneMode = redstoneMode.next();
            ((ToggleButton) b).nextTexturePosition();
            saveSettings();
        }));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
        guiGraphics.blit(JUSTSLOT, getGuiLeft() + 79, getGuiTop() + 30, 0, 18, 18, 18);
        int maxBurn = container.getMaxBurn();
        int burnRemaining = container.getBurnRemaining();
        int maxHeight = 18;
        if (maxBurn > 0) {
            int remaining = (burnRemaining * maxHeight) / maxBurn;
            guiGraphics.blit(JUSTSLOT, getGuiLeft() + 79, getGuiTop() + 30 + 18 - remaining, 18, 36 - remaining, 18, remaining + 3);
        }
    }

    @Override
    public void powerBarTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        if (baseMachineBE instanceof PoweredMachineBE poweredMachineBE) {
            if (MiscTools.inBounds(topSectionLeft + 5, topSectionTop + 5, 18, 72, pX, pY)) {
                int burnRemaining = this.container.getBurnRemaining();
                if (hasShiftDown())
                    pGuiGraphics.renderTooltip(font, Language.getInstance().getVisualOrder(Arrays.asList(
                            Component.translatable("justdirethings.screen.energy", MagicHelpers.formatted(this.container.getEnergy()), MagicHelpers.formatted(poweredMachineBE.getMaxEnergy())),
                            burnRemaining > 0 ?
                                    Component.translatable("justdirethings.screen.fepertick", MagicHelpers.formatted(generatorBE.fePerTick())) :
                                    Component.translatable("justdirethings.screen.fepertick", MagicHelpers.formatted(0)),
                            burnRemaining <= 0 ?
                                    Component.translatable("justdirethings.screen.no_fuel") :
                                    Component.translatable("justdirethings.screen.burn_time", MagicHelpers.ticksInSeconds(burnRemaining))
                    )), pX, pY);
                else
                    pGuiGraphics.renderTooltip(font, Language.getInstance().getVisualOrder(Arrays.asList(
                            Component.translatable("justdirethings.screen.energy", MagicHelpers.withSuffix(this.container.getEnergy()), MagicHelpers.withSuffix(poweredMachineBE.getMaxEnergy())),
                            burnRemaining > 0 ?
                                    Component.translatable("justdirethings.screen.fepertick", MagicHelpers.formatted(generatorBE.fePerTick())) :
                                    Component.translatable("justdirethings.screen.fepertick", MagicHelpers.formatted(0)),
                            burnRemaining <= 0 ?
                                    Component.translatable("justdirethings.screen.no_fuel") :
                                    Component.translatable("justdirethings.screen.burn_time", MagicHelpers.ticksInSeconds(burnRemaining))
                    )), pX, pY);
            }
        }
    }
}
