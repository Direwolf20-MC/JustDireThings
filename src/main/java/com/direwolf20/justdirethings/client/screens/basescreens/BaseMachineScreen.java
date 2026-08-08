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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMachineScreen<T extends BaseMachineContainer> extends BaseScreen<T> {
    private static final int LABEL_TEXT_ARGB = 0xFF404040;

    protected final Identifier JUSTSLOT = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/justslot.png");
    protected final Identifier POWERBAR = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/powerbar.png");
    protected final Identifier FLUIDBAR = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/fluidbar.png");
    protected final Identifier SOCIALBACKGROUND = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "background");
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
        topSectionLeft = leftPos - extraWidth / 2;
        topSectionTop = topPos - extraHeight - 26;
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
        valueButtonsDoubleList.clear();
        widgetsToAdd.clear();
        widgetsToRemove.clear();
        renderablesChanged = false;
        if (baseMachineBE instanceof AreaAffectingBE)
            addAreaButtons();
        if (baseMachineBE instanceof RedstoneControlledBE)
            addRedstoneButtons();
        if (baseMachineBE instanceof FilterableBE)
            addFilterButtons();
        addTickSpeedButton();
    }

    public void addTickSpeedButton() {
        addRenderableWidget(ToggleButtonFactory.TICKSPEEDBUTTON(leftPos + 144, topSectionTop + 40, tickSpeed, b -> {
            tickSpeed = ((NumberButton) b).getValue(); //The value is updated in the mouseClicked method below
            ClientPacketDistributor.sendToServer(new TickSpeedPayload(tickSpeed));
        }));
    }

    public void addRedstoneButtons() {
        addRenderableWidget(ToggleButtonFactory.REDSTONEBUTTON(leftPos + 134, topSectionTop + 62, redstoneMode.ordinal(), b -> {
            redstoneMode = MiscHelpers.RedstoneMode.values()[((ToggleButton) b).getTexturePosition()];
            saveSettings();
        }));
    }

    public void addFilterButtons() {
        addRenderableWidget(ToggleButtonFactory.ALLOWLISTBUTTON(leftPos + 8, topSectionTop + 62, filterData.allowlist, b -> {
            filterData.allowlist = !filterData.allowlist;
            saveSettings();
        }));

        addRenderableWidget(ToggleButtonFactory.COMPARENBTBUTTON(leftPos + 26, topSectionTop + 62, filterData.compareNBT, b -> {
            filterData.compareNBT = !filterData.compareNBT;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));

        if (filterData.blockItemFilter != -1) {
            addRenderableWidget(ToggleButtonFactory.FILTERBLOCKITEMBUTTON(leftPos + 44, topSectionTop + 62, filterData.blockItemFilter, b -> {
                filterData.blockItemFilter = ((ToggleButton) b).getTexturePosition();
                saveSettings();
            }));
        }
    }

    public void addAreaButtons() {
        addRenderableWidget(ToggleButtonFactory.RENDERAREABUTTON(leftPos + 152, topSectionTop + 62, renderArea, b -> {
            renderArea = !renderArea;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));

        valueButtonsDoubleList.add(new ValueButtonsDouble(leftPos + 25, topSectionTop + 12, xRadius, 0, ItemCollectorBE.maxRadius, font, (button, value) -> {
            xRadius = value;
            saveSettings();
        }));

        valueButtonsDoubleList.add(new ValueButtonsDouble(leftPos + 75, topSectionTop + 12, yRadius, 0, ItemCollectorBE.maxRadius, font, (button, value) -> {
            yRadius = value;
            saveSettings();
        }));

        valueButtonsDoubleList.add(new ValueButtonsDouble(leftPos + 125, topSectionTop + 12, zRadius, 0, ItemCollectorBE.maxRadius, font, (button, value) -> {
            zRadius = value;
            saveSettings();
        }));

        valueButtonsList.add(new ValueButtons(leftPos + 25, topSectionTop + 27, xOffset, -ItemCollectorBE.maxOffset, ItemCollectorBE.maxOffset, font, (button, value) -> {
            xOffset = value;
            saveSettings();
        }));

        valueButtonsList.add(new ValueButtons(leftPos + 75, topSectionTop + 27, yOffset, -ItemCollectorBE.maxOffset, ItemCollectorBE.maxOffset, font, (button, value) -> {
            yOffset = value;
            saveSettings();
        }));

        valueButtonsList.add(new ValueButtons(leftPos + 125, topSectionTop + 27, zOffset, -ItemCollectorBE.maxOffset, ItemCollectorBE.maxOffset, font, (button, value) -> {
            zOffset = value;
            saveSettings();
        }));

        valueButtonsList.forEach(valueButtons -> valueButtons.widgetList.forEach(this::addRenderableWidget));
        valueButtonsDoubleList.forEach(valueButtons -> valueButtons.widgetList.forEach(this::addRenderableWidget));
    }

    public int adjustNumberButton(int value, int change, int min, int max) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.hasShiftDown()) change *= 10;
        if (mc.hasControlDown()) change *= 64;
        if (change < 0) {
            value = (Math.max(value + change, min));
        } else {
            value = (Math.min(value + change, max));
        }
        return value;
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn) {
        if (MiscTools.inBounds(topSectionLeft, topSectionTop, topSectionWidth, topSectionHeight, mouseX, mouseY))
            return false;
        return super.hasClickedOutside(mouseX, mouseY, guiLeftIn, guiTopIn);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (baseMachineBE != null) {
            Component title = baseMachineBE.getBlockState().getBlock().getName();
            int titleX = topSectionLeft - leftPos + 20 + ((topSectionWidth - 40) / 2) - this.font.width(title) / 2;
            graphics.text(this.font, title, titleX, topSectionTop - topPos - 14, LABEL_TEXT_ARGB, false);
        }
        if (baseMachineBE instanceof AreaAffectingBE) {
            int areaWidth = 158; //The width of the area buttons is 157, including labels
            int xStart = topSectionLeft + (topSectionWidth / 2) - (areaWidth / 2) - leftPos;
            graphics.text(this.font, Component.literal("Rad"), xStart - 4, topSectionTop - topPos + 14, LABEL_TEXT_ARGB, false);
            graphics.text(this.font, Component.literal("Off"), xStart - 4, topSectionTop - topPos + 29, LABEL_TEXT_ARGB, false);
            graphics.text(this.font, Component.literal("X"), xStart + 35, topSectionTop - topPos + 4, LABEL_TEXT_ARGB, false);
            graphics.text(this.font, Component.literal("Y"), xStart + 85, topSectionTop - topPos + 4, LABEL_TEXT_ARGB, false);
            graphics.text(this.font, Component.literal("Z"), xStart + 135, topSectionTop - topPos + 4, LABEL_TEXT_ARGB, false);
        }
    }

    protected void drawSlot(GuiGraphicsExtractor graphics, Slot slot) {
        if (slot instanceof FilterBasicSlot) {
            drawFilterSlot(graphics, slot);
        } else if (slot instanceof net.neoforged.neoforge.transfer.item.ResourceHandlerSlot) {
            drawMachineSlot(graphics, slot);
        } else {
            drawInventorySlot(graphics, slot);
        }
    }

    protected void drawFilterSlot(GuiGraphicsExtractor graphics, Slot slot) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, JUSTSLOT, leftPos + slot.x - 1, topPos + slot.y - 1, 0.0F, 0.0F, 18, 18, 256, 256);
    }

    protected void drawMachineSlot(GuiGraphicsExtractor graphics, Slot slot) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, JUSTSLOT, leftPos + slot.x - 1, topPos + slot.y - 1, 0.0F, 0.0F, 18, 18, 256, 256);
    }

    protected void drawInventorySlot(GuiGraphicsExtractor graphics, Slot slot) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, JUSTSLOT, leftPos + slot.x - 1, topPos + slot.y - 1, 0.0F, 0.0F, 18, 18, 256, 256);
    }

    protected void drawBasicSlot(GuiGraphicsExtractor graphics, Slot slot) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, JUSTSLOT, leftPos + slot.x - 1, topPos + slot.y - 1, 0.0F, 0.0F, 18, 18, 256, 256);
    }

    public void renderInventorySection(GuiGraphicsExtractor graphics, int relX, int relY) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SOCIALBACKGROUND, relX, relY + 83 - 8, this.imageWidth, this.imageHeight - 73); //Inventory Section
    }

    public void extractTitleSprite(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SOCIALBACKGROUND, topSectionLeft + 20, topSectionTop - 20,
                topSectionWidth - 40, 20);
    }

    public void extractMachineSprite(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SOCIALBACKGROUND, topSectionLeft, topSectionTop,
                topSectionWidth, topSectionHeight);
    }

    public void extractInventorySprite(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;

        renderInventorySection(graphics, relX, relY);
        for (Slot slot : container.slots) {
            drawSlot(graphics, slot);
        }

    }

    public void extractPowerBarSprite(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        if (baseMachineBE instanceof PoweredMachineBE poweredMachineBE) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, POWERBAR, topSectionLeft + getEnergyBarOffset(),
                    topSectionTop + 5, 0.0F, 0.0F, 18, 72, 36, 72);
            int maxEnergy = poweredMachineBE.getMaxEnergy(), height = 70;
            if (maxEnergy > 0) {
                int remaining = (this.container.getEnergy() * height) / maxEnergy;
                graphics.blit(RenderPipelines.GUI_TEXTURED, POWERBAR, topSectionLeft + getEnergyBarOffset() + 1,
                        topSectionTop + getEnergyBarOffset() + 72 - 2 - remaining, 19.0F, 69.0F - remaining, 17,
                        remaining + 1, 36, 72);
            }
        }
    }

    public void extractFluidTankSprite(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        if (baseMachineBE instanceof FluidMachineBE fluidMachineBE) {
            int offset = getFluidBarOffset();
            graphics.blit(RenderPipelines.GUI_TEXTURED, FLUIDBAR, topSectionLeft + offset, topSectionTop + 5, 0.0F,
                    0.0F, 18, 72, 36, 72);
            int maxMB = fluidMachineBE.getMaxMB(), height = 70;
            if (maxMB > 0) {
                int remaining = (this.container.getFluidAmount() * height) / maxMB;
                renderFluid(graphics, topSectionLeft + offset + 1, topSectionTop + 5 + 72 - 1, 16, remaining);
            }
            graphics.blit(RenderPipelines.GUI_TEXTURED, FLUIDBAR, topSectionLeft + offset, topSectionTop + 5, 18.0F,
                    0.0F, 18, 72, 36, 72);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);

        extractTitleSprite(graphics, mouseX, mouseY, partialTicks);
        extractMachineSprite(graphics, mouseX, mouseY, partialTicks);

        extractInventorySprite(graphics, mouseX, mouseY, partialTicks);

        extractPowerBarSprite(graphics, mouseX, mouseY, partialTicks);

        extractFluidTankSprite(graphics, mouseX, mouseY, partialTicks);

        if (renderablesChanged)
            updateRenderables();
    }

    public int getEnergyBarOffset() {
        return 5;
    }

    public int getFluidBarOffset() {
        return baseMachineBE instanceof PoweredMachineBE ? 24 : getEnergyBarOffset();
    }

    public void renderFluid(GuiGraphicsExtractor graphics, int startX, int startY, int width, int height) {
        FluidStack fluidStack = container.getFluidStack();
        if (fluidStack.isEmpty() || height <= 0) return;

        net.minecraft.world.level.material.FluidState fluidState = fluidStack.getFluid().defaultFluidState();
        net.minecraft.client.renderer.block.FluidModel fluidModel =
                Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidState);
        net.minecraft.client.renderer.texture.TextureAtlasSprite sprite = fluidModel.stillMaterial().sprite();

        net.neoforged.neoforge.client.fluid.FluidTintSource tintSource = fluidModel.fluidTintSource();
        int tint = tintSource != null ? tintSource.colorAsStack(fluidStack) : -1;
        int colorARGB = tint | 0xFF000000;

        // Tile the fluid sprite vertically. The sprite's native size is 16 texels; scale to our box.
        int tileSize = 16;
        int remaining = height;
        int y = startY; // fill from bottom up; startY is the bar's bottom edge
        while (remaining > 0) {
            int drawHeight = Math.min(tileSize, remaining);
            int drawY = y - drawHeight;
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, startX, drawY, width, drawHeight, colorARGB);
            remaining -= drawHeight;
            y -= drawHeight;
        }
    }

    public void powerBarTooltip(GuiGraphicsExtractor graphics, int pX, int pY) {
        if (baseMachineBE instanceof PoweredMachineBE poweredMachineBE) {
            if (MiscTools.inBounds(topSectionLeft + getEnergyBarOffset(), topSectionTop + 5, 18, 72, pX, pY)) {
                Component msg = Minecraft.getInstance().hasShiftDown()
                        ? Component.translatable("justdirethings.screen.energy", MagicHelpers.formatted(this.container.getEnergy()), MagicHelpers.formatted(poweredMachineBE.getMaxEnergy()))
                        : Component.translatable("justdirethings.screen.energy", MagicHelpers.withSuffix(this.container.getEnergy()), MagicHelpers.withSuffix(poweredMachineBE.getMaxEnergy()));
                graphics.setTooltipForNextFrame(font, msg, pX, pY);
            }
        }
    }

    public void fluidBarTooltip(GuiGraphicsExtractor graphics, int pX, int pY) {
        if (baseMachineBE instanceof FluidMachineBE fluidMachineBE) {
            if (MiscTools.inBounds(topSectionLeft + getFluidBarOffset(), topSectionTop + 5, 18, 72, pX, pY)) {
                Component msg = Minecraft.getInstance().hasShiftDown()
                        ? Component.translatable("justdirethings.screen.fluid", this.container.getFluidStack().getHoverName(), MagicHelpers.formatted(this.container.getFluidAmount()), MagicHelpers.formatted(fluidMachineBE.getMaxMB()))
                        : Component.translatable("justdirethings.screen.fluid", this.container.getFluidStack().getHoverName(), MagicHelpers.withSuffix(this.container.getFluidAmount()), MagicHelpers.withSuffix(fluidMachineBE.getMaxMB()));
                graphics.setTooltipForNextFrame(font, msg, pX, pY);
            }
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);
        powerBarTooltip(graphics, mouseX, mouseY);
        fluidBarTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public void onClose() {
        saveSettings();
        super.onClose();
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double x = event.x();
        double y = event.y();
        int btn = event.button();
        if (baseMachineBE instanceof FilterableBE filterableBE) {
            if (hoveredSlot != null && (hoveredSlot instanceof FilterBasicSlot)) {
                ItemStack stack = this.menu.getCarried();
                stack = stack.copy().split(hoveredSlot.getMaxStackSize());
                hoveredSlot.set(stack);
                ClientPacketDistributor.sendToServer(new GhostSlotPayload(hoveredSlot.index, stack, stack.getCount(), -1));
                return true;
            }
        }
        for (Renderable renderable : this.renderables) {
            if (renderable instanceof NumberButton numberButton && MiscTools.inBounds(numberButton.getX(), numberButton.getY(), numberButton.getWidth(), numberButton.getHeight(), x, y)) {
                if (btn == 0)
                    numberButton.setValue(adjustNumberButton(numberButton.getValue(), 1, numberButton.min, numberButton.max));
                else if (btn == 1)
                    numberButton.setValue(adjustNumberButton(numberButton.getValue(), -1, numberButton.min, numberButton.max));
                numberButton.onPress(event);
                numberButton.playDownSound(Minecraft.getInstance().getSoundManager());
                return true;
            }
            if (renderable instanceof ToggleButton toggleButton && MiscTools.inBounds(toggleButton.getX(), toggleButton.getY(), toggleButton.getWidth(), toggleButton.getHeight(), x, y)) {
                if (btn == 1) {
                    toggleButton.onClick(event, doubleClick);
                    toggleButton.playDownSound(Minecraft.getInstance().getSoundManager());
                }
            }
        }
        return super.mouseClicked(event, doubleClick);
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
            ClientPacketDistributor.sendToServer(new AreaAffectingPayload(xRadius, yRadius, zRadius, xOffset, yOffset, zOffset, renderArea));
        if (baseMachineBE instanceof FilterableBE)
            ClientPacketDistributor.sendToServer(new FilterSettingPayload(filterData.allowlist, filterData.compareNBT, filterData.blockItemFilter));
        if (baseMachineBE instanceof RedstoneControlledBE)
            ClientPacketDistributor.sendToServer(new RedstoneSettingPayload(redstoneMode.ordinal()));
    }
}
