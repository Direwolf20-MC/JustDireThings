package com.direwolf20.justdirethings.common.containers.slots;

import com.direwolf20.justdirethings.common.blockentities.InventoryHolderBE;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.util.ItemStackKey;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class InventoryHolderSlot extends SlotItemHandler {
    private InventoryHolderBE inventoryHolderBE;

    public InventoryHolderSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, InventoryHolderBE inventoryHolderBE) {
        super(itemHandler, index, xPosition, yPosition);
        this.inventoryHolderBE = inventoryHolderBE;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return mayPlaceFiltered(stack);
    }

    @Override
    public int getMaxStackSize() {
        if (inventoryHolderBE.compareCounts)
            return getFilterStackSize();
        return super.getMaxStackSize();
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        if (inventoryHolderBE.compareCounts)
            return getFilterStackSize(stack);
        return super.getMaxStackSize(stack);
    }

    public boolean mayPlaceFiltered(ItemStack currentStack) {
        if (this.inventoryHolderBE == null) return false;
        ItemStackKey key = new ItemStackKey(currentStack, inventoryHolderBE.compareNBT);

        FilterBasicHandler filteredItems = inventoryHolderBE.filterBasicHandler;
        ItemStack stack = filteredItems.getStackInSlot(index);
        if (stack.isEmpty()) return !inventoryHolderBE.filtersOnly;
        return key.equals(new ItemStackKey(stack, inventoryHolderBE.compareNBT));
    }

    public int getFilterStackSize() {
        if (this.inventoryHolderBE == null) return 0;
        FilterBasicHandler filteredItems = inventoryHolderBE.filterBasicHandler;
        ItemStack filterStack = filteredItems.getStackInSlot(index);
        if (filterStack.isEmpty())
            return super.getMaxStackSize();
        return filterStack.getCount();
    }

    public int getFilterStackSize(ItemStack stack) {
        if (this.inventoryHolderBE == null) return 0;
        FilterBasicHandler filteredItems = inventoryHolderBE.filterBasicHandler;
        ItemStack filterStack = filteredItems.getStackInSlot(index);
        if (filterStack.isEmpty())
            return super.getMaxStackSize(stack);
        return filterStack.getCount();
    }
}
