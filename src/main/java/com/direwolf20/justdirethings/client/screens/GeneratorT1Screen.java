package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.GeneratorT1BE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.direwolf20.justdirethings.common.blocks.resources.CoalBlock_T1;
import com.direwolf20.justdirethings.common.containers.GeneratorT1Container;
import com.direwolf20.justdirethings.common.items.FuelCanister;
import com.direwolf20.justdirethings.common.items.resources.Coal_T1;
import com.direwolf20.justdirethings.util.MagicHelpers;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.MiscTools;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;

import java.util.ArrayList;
import java.util.List;

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
        addRenderableWidget(ToggleButtonFactory.REDSTONEBUTTON(leftPos + 104, topSectionTop + 38, redstoneMode.ordinal(), b -> {
            redstoneMode = MiscHelpers.RedstoneMode.values()[((ToggleButton) b).getTexturePosition()];
            saveSettings();
        }));
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        graphics.blit(RenderPipelines.GUI_TEXTURED, JUSTSLOT, leftPos + 79, topPos + 30, 0.0F, 18.0F, 18, 18, 256, 256);
        int maxBurn = container.getMaxBurn();
        int burnRemaining = container.getBurnRemaining();
        int maxHeight = 18;
        if (maxBurn > 0) {
            int remaining = (burnRemaining * maxHeight) / maxBurn;
            graphics.blit(RenderPipelines.GUI_TEXTURED, JUSTSLOT, leftPos + 79, topPos + 30 + 18 - remaining, 18.0F, 36.0F - remaining, 18, remaining + 3, 256, 256);
        }
    }

    @Override
    public void powerBarTooltip(GuiGraphicsExtractor graphics, int pX, int pY) {
        if (baseMachineBE instanceof PoweredMachineBE poweredMachineBE) {
            if (MiscTools.inBounds(topSectionLeft + 5, topSectionTop + 5, 18, 72, pX, pY)) {
                int burnRemaining = this.container.getBurnRemaining();
                List<Component> lines = new ArrayList<>();
                if (Minecraft.getInstance().hasShiftDown()) {
                    lines.add(Component.translatable("justdirethings.screen.energy", MagicHelpers.formatted(this.container.getEnergy()), MagicHelpers.formatted(poweredMachineBE.getMaxEnergy())));
                } else {
                    lines.add(Component.translatable("justdirethings.screen.energy", MagicHelpers.withSuffix(this.container.getEnergy()), MagicHelpers.withSuffix(poweredMachineBE.getMaxEnergy())));
                }
                lines.add(burnRemaining > 0
                        ? Component.translatable("justdirethings.screen.fepertick", MagicHelpers.formatted(generatorBE.fePerTick()))
                        : Component.translatable("justdirethings.screen.fepertick", MagicHelpers.formatted(0)));
                lines.add(burnRemaining <= 0
                        ? Component.translatable("justdirethings.screen.no_fuel")
                        : Component.translatable("justdirethings.screen.burn_time", MagicHelpers.ticksInSeconds(burnRemaining)));
                graphics.setTooltipForNextFrame(font, lines, java.util.Optional.empty(), pX, pY);
            }
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            ItemStack fuelStack = this.hoveredSlot.getItem();
            FuelValues fuelValues = minecraft != null && minecraft.level != null ? minecraft.level.fuelValues() : null;
            int burnTime = fuelValues != null ? fuelStack.getBurnTime(RecipeType.SMELTING, fuelValues) : 0;
            if (burnTime > 0) {
                int fuelBurnMultiplier = 1;
                if (fuelStack.getItem() instanceof Coal_T1 direCoal) {
                    fuelBurnMultiplier = direCoal.getBurnSpeedMultiplier();
                } else if (fuelStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof CoalBlock_T1 coalBlock) {
                    fuelBurnMultiplier = coalBlock.getBurnSpeedMultiplier();
                } else if (fuelStack.getItem() instanceof FuelCanister) {
                    fuelBurnMultiplier = FuelCanister.getBurnSpeedMultiplier(fuelStack);
                }
                List<Component> tooltip = new ArrayList<>(this.getTooltipFromContainerItem(fuelStack));
                tooltip.add(Component.translatable("justdirethings.screen.burnspeedmultiplier", fuelBurnMultiplier).withStyle(ChatFormatting.RED));
                graphics.setTooltipForNextFrame(this.font, tooltip, fuelStack.getTooltipImage(), fuelStack, mouseX, mouseY);
                return;
            }
        }
        super.extractTooltip(graphics, mouseX, mouseY);
    }
}
