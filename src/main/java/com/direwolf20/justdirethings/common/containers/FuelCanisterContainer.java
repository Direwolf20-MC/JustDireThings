package com.direwolf20.justdirethings.common.containers;

import com.direwolf20.justdirethings.common.containers.handlers.FuelCanisterHandler;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class FuelCanisterContainer extends BaseContainer {
    public static final int SLOTS = 1;
    public FuelCanisterHandler handler;
    public ItemStack fuelCanisterItemstack;
    public Player playerEntity;

    public FuelCanisterContainer(int windowId, Inventory playerInventory, Player player, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, player, extraData.readItem());
    }

    public FuelCanisterContainer(int windowId, Inventory playerInventory, Player player, ItemStack fuelCanister) {
        super(Registration.FuelCanister_Container.get(), windowId);
        playerEntity = player;
        this.handler = new FuelCanisterHandler(SLOTS, fuelCanister);
        this.fuelCanisterItemstack = fuelCanister;
        if (handler != null)
            addSlotRange(handler, 0, 80, 35, 1, 18);

        addPlayerSlots(playerInventory, 8, 84);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return playerIn.getMainHandItem().equals(fuelCanisterItemstack);
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
        Level world = playerIn.level();
        if (!world.isClientSide) {
            ItemStack containerItem = handler.getStackInSlot(0);
            if (!containerItem.isEmpty())
                ItemHandlerHelper.giveItemToPlayer(playerIn, containerItem);
        }
        super.removed(playerIn);
    }
}
