package com.direwolf20.justdirethings.common.containers.basecontainers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import javax.annotation.Nullable;

public abstract class BaseContainer extends AbstractContainerMenu {
    public BaseContainer(@Nullable MenuType<?> p_i50105_1_, int p_i50105_2_) {
        super(p_i50105_1_, p_i50105_2_);
    }

    protected void addPlayerSlots(Inventory playerInventory, int inX, int inY) {
        // Player inventory (rows 1..3 = slots 9..35)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, 9 + col + row * 9, inX + col * 18, inY + row * 18));
            }
        }

        // Hotbar (slots 0..8)
        int hotbarY = inY + 58;
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, inX + col * 18, hotbarY));
        }
    }

    protected void addPlayerSlots(Inventory playerInventory) {
        addPlayerSlots(playerInventory, 8, 84);
    }

    protected int addSlotRange(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new ResourceHandlerSlot(handler, slotModifier, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    protected int addSlotBox(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, slotModifier, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    protected ItemStack quickMoveBasicFilter(ItemStack currentStack, int startSlot, int SLOTS) {
        for (int i = startSlot; i < startSlot + SLOTS; i++) { //Prevents the same item from going in there more than once.
            if (ItemStack.isSameItemSameComponents(this.slots.get(i).getItem(), currentStack)) //Don't limit tags
                return ItemStack.EMPTY;
        }
        if (!this.moveItemStackTo(currentStack, startSlot, startSlot + SLOTS, false)) {
            return ItemStack.EMPTY;
        }
        return currentStack;
    }
}
