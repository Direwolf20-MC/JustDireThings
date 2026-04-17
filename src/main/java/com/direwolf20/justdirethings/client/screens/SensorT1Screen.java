package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.basescreens.SensorScreenInterface;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.BlockStateScrollList;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.blockentities.SensorT1BE;
import com.direwolf20.justdirethings.common.blockentities.basebe.FilterableBE;
import com.direwolf20.justdirethings.common.containers.SensorT1Container;
import com.direwolf20.justdirethings.common.containers.slots.FilterBasicSlot;
import com.direwolf20.justdirethings.common.network.data.BlockStateFilterPayload;
import com.direwolf20.justdirethings.common.network.data.SensorPayload;
import com.direwolf20.justdirethings.util.MiscTools;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorT1Screen extends BaseMachineScreen<SensorT1Container> implements SensorScreenInterface {
    public int senseTarget;
    public boolean strongSignal;
    public boolean showBlockStates;
    public int blockStateSlot = -1;
    private BlockStateScrollList scrollPanel;
    public ItemStack stateItemStack = ItemStack.EMPTY;
    public Map<Integer, Map<Property<?>, Comparable<?>>> blockStateProperties = new HashMap<>();
    public Map<Integer, ItemStack> itemStackCache = new HashMap<>();

    public SensorT1Screen(SensorT1Container container, Inventory inv, Component name) {
        super(container, inv, name);
        if (baseMachineBE instanceof SensorT1BE sensor) {
            senseTarget = sensor.sense_target.ordinal();
            strongSignal = sensor.strongSignal;
            blockStateProperties = sensor.blockStateProperties;
            populateItemStackCache();
        }
    }

    @Override
    public void addFilterButtons() {
        addRenderableWidget(ToggleButtonFactory.ALLOWLISTBUTTON(leftPos + 38, topSectionTop + 38, filterData.allowlist, b -> {
            filterData.allowlist = !filterData.allowlist;
            saveSettings();
        }));
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.SENSORTARGETBUTTON(leftPos + 56, topSectionTop + 38, senseTarget, b -> {
            senseTarget = ((ToggleButton) b).getTexturePosition();
            saveSettings();
        }));
        addRenderableWidget(ToggleButtonFactory.STRONGWEAKREDSTONEBUTTON(leftPos + 20, topSectionTop + 38, strongSignal ? 1 : 0, b -> {
            strongSignal = ((ToggleButton) b).getTexturePosition() == 1;
            saveSettings();
        }));

        this.scrollPanel = new BlockStateScrollList(this, topSectionLeft - 95, 90, topSectionTop + 5, topSectionTop + topSectionHeight - 10);
    }

    @Override
    public void setTopSection() {
        extraWidth = 0;
        extraHeight = 0;
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn) {
        if (showBlockStates && MiscTools.inBounds(topSectionLeft - 101, topSectionTop, 100, topSectionHeight, mouseX, mouseY))
            return false;
        return super.hasClickedOutside(mouseX, mouseY, guiLeftIn, guiTopIn);
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
        ClientPacketDistributor.sendToServer(new SensorPayload(senseTarget, strongSignal, 0, 0));
    }

    public Comparable<?> getValue(Property<?> property) {
        if (!blockStateProperties.containsKey(blockStateSlot)) return null;
        Map<Property<?>, Comparable<?>> props = blockStateProperties.get(blockStateSlot);
        if (!props.containsKey(property)) return null;
        return props.get(property);
    }

    public void setPropertyValue(Property<?> property, Comparable<?> comparable, boolean isAny) {
        Map<Property<?>, Comparable<?>> props = blockStateProperties.getOrDefault(blockStateSlot, new HashMap<>());
        if (isAny) {
            props.remove(property);
        } else {
            props.put(property, comparable);
        }
        blockStateProperties.put(blockStateSlot, props);
        saveBlockStateData(blockStateSlot);
    }

    public void clearStateProperties(int slot) {
        blockStateProperties.put(slot, new HashMap<>());
    }

    private ItemStack filterStack(int slot) {
        return container.filterHandler.getResource(slot).toStack(container.filterHandler.getAmountAsInt(slot));
    }

    public void populateItemStackCache() {
        for (int i = 0; i < container.FILTER_SLOTS; i++) {
            this.itemStackCache.put(i, filterStack(i));
        }
    }

    public void validateItemStackCache() {
        for (int i = 0; i < container.FILTER_SLOTS; i++) {
            ItemStack stack = filterStack(i);
            ItemStack cachedStack = itemStackCache.get(i);
            if (!ItemStack.isSameItemSameComponents(stack, cachedStack)) { //If the stack has changed, clear the props!
                clearStateProperties(i);
                saveBlockStateData(i);
                itemStackCache.put(i, stack);
            }
        }
    }

    public void saveBlockStateData(int slot) {
        if (!blockStateProperties.containsKey(slot)) return;
        Map<Property<?>, Comparable<?>> props = blockStateProperties.get(slot);
        CompoundTag tag = new CompoundTag();
        ListTag listTag = SensorT1BE.saveBlockStateProperty(props);
        tag.put("tagList", listTag);
        ClientPacketDistributor.sendToServer(new BlockStateFilterPayload(slot, tag));
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        validateItemStackCache();
        if (showBlockStates) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SOCIALBACKGROUND, topSectionLeft - 100, topSectionTop, 100, topSectionHeight);
            if (blockStateSlot != -1 && !filterStack(blockStateSlot).equals(scrollPanel.getStateStack()))
                refreshStateWindow();
        }
    }

    public void refreshStateWindow() {
        if (!showBlockStates || blockStateSlot == -1) return;
        stateItemStack = filterStack(blockStateSlot);
        scrollPanel.setStateStack(stateItemStack);
        scrollPanel.refreshList();
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (hoveredSlot != null && (hoveredSlot instanceof FilterBasicSlot)) {
            if (this.menu.getCarried().isEmpty() && this.hoveredSlot.hasItem()) {
                List<Component> components = new ArrayList<>();
                ItemStack itemstack = this.hoveredSlot.getItem();
                components.add(Component.translatable("justdirethings.screen.rightclicksettings").withStyle(ChatFormatting.RED));
                components.addAll(this.getTooltipFromContainerItem(itemstack));
                graphics.setTooltipForNextFrame(this.font, components, itemstack.getTooltipImage(), itemstack, mouseX, mouseY);
            } else {
                graphics.setTooltipForNextFrame(this.font,
                        Component.translatable("justdirethings.screen.rightclicksettings").withStyle(ChatFormatting.RED),
                        mouseX, mouseY);
            }
        } else {
            super.extractTooltip(graphics, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (baseMachineBE instanceof FilterableBE filterableBE) {
            if (hoveredSlot != null && (hoveredSlot instanceof FilterBasicSlot)) {
                if (event.button() == 1) {
                    if (showBlockStates) {
                        blockStateSlot = -1;
                        stateItemStack = ItemStack.EMPTY;
                        scrollPanel.setStateStack(ItemStack.EMPTY);
                        this.removeWidget(scrollPanel);
                    } else {
                        blockStateSlot = hoveredSlot.getSlotIndex();
                        stateItemStack = hoveredSlot.getItem();
                        scrollPanel.setStateStack(stateItemStack);
                        this.addRenderableWidget(scrollPanel);
                    }
                    showBlockStates = !showBlockStates;
                    this.scrollPanel.refreshList();
                    return true;
                }
            }
        }
        return super.mouseClicked(event, doubleClick);
    }
}
