package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.ParadoxMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.FluidMachineBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.direwolf20.justdirethings.common.containers.ParadoxMachineContainer;
import com.direwolf20.justdirethings.common.network.data.ParadoxMachineSnapshotPayload;
import com.direwolf20.justdirethings.common.network.data.ParadoxRenderPayload;
import com.direwolf20.justdirethings.util.MagicHelpers;
import com.direwolf20.justdirethings.util.MiscTools;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Arrays;

public class ParadoxMachineScreen extends BaseMachineScreen<ParadoxMachineContainer> {
    private boolean renderParadox = false;
    private int targetType = 0;
    public ParadoxMachineScreen(ParadoxMachineContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        if (container.baseMachineBE instanceof ParadoxMachineBE paradoxMachineBE) {
            renderParadox = paradoxMachineBE.renderParadox;
            targetType = paradoxMachineBE.targetType;
        }
    }

    @Override
    public void init() {
        super.init();
        addSnapshotButton();
        addRenderButton();
        addTargetButton();
    }

    @Override
    public void setTopSection() {
        extraWidth = 80;
        extraHeight = 0;
    }

    public void addSnapshotButton() {
        addRenderableWidget(ToggleButtonFactory.SNAPSHOT_AREA_BUTTON(getGuiLeft() + 116, topSectionTop + 62, b -> {
            PacketDistributor.sendToServer(new ParadoxMachineSnapshotPayload());
        }));
    }

    public void addRenderButton() {
        addRenderableWidget(ToggleButtonFactory.RENDERPARADOXBUTTON(getGuiLeft() + 98, topSectionTop + 62, renderParadox, b -> {
            renderParadox = !renderParadox;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
    }

    public void addTargetButton() {
        addRenderableWidget(ToggleButtonFactory.PARADOXTARGETBUTTON(getGuiLeft() + 56, topSectionTop + 38, targetType, b -> {
            targetType = ((ToggleButton) b).getTexturePosition();
            saveSettings();
        }));
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
        PacketDistributor.sendToServer(new ParadoxRenderPayload(renderParadox, targetType));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
        if (baseMachineBE instanceof ParadoxMachineBE paradoxMachineBE) {
            guiGraphics.blit(POWERBAR, topSectionLeft + topSectionWidth - 18 - 5, topSectionTop + 5, 0, 0, 18, 72, 36, 72);
            float maxEnergy = paradoxMachineBE.getMaxParadoxEnergy(), height = 70;
            if (maxEnergy > 0) {
                float remaining = (paradoxMachineBE.paradoxEnergy * height) / maxEnergy;
                guiGraphics.blit(POWERBAR, topSectionLeft + topSectionWidth - 18 - 5 + 1, topSectionTop + getEnergyBarOffset() + 72 - 2 - (int) remaining, 19, 69 - remaining, 17, (int) remaining + 1, 36, 72);
            }
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
        paradoxBarTooltip(pGuiGraphics, pX, pY);
    }

    public void paradoxBarTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        if (baseMachineBE instanceof ParadoxMachineBE paradoxMachineBE) {
            if (MiscTools.inBounds(topSectionLeft + topSectionWidth - 18 - 5, topSectionTop + 5, 18, 72, pX, pY)) {
                pGuiGraphics.renderTooltip(font, Language.getInstance().getVisualOrder(Arrays.asList(
                        Component.translatable("justdirethings.paradoxenergy", paradoxMachineBE.paradoxEnergy, paradoxMachineBE.getMaxParadoxEnergy())
                )), pX, pY);
            }
        }
    }

    @Override
    public void powerBarTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        if (baseMachineBE instanceof ParadoxMachineBE paradoxMachineBE && baseMachineBE instanceof PoweredMachineBE poweredMachineBE) {
            if (MiscTools.inBounds(topSectionLeft + 5, topSectionTop + 5, 18, 72, pX, pY)) {
                int totalBlocks = paradoxMachineBE.testRestoreBlocks().size();
                int totalEntities = paradoxMachineBE.getEntitiesFromNBT().size();
                int totalCostEnergy = paradoxMachineBE.getEnergyCost(totalBlocks, totalEntities);
                if (hasShiftDown())
                    pGuiGraphics.renderTooltip(font, Language.getInstance().getVisualOrder(Arrays.asList(
                            Component.translatable("justdirethings.screen.energy", MagicHelpers.formatted(this.container.getEnergy()), MagicHelpers.formatted(poweredMachineBE.getMaxEnergy())),
                            Component.translatable("justdirethings.screen.paradoxenergycost", MagicHelpers.formatted(totalCostEnergy))
                    )), pX, pY);
                else
                    pGuiGraphics.renderTooltip(font, Language.getInstance().getVisualOrder(Arrays.asList(
                            Component.translatable("justdirethings.screen.energy", MagicHelpers.withSuffix(this.container.getEnergy()), MagicHelpers.withSuffix(poweredMachineBE.getMaxEnergy())),
                            Component.translatable("justdirethings.screen.paradoxenergycost", MagicHelpers.withSuffix(totalCostEnergy))
                    )), pX, pY);
            }
        }
    }

    @Override
    public void fluidBarTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        if (baseMachineBE instanceof ParadoxMachineBE paradoxMachineBE && baseMachineBE instanceof FluidMachineBE fluidMachineBE) {
            if (MiscTools.inBounds(topSectionLeft + getFluidBarOffset(), topSectionTop + 5, 18, 72, pX, pY)) {
                int totalBlocks = paradoxMachineBE.testRestoreBlocks().size();
                int totalEntities = paradoxMachineBE.getEntitiesFromNBT().size();
                int totalCostFluid = paradoxMachineBE.getFluidCost(totalBlocks, totalEntities);
                if (hasShiftDown())
                    pGuiGraphics.renderTooltip(font, Language.getInstance().getVisualOrder(Arrays.asList(
                            Component.translatable("justdirethings.screen.fluid", this.container.getFluidStack().getHoverName(), MagicHelpers.formatted(this.container.getFluidAmount()), MagicHelpers.formatted(fluidMachineBE.getMaxMB())),
                            Component.translatable("justdirethings.screen.paradoxfluidcost", MagicHelpers.formatted(totalCostFluid))
                    )), pX, pY);
                else
                    pGuiGraphics.renderTooltip(font, Language.getInstance().getVisualOrder(Arrays.asList(
                            Component.translatable("justdirethings.screen.fluid", this.container.getFluidStack().getHoverName(), MagicHelpers.withSuffix(this.container.getFluidAmount()), MagicHelpers.withSuffix(fluidMachineBE.getMaxMB())),
                            Component.translatable("justdirethings.screen.paradoxfluidcost", MagicHelpers.withSuffix(totalCostFluid))
                    )), pX, pY);
            }
        }
    }
}
