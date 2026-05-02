package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.JustDireThings;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ParadoxMachineScreen extends BaseMachineScreen<ParadoxMachineContainer> {
    protected static final Identifier PARADOXBAR = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/paradoxbar.png");
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
        addRenderableWidget(ToggleButtonFactory.SNAPSHOT_AREA_BUTTON(leftPos + 116, topSectionTop + 62, b -> {
            ClientPacketDistributor.sendToServer(new ParadoxMachineSnapshotPayload());
        }));
    }

    public void addRenderButton() {
        addRenderableWidget(ToggleButtonFactory.RENDERPARADOXBUTTON(leftPos + 98, topSectionTop + 62, renderParadox, b -> {
            renderParadox = !renderParadox;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
    }

    public void addTargetButton() {
        addRenderableWidget(ToggleButtonFactory.PARADOXTARGETBUTTON(leftPos + 56, topSectionTop + 38, targetType, b -> {
            targetType = ((ToggleButton) b).getTexturePosition();
            saveSettings();
        }));
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
        ClientPacketDistributor.sendToServer(new ParadoxRenderPayload(renderParadox, targetType));
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        if (baseMachineBE instanceof ParadoxMachineBE paradoxMachineBE) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, PARADOXBAR, topSectionLeft + topSectionWidth - 18 - 5, topSectionTop + 5, 0.0F, 0.0F, 18, 72, 36, 72);
            float maxEnergy = paradoxMachineBE.getMaxParadoxEnergy(), height = 70;
            if (maxEnergy > 0 && paradoxMachineBE.paradoxEnergy > 0) {
                float remaining = (paradoxMachineBE.paradoxEnergy * height) / maxEnergy;
                long time = System.currentTimeMillis();
                int rgb = Color.HSBtoRGB((time % 10800L) / 10800.0f, 1.0f, 1.0f);
                int rainbowArgb = 0xFF000000 | (rgb & 0x00FFFFFF);
                graphics.blit(RenderPipelines.GUI_TEXTURED, PARADOXBAR,
                        topSectionLeft + topSectionWidth - 18 - 5 + 1,
                        topSectionTop + getEnergyBarOffset() + 72 - 2 - (int) remaining,
                        19.0F, 69.0F - remaining, 17, (int) remaining + 1, 17, (int) remaining + 1, 36, 72, rainbowArgb);
            }
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);
        paradoxBarTooltip(graphics, mouseX, mouseY);
    }

    public void paradoxBarTooltip(GuiGraphicsExtractor graphics, int pX, int pY) {
        if (baseMachineBE instanceof ParadoxMachineBE paradoxMachineBE) {
            if (MiscTools.inBounds(topSectionLeft + topSectionWidth - 18 - 5, topSectionTop + 5, 18, 72, pX, pY)) {
                String energyFormatted = String.format("%.2f", paradoxMachineBE.paradoxEnergy);
                graphics.setTooltipForNextFrame(font,
                        Component.translatable("justdirethings.paradoxenergy", energyFormatted, paradoxMachineBE.getMaxParadoxEnergy()),
                        pX, pY);
            }
        }
    }

    @Override
    public void powerBarTooltip(GuiGraphicsExtractor graphics, int pX, int pY) {
        if (baseMachineBE instanceof ParadoxMachineBE paradoxMachineBE && baseMachineBE instanceof PoweredMachineBE poweredMachineBE) {
            if (MiscTools.inBounds(topSectionLeft + 5, topSectionTop + 5, 18, 72, pX, pY)) {
                int totalBlocks = paradoxMachineBE.testRestoreBlocks().size();
                int totalEntities = paradoxMachineBE.getEntitiesFromNBT().size();
                int totalCostEnergy = paradoxMachineBE.getEnergyCost(totalBlocks, totalEntities);
                List<Component> lines = new ArrayList<>();
                if (Minecraft.getInstance().hasShiftDown()) {
                    lines.add(Component.translatable("justdirethings.screen.energy", MagicHelpers.formatted(this.container.getEnergy()), MagicHelpers.formatted(poweredMachineBE.getMaxEnergy())));
                    lines.add(Component.translatable("justdirethings.screen.paradoxenergycost", MagicHelpers.formatted(totalCostEnergy)));
                } else {
                    lines.add(Component.translatable("justdirethings.screen.energy", MagicHelpers.withSuffix(this.container.getEnergy()), MagicHelpers.withSuffix(poweredMachineBE.getMaxEnergy())));
                    lines.add(Component.translatable("justdirethings.screen.paradoxenergycost", MagicHelpers.withSuffix(totalCostEnergy)));
                }
                graphics.setTooltipForNextFrame(font, lines, java.util.Optional.empty(), pX, pY);
            }
        }
    }

    @Override
    public void fluidBarTooltip(GuiGraphicsExtractor graphics, int pX, int pY) {
        if (baseMachineBE instanceof ParadoxMachineBE paradoxMachineBE && baseMachineBE instanceof FluidMachineBE fluidMachineBE) {
            if (MiscTools.inBounds(topSectionLeft + getFluidBarOffset(), topSectionTop + 5, 18, 72, pX, pY)) {
                int totalBlocks = paradoxMachineBE.testRestoreBlocks().size();
                int totalEntities = paradoxMachineBE.getEntitiesFromNBT().size();
                int totalCostFluid = paradoxMachineBE.getFluidCost(totalBlocks, totalEntities);
                List<Component> lines = new ArrayList<>();
                if (Minecraft.getInstance().hasShiftDown()) {
                    lines.add(Component.translatable("justdirethings.screen.fluid", this.container.getFluidStack().getHoverName(), MagicHelpers.formatted(this.container.getFluidAmount()), MagicHelpers.formatted(fluidMachineBE.getMaxMB())));
                    lines.add(Component.translatable("justdirethings.screen.paradoxfluidcost", MagicHelpers.formatted(totalCostFluid)));
                } else {
                    lines.add(Component.translatable("justdirethings.screen.fluid", this.container.getFluidStack().getHoverName(), MagicHelpers.withSuffix(this.container.getFluidAmount()), MagicHelpers.withSuffix(fluidMachineBE.getMaxMB())));
                    lines.add(Component.translatable("justdirethings.screen.paradoxfluidcost", MagicHelpers.withSuffix(totalCostFluid)));
                }
                graphics.setTooltipForNextFrame(font, lines, java.util.Optional.empty(), pX, pY);
            }
        }
    }
}
