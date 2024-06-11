package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.GeneratorT1BE;
import com.direwolf20.justdirethings.common.blockentities.basebe.PoweredMachineBE;
import com.direwolf20.justdirethings.common.blocks.resources.CoalBlock_T1;
import com.direwolf20.justdirethings.common.containers.GeneratorFluidT1Container;
import com.direwolf20.justdirethings.common.items.FuelCanister;
import com.direwolf20.justdirethings.common.items.resources.Coal_T1;
import com.direwolf20.justdirethings.util.MagicHelpers;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.MiscTools;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Arrays;
import java.util.List;

public class GeneratorFluidT1Screen extends BaseMachineScreen<GeneratorFluidT1Container> {
    protected GeneratorFluidT1Container container;
    protected GeneratorT1BE generatorBE;

    public GeneratorFluidT1Screen(GeneratorFluidT1Container container, Inventory inv, Component name) {
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
            redstoneMode = MiscHelpers.RedstoneMode.values()[((ToggleButton) b).getTexturePosition()];
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

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            ItemStack fuelStack = this.hoveredSlot.getItem();
            int burnTime = fuelStack.getBurnTime(RecipeType.SMELTING);
            if (burnTime > 0) {
                int fuelBurnMultiplier = 1;
                if (fuelStack.getItem() instanceof Coal_T1 direCoal) {
                    fuelBurnMultiplier = direCoal.getBurnSpeedMultiplier();
                } else if (fuelStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof CoalBlock_T1 coalBlock) {
                    fuelBurnMultiplier = coalBlock.getBurnSpeedMultiplier();
                } else if (fuelStack.getItem() instanceof FuelCanister) {
                    fuelBurnMultiplier = FuelCanister.getBurnSpeedMultiplier(fuelStack);
                }
                List<Component> tooltip = this.getTooltipFromContainerItem(fuelStack);
                tooltip.add(Component.translatable("justdirethings.screen.burnspeedmultiplier", fuelBurnMultiplier).withStyle(ChatFormatting.RED));
                pGuiGraphics.renderTooltip(this.font, tooltip, fuelStack.getTooltipImage(), fuelStack, pX, pY);
                return;
            }
        }
        super.renderTooltip(pGuiGraphics, pX, pY);
    }
}
