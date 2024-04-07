package com.direwolf20.justdirethings.common.containers;

import com.direwolf20.justdirethings.common.containers.basecontainers.BaseContainer;
import com.direwolf20.justdirethings.common.containers.slots.FuelSlot;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class PocketGeneratorContainer extends BaseContainer {
    public static final int SLOTS = 1;
    public ItemStackHandler handler;
    public ItemStack pocketGeneratorItemStack;
    public Player playerEntity;

    public PocketGeneratorContainer(int windowId, Inventory playerInventory, Player player, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, player, extraData.readItem());
    }

    public PocketGeneratorContainer(int windowId, Inventory playerInventory, Player player, ItemStack pocketGenerator) {
        super(Registration.PocketGenerator_Container.get(), windowId);
        playerEntity = player;
        handler = pocketGenerator.getData(Registration.HANDLER);
        this.pocketGeneratorItemStack = pocketGenerator;
        if (handler != null)
            addGeneratorSlots(handler, 0, 80, 35, 1, 18);

        addPlayerSlots(playerInventory, 8, 84);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return playerIn.getMainHandItem().equals(pocketGeneratorItemStack);
    }

    protected int addGeneratorSlots(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new FuelSlot(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack currentStack = slot.getItem();
            if (index < SLOTS) { //Slot to Player Inventory
                if (!this.moveItemStackTo(currentStack, SLOTS, Inventory.INVENTORY_SIZE + SLOTS, true)) {
                    return ItemStack.EMPTY;
                }
            }
            if (index >= SLOTS) { //Player Inventory to Slots
                if (!this.moveItemStackTo(currentStack, 0, SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (currentStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (currentStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, currentStack);
        }
        return itemstack;
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
    }
}
