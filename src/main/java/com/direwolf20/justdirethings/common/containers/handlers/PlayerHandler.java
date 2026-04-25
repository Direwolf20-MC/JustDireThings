package com.direwolf20.justdirethings.common.containers.handlers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.PlayerInventoryWrapper;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class PlayerHandler extends DelegatingResourceHandler<ItemResource> {
    public final Player player;
    public final InventoryType inventoryType;

    public enum InventoryType {
        Inventory,
        Armor,
        Offhand,
        MainInventory,
        Hotbar;

        public InventoryType next() {
            InventoryType[] values = values();
            int nextOrdinal = (this.ordinal() + 1) % values.length;
            return values[nextOrdinal];
        }
    }

    public PlayerHandler(Player player, InventoryType inventoryType) {
        super(buildDelegate(player, inventoryType));
        this.player = player;
        this.inventoryType = inventoryType;
    }

    private static ResourceHandler<ItemResource> buildDelegate(Player player, InventoryType inventoryType) {
        PlayerInventoryWrapper wrapper = PlayerInventoryWrapper.of(player);
        return switch (inventoryType) {
            case Inventory -> RangedResourceHandler.of(wrapper, 0, Inventory.INVENTORY_SIZE);
            case Armor -> RangedResourceHandler.of(wrapper, Inventory.INVENTORY_SIZE, Inventory.SLOT_OFFHAND);
            case Offhand -> RangedResourceHandler.ofSingleIndex(wrapper, Inventory.SLOT_OFFHAND);
            case MainInventory -> RangedResourceHandler.of(wrapper, Inventory.SELECTION_SIZE, Inventory.INVENTORY_SIZE);
            case Hotbar -> RangedResourceHandler.of(wrapper, 0, Inventory.SELECTION_SIZE);
        };
    }

    public Inventory getInventory() {
        if (player == null) return null;
        return player.getInventory();
    }

    public boolean isPlayerInvalid() {
        return player == null || player.isRemoved();
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (isPlayerInvalid()) return 0;
        return super.insert(index, resource, amount, transaction);
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (isPlayerInvalid()) return 0;
        return super.extract(index, resource, amount, transaction);
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        if (isPlayerInvalid()) return false;
        return super.isValid(index, resource);
    }

    @Override
    public ItemResource getResource(int index) {
        if (isPlayerInvalid()) return ItemResource.EMPTY;
        return super.getResource(index);
    }

    @Override
    public long getAmountAsLong(int index) {
        if (isPlayerInvalid()) return 0;
        return super.getAmountAsLong(index);
    }
}
