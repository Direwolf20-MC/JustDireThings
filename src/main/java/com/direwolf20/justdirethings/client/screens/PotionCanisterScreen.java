package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.containers.PotionCanisterContainer;
import com.direwolf20.justdirethings.common.items.PotionCanister;
import com.direwolf20.justdirethings.util.MagicHelpers;
import com.direwolf20.justdirethings.util.MiscTools;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PotionCanisterScreen extends AbstractContainerScreen<PotionCanisterContainer> {
    private final Identifier GUI = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/fuelcanister.png");
    protected final Identifier FLUIDBAR = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/fluidbar.png");

    protected final PotionCanisterContainer container;
    private ItemStack potionCanister;

    public PotionCanisterScreen(PotionCanisterContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        this.container = container;
        this.potionCanister = container.potionCanister;
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);
        fluidBarTooltip(graphics, mouseX, mouseY);
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
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI, relX, relY, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        int offset = 5;
        graphics.blit(RenderPipelines.GUI_TEXTURED, FLUIDBAR, leftPos + offset, topPos + 5, 0.0F, 0.0F, 18, 72, 36, 72);
        int maxMB = PotionCanister.getMaxMB(), height = 70;
        if (maxMB > 0) {
            int remaining = (PotionCanister.getPotionAmount(potionCanister) * height) / maxMB;
            renderFluid(graphics, leftPos + offset + 1, topPos + 5 + 72 - 1, 16, remaining, potionCanister);
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, FLUIDBAR, leftPos + offset, topPos + 5, 18.0F, 0.0F, 18, 72, 36, 72);
    }

    public void renderFluid(GuiGraphicsExtractor graphics, int startX, int startY, int width, int height, ItemStack potionCanister) {
        PotionContents potionContents = PotionCanister.getPotionContents(potionCanister);
        if (potionContents.equals(PotionContents.EMPTY) || PotionCanister.getPotionAmount(potionCanister) <= 0) return;
        if (height <= 0) return;

        net.minecraft.world.level.material.FluidState waterState = net.minecraft.world.level.material.Fluids.WATER.defaultFluidState();
        net.minecraft.client.renderer.block.FluidModel fluidModel =
                net.minecraft.client.Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(waterState);
        net.minecraft.client.renderer.texture.TextureAtlasSprite sprite = fluidModel.stillMaterial().sprite();

        int tint = potionContents.getColor() | 0xFF000000;

        int tileSize = 16;
        int remaining = height;
        int y = startY;
        while (remaining > 0) {
            int drawHeight = Math.min(tileSize, remaining);
            int drawY = y - drawHeight;
            if (drawHeight == tileSize) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, startX, drawY, width, drawHeight, tint);
            } else {
                float u0 = sprite.getU0();
                float u1 = sprite.getU1();
                float v0 = sprite.getV0();
                float v1 = sprite.getV0() + (sprite.getV1() - sprite.getV0()) * drawHeight / (float) tileSize;
                graphics.blit(sprite.atlasLocation(), startX, drawY,
                        startX + width, drawY + drawHeight, u0, u1, v0, v1);
            }
            remaining -= drawHeight;
            y -= drawHeight;
        }
    }

    public void fluidBarTooltip(GuiGraphicsExtractor graphics, int pX, int pY) {
        PotionContents potionContents = PotionCanister.getPotionContents(potionCanister);
        int potionAmt = PotionCanister.getPotionAmount(potionCanister);
        if (potionAmt == 0 || potionContents.equals(PotionContents.EMPTY)) return;
        if (MiscTools.inBounds(leftPos + 5, topPos + 5, 18, 72, pX, pY)) {
            List<Component> components = new ArrayList<>();
            components.add(Component.literal(MagicHelpers.formatted(potionAmt) + "/" + MagicHelpers.formatted(PotionCanister.getMaxMB())));
            PotionContents.addPotionTooltip(potionContents.getAllEffects(), components::add, 1, 20);
            graphics.setTooltipForNextFrame(font, components, Optional.empty(), pX, pY);
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
