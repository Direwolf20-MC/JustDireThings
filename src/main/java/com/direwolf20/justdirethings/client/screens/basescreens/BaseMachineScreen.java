package com.direwolf20.justdirethings.client.screens.basescreens;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ValueButtons;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.ItemCollectorBE;
import com.direwolf20.justdirethings.common.blockentities.basebe.*;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.containers.slots.FilterBasicSlot;
import com.direwolf20.justdirethings.common.network.data.AreaAffectingPayload;
import com.direwolf20.justdirethings.common.network.data.FilterSettingPayload;
import com.direwolf20.justdirethings.common.network.data.GhostSlotPayload;
import com.direwolf20.justdirethings.common.network.data.RedstoneSettingPayload;
import com.direwolf20.justdirethings.util.MagicHelpers;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.MiscTools;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseMachineScreen<T extends BaseMachineContainer> extends BaseScreen<T> {
    private final ResourceLocation JUSTSLOT = new ResourceLocation(JustDireThings.MODID, "textures/gui/justslot.png");
    private final ResourceLocation POWERBAR = new ResourceLocation(JustDireThings.MODID, "textures/gui/powerbar.png");
    private final ResourceLocation SOCIALBACKGROUND = new ResourceLocation(JustDireThings.MODID, "background");
    protected BaseMachineContainer container;
    protected BaseMachineBE baseMachineBE;
    protected int xRadius = 3, yRadius = 3, zRadius = 3;
    protected int xOffset = 0, yOffset = 0, zOffset = 0;
    protected boolean renderArea = false;
    protected FilterData filterData;
    protected MiscHelpers.RedstoneMode redstoneMode;
    protected List<ValueButtons> valueButtonsList = new ArrayList<>();
    protected int topSectionLeft;
    protected int topSectionTop;
    protected int topSectionWidth;
    protected int topSectionHeight;
    protected int extraWidth;
    protected int extraHeight;

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
    }

    public void addRedstoneButtons() {
        addRenderableWidget(ToggleButtonFactory.REDSTONEBUTTON(getGuiLeft() + 134, topSectionTop + 62, redstoneMode.ordinal(), b -> {
            redstoneMode = redstoneMode.next();
            ((ToggleButton) b).nextTexturePosition();
            saveSettings();
        }));
    }

    public void addFilterButtons() {
        addRenderableWidget(ToggleButtonFactory.ALLOWLISTBUTTON(getGuiLeft() + 8, topSectionTop + 62, filterData.allowlist, b -> {
            filterData.allowlist = !filterData.allowlist;
            ((ToggleButton) b).toggleActive();
            saveSettings();
        }));

        addRenderableWidget(ToggleButtonFactory.COMPARENBTBUTTON(getGuiLeft() + 26, topSectionTop + 62, filterData.compareNBT, b -> {
            filterData.compareNBT = !filterData.compareNBT;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));

        if (filterData.blockItemFilter != -1) {
            addRenderableWidget(ToggleButtonFactory.FILTERBLOCKITEMBUTTON(getGuiLeft() + 44, topSectionTop + 62, filterData.blockItemFilter, b -> {
                filterData.blockItemFilter = filterData.blockItemFilter == 0 ? 1 : 0;
                ((ToggleButton) b).setTexturePosition(filterData.blockItemFilter);
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

        valueButtonsList.add(new ValueButtons(getGuiLeft() + 25, topSectionTop + 12, xRadius, 0, ItemCollectorBE.maxRadius, font, (button, value) -> {
            xRadius = value;
            saveSettings();
        }));

        valueButtonsList.add(new ValueButtons(getGuiLeft() + 75, topSectionTop + 12, yRadius, 0, ItemCollectorBE.maxRadius, font, (button, value) -> {
            yRadius = value;
            saveSettings();
        }));

        valueButtonsList.add(new ValueButtons(getGuiLeft() + 125, topSectionTop + 12, zRadius, 0, ItemCollectorBE.maxRadius, font, (button, value) -> {
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
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        if (MiscTools.inBounds(topSectionLeft, topSectionTop, topSectionWidth, topSectionHeight, mouseX, mouseY))
            return false;
        return super.hasClickedOutside(mouseX, mouseY, guiLeftIn, guiTopIn, mouseButton);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        //super.renderLabels(guiGraphics, mouseX, mouseY);
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
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
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

    @Override
    public void onClose() {
        saveSettings();
        super.onClose();
    }

    @Override
    public boolean mouseClicked(double x, double y, int btn) {
        if (baseMachineBE instanceof FilterableBE filterableBE) {
            if (hoveredSlot == null || !(hoveredSlot instanceof FilterBasicSlot))
                return super.mouseClicked(x, y, btn);

            // By splitting the stack we can get air easily :) perfect removal basically
            ItemStack stack = this.menu.getCarried();// getMinecraft().player.inventoryMenu.getCarried();
            stack = stack.copy().split(hoveredSlot.getMaxStackSize()); // Limit to slot limit
            hoveredSlot.set(stack); // Temporarily update the client for continuity purposes
            PacketDistributor.SERVER.noArg().send(new GhostSlotPayload(hoveredSlot.index, stack, stack.getCount(), -1));
            return true;
        }
        return super.mouseClicked(x, y, btn);
    }

    public void saveSettings() {
        if (baseMachineBE instanceof AreaAffectingBE)
            PacketDistributor.SERVER.noArg().send(new AreaAffectingPayload(xRadius, yRadius, zRadius, xOffset, yOffset, zOffset, renderArea));
        if (baseMachineBE instanceof FilterableBE)
            PacketDistributor.SERVER.noArg().send(new FilterSettingPayload(filterData.allowlist, filterData.compareNBT, filterData.blockItemFilter));
        if (baseMachineBE instanceof RedstoneControlledBE)
            PacketDistributor.SERVER.noArg().send(new RedstoneSettingPayload(redstoneMode.ordinal()));
    }
}
