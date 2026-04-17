package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.blocks.resources.CoalBlock_T1;
import com.direwolf20.justdirethings.common.containers.PocketGeneratorContainer;
import com.direwolf20.justdirethings.common.items.FuelCanister;
import com.direwolf20.justdirethings.common.items.PocketGenerator;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.resources.Coal_T1;
import com.direwolf20.justdirethings.util.MagicHelpers;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;

import java.util.ArrayList;
import java.util.List;

public class PocketGeneratorScreen extends AbstractContainerScreen<PocketGeneratorContainer> {
    private final Identifier GUI = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/pocketgenerator.png");

    protected final PocketGeneratorContainer container;
    private ItemStack pocketGenerator;
    private EnergyHandler energyStorage;

    public PocketGeneratorScreen(PocketGeneratorContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        this.container = container;
        this.pocketGenerator = container.playerEntity.getMainHandItem();
        this.energyStorage = pocketGenerator.getCapability(Capabilities.Energy.ITEM);
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractContents(graphics, mouseX, mouseY, partialTicks);
        if (energyStorage != null && mouseX > (leftPos + 7) && mouseX < (leftPos + 7) + 18 && mouseY > (topPos + 7) && mouseY < (topPos + 7) + 73) {
            int counter = pocketGenerator.getOrDefault(JustDireDataComponents.POCKETGEN_COUNTER, 0);
            int feBurnPerTick = 0;
            if (pocketGenerator.getItem() instanceof PocketGenerator pocketGeneratorItem) {
                feBurnPerTick = pocketGeneratorItem.getFePerFuelTick() * pocketGeneratorItem.getBurnSpeedMultiplier(pocketGenerator);
            }
            List<Component> lines = new ArrayList<>();
            if (Minecraft.getInstance().hasShiftDown()) {
                lines.add(Component.translatable("justdirethings.screen.energy", MagicHelpers.formatted(energyStorage.getAmountAsInt()), MagicHelpers.withSuffix(energyStorage.getCapacityAsInt())));
            } else {
                lines.add(Component.translatable("justdirethings.screen.energy", MagicHelpers.withSuffix(energyStorage.getAmountAsInt()), MagicHelpers.withSuffix(energyStorage.getCapacityAsInt())));
            }
            lines.add(counter <= 0
                    ? Component.translatable("justdirethings.screen.no_fuel")
                    : Component.translatable("justdirethings.screen.burn_time", MagicHelpers.ticksInSeconds(counter)));
            lines.add(counter > 0
                    ? Component.translatable("justdirethings.screen.fepertick", MagicHelpers.formatted(feBurnPerTick))
                    : Component.translatable("justdirethings.screen.fepertick", MagicHelpers.formatted(0)));
            graphics.setTooltipForNextFrame(font, lines, java.util.Optional.empty(), mouseX, mouseY);
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

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        //super.extractLabels(graphics, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI, relX, relY, 0.0F, 0.0F, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

        this.pocketGenerator = container.playerEntity.getMainHandItem();
        if (pocketGenerator.isEmpty() || !(pocketGenerator.getItem() instanceof PocketGenerator))
            return;
        this.energyStorage = pocketGenerator.getCapability(Capabilities.Energy.ITEM);
        if (energyStorage == null)
            return;
        int maxBurn = pocketGenerator.getOrDefault(JustDireDataComponents.POCKETGEN_MAXBURN, 0);
        int counter = pocketGenerator.getOrDefault(JustDireDataComponents.POCKETGEN_COUNTER, 0);
        int maxHeight = 13;
        if (maxBurn > 0) {
            int remaining = (counter * maxHeight) / maxBurn;
            graphics.blit(RenderPipelines.GUI_TEXTURED, GUI, leftPos + 80, topPos + 17 + 13 - remaining, 176.0F, 13.0F - remaining, 14, remaining + 1, this.imageWidth, this.imageHeight);
        }

        int maxEnergy = energyStorage.getCapacityAsInt();
        int height = 70;
        if (maxEnergy > 0) {
            int remaining = (energyStorage.getAmountAsInt() * height) / maxEnergy;
            graphics.blit(RenderPipelines.GUI_TEXTURED, GUI, leftPos + 8, topPos + 78 - remaining, 176.0F, 84.0F - remaining, 16, remaining + 1, this.imageWidth, this.imageHeight);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        InputConstants.Key mouseKey = InputConstants.getKey(event);
        if (event.key() == 256 || minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            onClose();
            return true;
        }
        return super.keyPressed(event);
    }
}
