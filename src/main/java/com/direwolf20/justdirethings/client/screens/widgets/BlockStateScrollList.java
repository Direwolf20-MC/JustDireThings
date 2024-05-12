package com.direwolf20.justdirethings.client.screens.widgets;

import com.direwolf20.justdirethings.client.screens.basescreens.SensorScreenInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockStateScrollList extends ObjectSelectionList<BlockStateScrollList.BlockStateEntry> {
    private static String stripControlCodes(String value) {
        return net.minecraft.util.StringUtil.stripColor(value);
    }

    private final int listWidth;
    private ItemStack stateStack = ItemStack.EMPTY;
    private SensorScreenInterface parent;

    public BlockStateScrollList(SensorScreenInterface parent, int left, int listWidth, int top, int bottom) {
        super(Minecraft.getInstance(), listWidth, bottom - top, top, parent.getFontRenderer().lineHeight * 2 + 8);
        this.parent = parent;
        this.listWidth = listWidth;
        //this.setRenderBackground(false);
        this.refreshList();
        setX(left);
    }

    public ItemStack getStateStack() {
        return stateStack;
    }

    public void setStateStack(ItemStack stack) {
        this.stateStack = stack;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getX() + this.listWidth - 5;
    }

    @Override
    public int getRowWidth() {
        return this.listWidth;
    }

    public void refreshList() {
        this.clearEntries();
        if (stateStack.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            BlockState defaultState = block.defaultBlockState();
            for (Property<?> property : defaultState.getProperties()) {
                List<Comparable<?>> values = new ArrayList<>(property.getPossibleValues());
                Comparable<?> setValue = parent.getValue(property);
                boolean isAny = false;
                if (setValue == null) {
                    setValue = defaultState.getValue(property);
                    isAny = true;
                }
                addEntry(new BlockStateEntry(property, setValue, defaultState.getValue(property), parent, values, isAny));
            }
        }
    }

    @Override
    public void renderWidget(GuiGraphics p_282708_, int p_283242_, int p_282891_, float p_283683_) {
        renderContentBackground(p_282708_);
        super.renderWidget(p_282708_, p_283242_, p_282891_, p_283683_);
    }

    protected void renderContentBackground(GuiGraphics guiGraphics) {
        guiGraphics.fillGradient(getX(), getY(), getRight(), getBottom(), 0xC0101010, 0xD0101010);
    }

    public class BlockStateEntry extends ObjectSelectionList.Entry<BlockStateEntry> {
        private final Property<?> property;
        private final SensorScreenInterface parent;
        private Comparable<?> currentValue;
        private final List<Comparable<?>> possibleValues;
        private final int anyIndex;
        private boolean isAny;
        private Map<Property<?>, Comparable<?>> assignedValues = new HashMap<>();

        BlockStateEntry(Property<?> property, Comparable<?> currentValue, Comparable<?> defaultValue, SensorScreenInterface parent, List<Comparable<?>> possibleValues, boolean isAny) {
            this.property = property;
            this.currentValue = currentValue;
            this.anyIndex = possibleValues.indexOf(defaultValue);
            this.parent = parent;
            this.possibleValues = possibleValues;
            this.isAny = isAny;
        }

        @Override
        public Component getNarration() {
            return Component.translatable("narrator.select", property.getName());
        }

        @Override
        public void render(GuiGraphics guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            Component name = Component.literal(stripControlCodes(property.getName()));
            Component value = Component.literal(stripControlCodes(isAny ? "ANY" : this.currentValue.toString()));
            Font font = this.parent.getFontRenderer();
            guiGraphics.drawString(font, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(name, listWidth))), left + 3, top + 2, 0xFFFFFF, false);
            guiGraphics.drawString(font, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(value, listWidth))), left + 3, top + 2 + font.lineHeight, 0xCCCCCC, false);
        }

        @Override
        public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
            BlockStateScrollList.this.setSelected(this);
            int currentIndex = possibleValues.indexOf(this.currentValue);
            int nextIndex;
            if (currentIndex == anyIndex && isAny) {
                nextIndex = anyIndex;
                isAny = false;
            } else {
                nextIndex = (currentIndex + 1) % possibleValues.size();
                if (nextIndex == anyIndex) isAny = true;
            }
            currentValue = possibleValues.get(nextIndex);
            parent.setPropertyValue(property, currentValue, isAny);

            return false;
        }
    }
}