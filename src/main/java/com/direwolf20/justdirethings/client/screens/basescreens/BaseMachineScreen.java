package com.direwolf20.justdirethings.client.screens.basescreens;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ValueButtons;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ValueButtonsDouble;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.client.screens.widgets.NumberButton;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.ItemCollectorBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.*;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.containers.slots.FilterBasicSlot;
import com.direwolf20.justdirethings.common.network.data.*;
import com.direwolf20.justdirethings.util.MagicHelpers;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.MiscTools;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseMachineScreen<T extends BaseMachineContainer> extends BaseScreen<T> {
    protected final ResourceLocation JUSTSLOT = new ResourceLocation(JustDireThings.MODID, "textures/gui/justslot.png");
    protected final ResourceLocation POWERBAR = new ResourceLocation(JustDireThings.MODID, "textures/gui/powerbar.png");
    protected final ResourceLocation FLUIDBAR = new ResourceLocation(JustDireThings.MODID, "textures/gui/fluidbar.png");
    protected final ResourceLocation SOCIALBACKGROUND = new ResourceLocation(JustDireThings.MODID, "background");
    protected BaseMachineContainer container;
    protected BaseMachineBE baseMachineBE;
    protected double xRadius = 3, yRadius = 3, zRadius = 3;
    protected int xOffset = 0, yOffset = 0, zOffset = 0;
    protected boolean renderArea = false;
    protected FilterData filterData;
    protected MiscHelpers.RedstoneMode redstoneMode;
    protected List<ValueButtons> valueButtonsList = new ArrayList<>();
    protected List<ValueButtonsDouble> valueButtonsDoubleList = new ArrayList<>();
    protected int topSectionLeft;
    protected int topSectionTop;
    protected int topSectionWidth;
    protected int topSectionHeight;
    protected int extraWidth;
    protected int extraHeight;
    protected int direction;
    protected int tickSpeed;
    protected List<AbstractWidget> widgetsToRemove = new ArrayList<>();
    protected List<AbstractWidget> widgetsToAdd = new ArrayList<>();
    protected boolean renderablesChanged = false;

    public BaseMachineScreen(T container, Inventory pPlayerInventory, Component pTitle) {
        super(container, pPlayerInventory, pTitle);
        this.container = container;
        baseMachineBE = container.baseMachineBE;
        if (baseMachineBE instanceof AreaAffectingBE areaAffectingBE) {
            this.xRadius = areaAffectingBE.getAreaAffectingData().xRadius;
            this.yRadius = areaAffectingBE.getAreaAffectingData().yRadius;
            this.zRadius = areaAffectingBE.getAreaAffectingData().zRadius;
            this.xOffset = areaAffectingBE.getAreaAffectingData().xOffset;
            this.yOffset = areaAffectingBE.getAreaAffectingData().yOffset;
            this.zOffset = areaAffectingBE.getAreaAffectingData().zOffset;
            this.renderArea = areaAffectingBE.getAreaAffectingData().renderArea;
        }
        if (baseMachineBE instanceof FilterableBE filterableBE) {
            this.filterData = filterableBE.getFilterData();
        }
        if (baseMachineBE instanceof RedstoneControlledBE redstoneControlledBE) {
            this.redstoneMode = redstoneControlledBE.getRedstoneControlData().redstoneMode;
        }
        if (baseMachineBE instanceof BaseMachineBE) {
            direction = baseMachineBE.getDirection();
            tickSpeed = baseMachineBE.getTickSpeed();
        }
    }

    public void calculateTopSection() {
        topSectionWidth = imageWidth + extraWidth;
        topSectionHeight = imageHeight + extraHeight - 64; //-64 for the inventory spots
        topSectionLeft = getGuiLeft() - extraWidth / 2;
        topSectionTop = getGuiTop() - extraHeight - 26;
    }

    public void setTopSection() {
        extraWidth = 20;
        extraHeight = 0;
    }

    @Override
    public void init() {
        super.init();
        setTopSection();
        calculateTopSection();
        valueButtonsList.clear();
        if (baseMachineBE instanceof AreaAffectingBE)
            addAreaButtons();
        if (baseMachineBE instanceof RedstoneControlledBE)
            addRedstoneButtons();
        if (baseMachineBE instanceof FilterableBE)
            addFilterButtons();
        addTickSpeedButton();
    }

    public void addTickSpeedButton() {
        addRenderableWidget(ToggleButtonFactory.TICKSPEEDBUTTON(getGuiLeft() + 144, topSectionTop + 40, tickSpeed, b -> {
            tickSpeed = ((NumberButton) b).getValue(); //The value is updated in the mouseClicked method below
            PacketDistributor.sendToServer(new TickSpeedPayload(tickSpeed));
        }));
    }

    public void addRedstoneButtons() {
        addRenderableWidget(ToggleButtonFactory.REDSTONEBUTTON(getGuiLeft() + 134, topSectionTop + 62, redstoneMode.ordinal(), b -> {
            redstoneMode = MiscHelpers.RedstoneMode.values()[((ToggleButton) b).getTexturePosition()];
            //((ToggleButton) b).nextTexturePosition();
            saveSettings();
        }));
    }

    public void addFilterButtons() {
        addRenderableWidget(ToggleButtonFactory.ALLOWLISTBUTTON(getGuiLeft() + 8, topSectionTop + 62, filterData.allowlist, b -> {
            filterData.allowlist = !filterData.allowlist;
            //((ToggleButton) b).toggleActive();
            saveSettings();
        }));

        addRenderableWidget(ToggleButtonFactory.COMPARENBTBUTTON(getGuiLeft() + 26, topSectionTop + 62, filterData.compareNBT, b -> {
            filterData.compareNBT = !filterData.compareNBT;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));

        if (filterData.blockItemFilter != -1) {
            addRenderableWidget(ToggleButtonFactory.FILTERBLOCKITEMBUTTON(getGuiLeft() + 44, topSectionTop + 62, filterData.blockItemFilter, b -> {
                //((ToggleButton) b).nextTexturePosition();
                filterData.blockItemFilter = ((ToggleButton) b).getTexturePosition();
                saveSettings();
            }));
        }
    }

    public void addAreaButtons() {
        addRenderableWidget(ToggleButtonFactory.RENDERAREABUTTON(getGuiLeft() + 152, topSectionTop + 62, renderArea, b -> {
            renderArea = !renderArea;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));

        valueButtonsDoubleList.add(new ValueButtonsDouble(getGuiLeft() + 25, topSectionTop + 12, xRadius, 0, ItemCollectorBE.maxRadius, font, (button, value) -> {
            xRadius = value;
            saveSettings();
        }));

        valueButtonsDoubleList.add(new ValueButtonsDouble(getGuiLeft() + 75, topSectionTop + 12, yRadius, 0, ItemCollectorBE.maxRadius, font, (button, value) -> {
            yRadius = value;
            saveSettings();
        }));

        valueButtonsDoubleList.add(new ValueButtonsDouble(getGuiLeft() + 125, topSectionTop + 12, zRadius, 0, ItemCollectorBE.maxRadius, font, (button, value) -> {
            zRadius = value;
            saveSettings();
        }));

        valueButtonsList.add(new ValueButtons(getGuiLeft() + 25, topSectionTop + 27, xOffset, -ItemCollectorBE.maxOffset, ItemCollectorBE.maxOffset, font, (button, value) -> {
            xOffset = value;
            saveSettings();
        }));

        valueButtonsList.add(new ValueButtons(getGuiLeft() + 75, topSectionTop + 27, yOffset, -ItemCollectorBE.maxOffset, ItemCollectorBE.maxOffset, font, (button, value) -> {
            yOffset = value;
            saveSettings();
        }));

        valueButtonsList.add(new ValueButtons(getGuiLeft() + 125, topSectionTop + 27, zOffset, -ItemCollectorBE.maxOffset, ItemCollectorBE.maxOffset, font, (button, value) -> {
            zOffset = value;
            saveSettings();
        }));

        valueButtonsList.forEach(valueButtons -> valueButtons.widgetList.forEach(this::addRenderableWidget));
        valueButtonsDoubleList.forEach(valueButtons -> valueButtons.widgetList.forEach(this::addRenderableWidget));
    }

    public int adjustNumberButton(int value, int change, int min, int max) {
        if (Screen.hasShiftDown()) change *= 10;
        if (Screen.hasControlDown()) change *= 64;
        if (change < 0) {
            value = (Math.max(value + change, min));
        } else {
            value = (Math.min(value + change, max));
        }
        return value;
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        if (MiscTools.inBounds(topSectionLeft, topSectionTop, topSectionWidth, topSectionHeight, mouseX, mouseY))
            return false;
        return super.hasClickedOutside(mouseX, mouseY, guiLeftIn, guiTopIn, mouseButton);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (baseMachineBE != null) {
            Component title = baseMachineBE.getBlockState().getBlock().getName();
            int titleX = topSectionLeft - getGuiLeft() + 20 + ((topSectionWidth - 40) / 2) - this.font.width(title) / 2;
            guiGraphics.drawString(this.font, title, titleX, topSectionTop - getGuiTop() - 14, 4210752, false);
        }
        if (baseMachineBE instanceof AreaAffectingBE) {
            int areaWidth = 158; //The width of the area buttons is 157, including labels
            int xStart = topSectionLeft + (topSectionWidth / 2) - (areaWidth / 2) - getGuiLeft();
            guiGraphics.drawString(this.font, Component.literal("Rad"), xStart - 4, topSectionTop - getGuiTop() + 14, 4210752, false);
            guiGraphics.drawString(this.font, Component.literal("Off"), xStart - 4, topSectionTop - getGuiTop() + 29, 4210752, false);
            guiGraphics.drawString(this.font, Component.literal("X"), xStart + 35, topSectionTop - getGuiTop() + 4, 4210752, false);
            guiGraphics.drawString(this.font, Component.literal("Y"), xStart + 85, topSectionTop - getGuiTop() + 4, 4210752, false);
            guiGraphics.drawString(this.font, Component.literal("Z"), xStart + 135, topSectionTop - getGuiTop() + 4, 4210752, false);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        guiGraphics.blitSprite(SOCIALBACKGROUND, topSectionLeft + 20, topSectionTop - 20, topSectionWidth - 40, 20);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        guiGraphics.blitSprite(SOCIALBACKGROUND, topSectionLeft, topSectionTop, topSectionWidth, topSectionHeight);
        guiGraphics.blitSprite(SOCIALBACKGROUND, relX, relY + 83 - 8, this.imageWidth, this.imageHeight - 73); //Inventory Section
        for (Slot slot : container.slots) {
            guiGraphics.blit(JUSTSLOT, getGuiLeft() + slot.x - 1, getGuiTop() + slot.y - 1, 0, 0, 18, 18);
        }
        if (baseMachineBE instanceof PoweredMachineBE poweredMachineBE) {
            guiGraphics.blit(POWERBAR, topSectionLeft + 5, topSectionTop + 5, 0, 0, 18, 72, 36, 72);
            int maxEnergy = poweredMachineBE.getMaxEnergy(), height = 70;
            if (maxEnergy > 0) {
                int remaining = (this.container.getEnergy() * height) / maxEnergy;
                guiGraphics.blit(POWERBAR, topSectionLeft + 5 + 1, topSectionTop + 5 + 72 - 2 - remaining, 19, 69 - remaining, 17, remaining + 1, 36, 72);
            }
        }
        if (baseMachineBE instanceof FluidMachineBE fluidMachineBE) {
            int offset = getFluidBarOffset();
            guiGraphics.blit(FLUIDBAR, topSectionLeft + offset, topSectionTop + 5, 0, 0, 18, 72, 36, 72);
            int maxMB = fluidMachineBE.getMaxMB(), height = 70;
            if (maxMB > 0) {
                int remaining = (this.container.getFluidAmount() * height) / maxMB;
                renderFluid(guiGraphics, topSectionLeft + offset + 1, topSectionTop + 5 + 72 - 1, 16, remaining);
            }
            guiGraphics.blit(FLUIDBAR, topSectionLeft + offset, topSectionTop + 5, 18, 0, 18, 72, 36, 72);
        }
        if (renderablesChanged)
            updateRenderables();
    }

    public int getFluidBarOffset() {
        return 24;
    }

    public void renderFluid(GuiGraphics guiGraphics, int startX, int startY, int width, int height) {
        FluidStack fluidStack = container.getFluidStack();
        if (fluidStack.isEmpty()) return;

        Fluid fluid = fluidStack.getFluid();
        ResourceLocation fluidStill = IClientFluidTypeExtensions.of(fluid).getStillTexture();
        TextureAtlasSprite fluidStillSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
        int fluidColor = IClientFluidTypeExtensions.of(fluid).getTintColor(fluidStack);

        float red = (float) (fluidColor >> 16 & 255) / 255.0F;
        float green = (float) (fluidColor >> 8 & 255) / 255.0F;
        float blue = (float) (fluidColor & 255) / 255.0F;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        RenderSystem.setShaderColor(red, green, blue, 1.0f);

        int zLevel = 0;
        float uMin = fluidStillSprite.getU0();
        float uMax = fluidStillSprite.getU1();
        float vMin = fluidStillSprite.getV0();
        float vMax = fluidStillSprite.getV1();
        int textureWidth = fluidStillSprite.contents().width();
        int textureHeight = fluidStillSprite.contents().height();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder vertexBuffer = tesselator.getBuilder();

        vertexBuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        int yOffset = 0;
        while (yOffset < height) {
            int drawHeight = Math.min(textureHeight, height - yOffset);
            int drawY = startY - yOffset - drawHeight; // Adjust for bottom-to-top drawing

            float vMaxAdjusted = vMin + (vMax - vMin) * ((float) drawHeight / textureHeight);

            int xOffset = 0;
            while (xOffset < width) {
                int drawWidth = Math.min(textureWidth, width - xOffset);

                float uMaxAdjusted = uMin + (uMax - uMin) * ((float) drawWidth / textureWidth);

                vertexBuffer.vertex(poseStack.last().pose(), startX + xOffset, drawY + drawHeight, zLevel).uv(uMin, vMaxAdjusted).endVertex();
                vertexBuffer.vertex(poseStack.last().pose(), startX + xOffset + drawWidth, drawY + drawHeight, zLevel).uv(uMaxAdjusted, vMaxAdjusted).endVertex();
                vertexBuffer.vertex(poseStack.last().pose(), startX + xOffset + drawWidth, drawY, zLevel).uv(uMaxAdjusted, vMin).endVertex();
                vertexBuffer.vertex(poseStack.last().pose(), startX + xOffset, drawY, zLevel).uv(uMin, vMin).endVertex();

                xOffset += drawWidth;
            }
            yOffset += drawHeight;
        }

        tesselator.end();
        poseStack.popPose();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.applyModelViewMatrix();
    }

    public void powerBarTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        if (baseMachineBE instanceof PoweredMachineBE poweredMachineBE) {
            if (MiscTools.inBounds(topSectionLeft + 5, topSectionTop + 5, 18, 72, pX, pY)) {
                if (hasShiftDown())
                    pGuiGraphics.renderTooltip(font, Language.getInstance().getVisualOrder(Arrays.asList(
                            Component.translatable("justdirethings.screen.energy", MagicHelpers.formatted(this.container.getEnergy()), MagicHelpers.formatted(poweredMachineBE.getMaxEnergy()))
                    )), pX, pY);
                else
                    pGuiGraphics.renderTooltip(font, Language.getInstance().getVisualOrder(Arrays.asList(
                            Component.translatable("justdirethings.screen.energy", MagicHelpers.withSuffix(this.container.getEnergy()), MagicHelpers.withSuffix(poweredMachineBE.getMaxEnergy()))
                    )), pX, pY);
            }
        }
    }

    public void fluidBarTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        if (baseMachineBE instanceof FluidMachineBE fluidMachineBE) {
            if (MiscTools.inBounds(topSectionLeft + getFluidBarOffset(), topSectionTop + 5, 18, 72, pX, pY)) {
                if (hasShiftDown())
                    pGuiGraphics.renderTooltip(font, Language.getInstance().getVisualOrder(Arrays.asList(
                            Component.translatable("justdirethings.screen.fluid", this.container.getFluidStack().getHoverName(), MagicHelpers.formatted(this.container.getFluidAmount()), MagicHelpers.formatted(fluidMachineBE.getMaxMB()))
                    )), pX, pY);
                else
                    pGuiGraphics.renderTooltip(font, Language.getInstance().getVisualOrder(Arrays.asList(
                            Component.translatable("justdirethings.screen.fluid", this.container.getFluidStack().getHoverName(), MagicHelpers.withSuffix(this.container.getFluidAmount()), MagicHelpers.withSuffix(fluidMachineBE.getMaxMB()))
                    )), pX, pY);
            }
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
        powerBarTooltip(pGuiGraphics, pX, pY);
        fluidBarTooltip(pGuiGraphics, pX, pY);
    }

    @Override
    public void onClose() {
        saveSettings();
        super.onClose();
    }

    @Override
    public boolean mouseClicked(double x, double y, int btn) {
        if (baseMachineBE instanceof FilterableBE filterableBE) {
            if (hoveredSlot != null && (hoveredSlot instanceof FilterBasicSlot)) {
                // By splitting the stack we can get air easily :) perfect removal basically
                ItemStack stack = this.menu.getCarried();// getMinecraft().player.inventoryMenu.getCarried();
                stack = stack.copy().split(hoveredSlot.getMaxStackSize()); // Limit to slot limit
                hoveredSlot.set(stack); // Temporarily update the client for continuity purposes
                PacketDistributor.sendToServer(new GhostSlotPayload(hoveredSlot.index, stack, stack.getCount(), -1));
                return true;
            }
        }
        for (Renderable renderable : this.renderables) {
            if (renderable instanceof NumberButton numberButton && MiscTools.inBounds(numberButton.getX(), numberButton.getY(), numberButton.getWidth(), numberButton.getHeight(), x, y)) {
                if (btn == 0)
                    numberButton.setValue(adjustNumberButton(numberButton.getValue(), 1, numberButton.min, numberButton.max));
                else if (btn == 1)
                    numberButton.setValue(adjustNumberButton(numberButton.getValue(), -1, numberButton.min, numberButton.max));
                numberButton.onPress();
                numberButton.playDownSound(Minecraft.getInstance().getSoundManager());
                return true;
            }
            if (renderable instanceof ToggleButton toggleButton && MiscTools.inBounds(toggleButton.getX(), toggleButton.getY(), toggleButton.getWidth(), toggleButton.getHeight(), x, y)) {
                if (btn == 1) {
                    toggleButton.onClick(x, y, btn);
                    toggleButton.playDownSound(Minecraft.getInstance().getSoundManager());
                }
            }
        }
        return super.mouseClicked(x, y, btn);
    }

    public void updateRenderables() {
        if (!widgetsToRemove.isEmpty()) {
            for (AbstractWidget abstractWidget : widgetsToRemove) {
                removeWidget(abstractWidget);
            }
            widgetsToRemove.clear();
        }
        if (!widgetsToAdd.isEmpty()) {
            for (AbstractWidget abstractWidget : widgetsToAdd) {
                addRenderableWidget(abstractWidget);
            }
            widgetsToAdd.clear();
        }
        renderablesChanged = false;
    }

    public void saveSettings() {
        if (baseMachineBE instanceof AreaAffectingBE)
            PacketDistributor.sendToServer(new AreaAffectingPayload(xRadius, yRadius, zRadius, xOffset, yOffset, zOffset, renderArea));
        if (baseMachineBE instanceof FilterableBE)
            PacketDistributor.sendToServer(new FilterSettingPayload(filterData.allowlist, filterData.compareNBT, filterData.blockItemFilter));
        if (baseMachineBE instanceof RedstoneControlledBE)
            PacketDistributor.sendToServer(new RedstoneSettingPayload(redstoneMode.ordinal()));
    }
}
