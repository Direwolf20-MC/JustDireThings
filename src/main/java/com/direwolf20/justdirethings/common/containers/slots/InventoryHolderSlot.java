package com.direwolf20.justdirethings.common.containers.slots;

import com.direwolf20.justdirethings.common.blockentities.InventoryHolderBE;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.util.ItemStackKey;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class InventoryHolderSlot extends ResourceHandlerSlot {
    private final int slotIndex;
    private final InventoryHolderBE inventoryHolderBE;

    public InventoryHolderSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition, InventoryHolderBE inventoryHolderBE) {
        super(handler, slotModifier, index, xPosition, yPosition);
        this.slotIndex = index;
        this.inventoryHolderBE = inventoryHolderBE;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return mayPlaceFiltered(stack);
    }

    @Override
    public int getMaxStackSize() {
        if (inventoryHolderBE != null && inventoryHolderBE.compareCounts)
            return getFilterStackSize();
        return super.getMaxStackSize();
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        if (inventoryHolderBE != null && inventoryHolderBE.compareCounts)
            return getFilterStackSize(stack);
        return super.getMaxStackSize(stack);
    }

    public boolean mayPlaceFiltered(ItemStack currentStack) {
        if (this.inventoryHolderBE == null) return false;
        ItemStackKey key = new ItemStackKey(currentStack, inventoryHolderBE.compareNBT);

        FilterBasicHandler filteredItems = inventoryHolderBE.filterBasicHandler;
        ItemStack stack = filteredItems.getResource(slotIndex).toStack(filteredItems.getAmountAsInt(slotIndex));
        if (stack.isEmpty()) return !inventoryHolderBE.filtersOnly;
        return key.equals(new ItemStackKey(stack, inventoryHolderBE.compareNBT));
    }

    public int getFilterStackSize() {
        if (this.inventoryHolderBE == null) return 0;
        FilterBasicHandler filteredItems = inventoryHolderBE.filterBasicHandler;
        ItemStack filterStack = filteredItems.getResource(slotIndex).toStack(filteredItems.getAmountAsInt(slotIndex));
        if (filterStack.isEmpty())
            return super.getMaxStackSize();
        return filterStack.getCount();
    }

    public int getFilterStackSize(ItemStack stack) {
        if (this.inventoryHolderBE == null) return 0;
        FilterBasicHandler filteredItems = inventoryHolderBE.filterBasicHandler;
        ItemStack filterStack = filteredItems.getResource(slotIndex).toStack(filteredItems.getAmountAsInt(slotIndex));
        if (filterStack.isEmpty())
            return super.getMaxStackSize(stack);
        return filterStack.getCount();
    }
}
