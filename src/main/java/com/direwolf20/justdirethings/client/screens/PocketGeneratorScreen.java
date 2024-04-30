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
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.Arrays;
import java.util.List;

public class PocketGeneratorScreen extends AbstractContainerScreen<PocketGeneratorContainer> {
    private final ResourceLocation GUI = new ResourceLocation(JustDireThings.MODID, "textures/gui/pocketgenerator.png");

    protected final PocketGeneratorContainer container;
    private ItemStack pocketGenerator;
    private IEnergyStorage energyStorage;

    public PocketGeneratorScreen(PocketGeneratorContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        this.container = container;
        this.pocketGenerator = container.playerEntity.getMainHandItem();
        this.energyStorage = pocketGenerator.getCapability(Capabilities.EnergyStorage.ITEM);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        if (mouseX > (leftPos + 7) && mouseX < (leftPos + 7) + 18 && mouseY > (topPos + 7) && mouseY < (topPos + 7) + 73) {
            int counter = pocketGenerator.getOrDefault(JustDireDataComponents.POCKETGEN_COUNTER, 0);
            int feBurnPerTick = 0;
            if (pocketGenerator.getItem() instanceof PocketGenerator pocketGeneratorItem) {
                feBurnPerTick = pocketGeneratorItem.getFePerFuelTick() * pocketGeneratorItem.getBurnSpeedMultiplier(pocketGenerator);
            }
            guiGraphics.renderTooltip(font, Language.getInstance().getVisualOrder(Arrays.asList(
                    Component.translatable("justdirethings.screen.energy", MagicHelpers.withSuffix(energyStorage.getEnergyStored()), MagicHelpers.withSuffix(energyStorage.getMaxEnergyStored())),
                    counter <= 0 ?
                            Component.translatable("justdirethings.screen.no_fuel") :
                            Component.translatable("justdirethings.screen.burn_time", MagicHelpers.ticksInSeconds(counter)),
                    counter > 0 ?
                            Component.translatable("justdirethings.screen.fepertick", MagicHelpers.formatted(feBurnPerTick)) :
                            Component.translatable("justdirethings.screen.fepertick", MagicHelpers.formatted(0))
            )), mouseX, mouseY);
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

    @Override
    protected void renderSlot(GuiGraphics pGuiGraphics, Slot pSlot) {
        super.renderSlot(pGuiGraphics, pSlot);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        //super.renderLabels(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

        this.pocketGenerator = container.playerEntity.getMainHandItem();
        if (pocketGenerator.isEmpty() || !(pocketGenerator.getItem() instanceof PocketGenerator))
            return;
        this.energyStorage = pocketGenerator.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage == null)
            return;
        int maxBurn = pocketGenerator.getOrDefault(JustDireDataComponents.POCKETGEN_MAXBURN, 0);
        int counter = pocketGenerator.getOrDefault(JustDireDataComponents.POCKETGEN_COUNTER, 0);
        int maxHeight = 13;
        if (maxBurn > 0) {
            int remaining = (counter * maxHeight) / maxBurn;
            guiGraphics.blit(GUI, leftPos + 80, topPos + 17 + 13 - remaining, 176, 13 - remaining, 14, remaining + 1);
        }

        int maxEnergy = energyStorage.getMaxEnergyStored();
        int height = 70;
        if (maxEnergy > 0) {
            int remaining = (energyStorage.getEnergyStored() * height) / maxEnergy;
            guiGraphics.blit(GUI, leftPos + 8, topPos + 78 - remaining, 176, 84 - remaining, 16, remaining + 1);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        InputConstants.Key mouseKey = InputConstants.getKey(p_keyPressed_1_, p_keyPressed_2_);
        if (p_keyPressed_1_ == 256 || minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            onClose();

            return true;
        }

        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public boolean mouseClicked(double x, double y, int btn) {
        return super.mouseClicked(x, y, btn);
    }

    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double pScrollX, double pScrollY) {
        return super.mouseScrolled(mouseX, mouseY, pScrollX, pScrollY);
    }

    private static MutableComponent getTrans(String key, Object... args) {
        return Component.translatable(JustDireThings.MODID + "." + key, args);
    }

}
