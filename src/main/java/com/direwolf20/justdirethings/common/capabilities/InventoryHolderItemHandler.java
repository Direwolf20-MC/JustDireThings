package com.direwolf20.justdirethings.common.capabilities;

import com.direwolf20.justdirethings.common.blockentities.InventoryHolderBE;
import com.direwolf20.justdirethings.util.ItemStackKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.List;

public class InventoryHolderItemHandler implements ResourceHandler<ItemResource> {
    private final InventoryHolderBE inventoryHolderBE;
    private final ItemStacksResourceHandler sourceHandler;

    public InventoryHolderItemHandler(InventoryHolderBE inventoryHolderBE, ItemStacksResourceHandler sourceHandler) {
        this.inventoryHolderBE = inventoryHolderBE;
        this.sourceHandler = sourceHandler;
    }

    @Override
    public int size() {
        return sourceHandler.size();
    }

    @Override
    public ItemResource getResource(int index) {
        return sourceHandler.getResource(index);
    }

    @Override
    public long getAmountAsLong(int index) {
        return sourceHandler.getAmountAsLong(index);
    }

    @Override
    public long getCapacityAsLong(int index, ItemResource resource) {
        if (inventoryHolderBE == null) return 0;
        int allowedAmt = inventoryHolderBE.getSlotLimit(index);
        if (allowedAmt == -1)
            return resource.isEmpty() ? Item.ABSOLUTE_MAX_STACK_SIZE : Math.min(resource.getMaxStackSize(), Item.ABSOLUTE_MAX_STACK_SIZE);
        return allowedAmt;
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        if (inventoryHolderBE == null) return false;
        if (inventoryHolderBE.automatedFiltersOnly)
            return inventoryHolderBE.isStackValidFilter(resource.toStack(), index);
        return sourceHandler.isValid(index, resource);
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (inventoryHolderBE == null) return 0;
        if (!isValid(index, resource)) return 0;
        ItemStackKey key = new ItemStackKey(resource.toStack(), inventoryHolderBE.compareNBT);
        int inserted = 0;
        if (inventoryHolderBE.filteredCache.containsKey(key)) {
            List<Integer> slotsList = inventoryHolderBE.filteredCache.get(key);
            if (!slotsList.contains(index)) {
                for (Integer i : slotsList) {
                    int remaining = amount - inserted;
                    if (remaining <= 0) break;
                    inserted += sourceHandler.insert(i, resource, remaining, transaction);
                }
            }
        }
        int remaining = amount - inserted;
        if (remaining > 0) {
            int cap = (int) Math.min(Integer.MAX_VALUE, getCapacityAsLong(index, resource));
            int room = cap - sourceHandler.getAmountAsInt(index);
            if (room > 0) {
                inserted += sourceHandler.insert(index, resource, Math.min(remaining, room), transaction);
            }
        }
        return inserted;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (inventoryHolderBE == null) return 0;
        int amountAllowed = inventoryHolderBE.allowedExtractAmount(index, amount);
        if (amountAllowed <= 0) return 0;
        try (Transaction tx = Transaction.open(transaction)) {
            int extracted = sourceHandler.extract(index, resource, amountAllowed, tx);
            tx.commit();
            return extracted;
        }
    }
}
