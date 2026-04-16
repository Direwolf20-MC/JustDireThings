package com.direwolf20.justdirethings.client.screens.widgets;

import com.direwolf20.justdirethings.client.screens.basescreens.SensorScreenInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockStateScrollList extends ObjectSelectionList<BlockStateScrollList.BlockStateEntry> {
    private static final int NAME_TEXT_ARGB = 0xFFFFFFFF;
    private static final int VALUE_TEXT_ARGB = 0xFFCCCCCC;
    private static final int BACKGROUND_TOP_ARGB = 0xC0101010;
    private static final int BACKGROUND_BOTTOM_ARGB = 0xD0101010;

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
        } else if (stateStack.getItem() instanceof BucketItem bucketItem) {
            BlockState defaultState = bucketItem.content.defaultFluidState().createLegacyBlock();
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
    public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        renderContentBackground(graphics);
        super.extractWidgetRenderState(graphics, mouseX, mouseY, partialTicks);
    }

    protected void renderContentBackground(GuiGraphicsExtractor graphics) {
        graphics.fillGradient(getX(), getY(), getRight(), getBottom(), BACKGROUND_TOP_ARGB, BACKGROUND_BOTTOM_ARGB);
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
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
            Component name = Component.literal(stripControlCodes(property.getName()));
            Component value = Component.literal(stripControlCodes(isAny ? "ANY" : this.currentValue.toString()));
            Font font = this.parent.getFontRenderer();
            int left = this.getX();
            int top = this.getY();
            graphics.text(font, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(name, listWidth))), left + 3, top + 2, NAME_TEXT_ARGB, false);
            graphics.text(font, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(value, listWidth))), left + 3, top + 2 + font.lineHeight, VALUE_TEXT_ARGB, false);
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
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
