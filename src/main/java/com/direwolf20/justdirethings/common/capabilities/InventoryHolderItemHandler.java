package com.direwolf20.justdirethings.common.capabilities;

import com.direwolf20.justdirethings.common.blockentities.InventoryHolderBE;
import com.direwolf20.justdirethings.util.ItemStackKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InventoryHolderItemHandler extends ItemStackHandler {
    InventoryHolderBE inventoryHolderBE;
    ItemStackHandler sourceHandler;

    public InventoryHolderItemHandler(InventoryHolderBE inventoryHolderBE, ItemStackHandler sourceHandler) {
        super(sourceHandler.getSlots());
        this.inventoryHolderBE = inventoryHolderBE;
        this.sourceHandler = sourceHandler;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (inventoryHolderBE == null) return ItemStack.EMPTY;
        int amountAllowed = inventoryHolderBE.allowedExtractAmount(slot, amount);
        return sourceHandler.extractItem(slot, amountAllowed, simulate);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        ItemStackKey key = new ItemStackKey(stack, inventoryHolderBE.compareNBT);
        if (inventoryHolderBE.filteredCache.containsKey(key)) {
            List<Integer> slotsList = inventoryHolderBE.filteredCache.get(key);
            if (slotsList.contains(slot)) //Skip this if we're already inserting into a filtered slot
                return insertItemProxy(slot, stack, simulate);
            for (Integer i : slotsList) {
                stack = insertItemProxy(i, stack, simulate);
            }
        }
        return insertItemProxy(slot, stack, simulate);
    }

    public @NotNull ItemStack insertItemProxy(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        if (!isItemValid(slot, stack))
            return stack;

        validateSlotIndex(slot);

        ItemStack existing = sourceHandler.getStackInSlot(slot);

        int limit = getStackLimit(slot, stack);

        if (!existing.isEmpty()) {
            if (!ItemStack.isSameItemSameComponents(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                sourceHandler.setStackInSlot(slot, reachedLimit ? stack.copyWithCount(limit) : stack);
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            onContentsChanged(slot);
        }

        return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack itemStack) {
        if (inventoryHolderBE == null) return false;
        if (inventoryHolderBE.automatedFiltersOnly)
            return inventoryHolderBE.isStackValidFilter(itemStack, slot);
        return sourceHandler.isItemValid(slot, itemStack);
    }

    @Override
    public int getSlotLimit(int slot) {
        if (inventoryHolderBE == null) return 0;
        int allowedAmt = inventoryHolderBE.getSlotLimit(slot);
        if (allowedAmt == -1)
            return Item.ABSOLUTE_MAX_STACK_SIZE;
        return allowedAmt;
    }

    @Override
    protected int getStackLimit(int slot, ItemStack stack) {
        if (inventoryHolderBE == null) return 0;
        int allowedAmt = inventoryHolderBE.getSlotLimit(slot);
        if (allowedAmt == -1)
            return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
        return Math.min(allowedAmt, stack.getMaxStackSize());
    }

    @Override
    public int getSlots() {
        return sourceHandler.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int i) {
        return sourceHandler.getStackInSlot(i);
    }
}
