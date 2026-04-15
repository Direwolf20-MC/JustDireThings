package com.direwolf20.justdirethings.common.capabilities;

import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class EnergyStorageItemStackNoReceive extends EnergyStorageItemstack {

    public EnergyStorageItemStackNoReceive(ItemAccess itemAccess, int capacity) {
        // Block external insertion; allow full extraction.
        super(itemAccess, capacity, 0, capacity);
    }

    /**
     * Pocket Generators need to be able to power themselves, but not receive energy from anything else.
     * This bypasses the maxInsert=0 limit set in the constructor.
     */
    public int forceReceiveEnergy(int maxReceive, boolean simulate) {
        if (maxReceive <= 0) return 0;

        int accessAmount = itemAccess.getAmount();
        if (accessAmount == 0) return 0;

        ItemResource accessResource = itemAccess.getResource();
        if (!accessResource.is(validItem)) return 0;

        int amountPerItem = maxReceive / accessAmount;
        if (amountPerItem == 0) return 0;

        int currentAmountPerItem = getAmountFrom(accessResource);
        int insertedPerItem = Math.min(amountPerItem, capacity - currentAmountPerItem);
        if (insertedPerItem <= 0) return 0;

        ItemResource filledResource = update(accessResource, currentAmountPerItem + insertedPerItem);
        if (filledResource.isEmpty()) return 0;

        try (Transaction tx = Transaction.openRoot()) {
            int exchanged = itemAccess.exchange(filledResource, accessAmount, tx);
            if (exchanged <= 0) return 0;
            if (!simulate) tx.commit();
            return insertedPerItem * exchanged;
        }
    }

    public int forceReceiveEnergy(int maxReceive, TransactionContext transaction) {
        if (maxReceive <= 0) return 0;

        int accessAmount = itemAccess.getAmount();
        if (accessAmount == 0) return 0;

        ItemResource accessResource = itemAccess.getResource();
        if (!accessResource.is(validItem)) return 0;

        int amountPerItem = maxReceive / accessAmount;
        if (amountPerItem == 0) return 0;

        int currentAmountPerItem = getAmountFrom(accessResource);
        int insertedPerItem = Math.min(amountPerItem, capacity - currentAmountPerItem);
        if (insertedPerItem <= 0) return 0;

        ItemResource filledResource = update(accessResource, currentAmountPerItem + insertedPerItem);
        if (filledResource.isEmpty()) return 0;

        return insertedPerItem * itemAccess.exchange(filledResource, accessAmount, transaction);
    }
}
