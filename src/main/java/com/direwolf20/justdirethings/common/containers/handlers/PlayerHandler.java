package com.direwolf20.justdirethings.common.containers.handlers;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

public class PlayerHandler extends ItemStackHandler {
    public final Player player;
    public final InventoryType inventoryType;

    public enum InventoryType {
        Inventory,
        Armor,
        Offhand;

        public InventoryType next() {
            InventoryType[] values = values();
            int nextOrdinal = (this.ordinal() + 1) % values.length;
            return values[nextOrdinal];
        }
    }

    public PlayerHandler(Player player, InventoryType inventoryType) {
        super(inventoryType.equals(InventoryType.Inventory) ? player.getInventory().items : inventoryType.equals(InventoryType.Armor) ? player.getInventory().armor : player.getInventory().offhand);
        this.player = player;
        this.inventoryType = inventoryType;
    }

    public Inventory getInventory() {
        if (player == null) return null;
        return player.getInventory();
    }

    public boolean isPlayerInvalid() {
        return player == null || player.isRemoved();
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (isPlayerInvalid()) return;
        super.setStackInSlot(slot, stack);
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (isPlayerInvalid()) return ItemStack.EMPTY;
        return super.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (isPlayerInvalid())
            return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (isPlayerInvalid()) return ItemStack.EMPTY;
        if (inventoryType == InventoryType.Armor && EnchantmentHelper.has(super.getStackInSlot(slot), EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE))
            return ItemStack.EMPTY;
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (isPlayerInvalid()) return false;
        if (inventoryType.equals(InventoryType.Inventory))
            return true;
        if (inventoryType.equals(InventoryType.Armor)) {
            if (slot == 0)
                return stack.canEquip(EquipmentSlot.FEET, player);
            if (slot == 1)
                return stack.canEquip(EquipmentSlot.LEGS, player);
            if (slot == 2)
                return stack.canEquip(EquipmentSlot.CHEST, player);
            if (slot == 3)
                return stack.canEquip(EquipmentSlot.HEAD, player);
        }
        if (inventoryType.equals(InventoryType.Offhand)) {
            return true;
        }
        return false;
    }
}
