package com.direwolf20.justdirethings.common.containers;

import com.direwolf20.justdirethings.common.containers.basecontainers.BaseContainer;
import com.direwolf20.justdirethings.common.containers.handlers.PotionCanisterHandler;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class PotionCanisterContainer extends BaseContainer {
    public static final int SLOTS = 1;
    public PotionCanisterHandler handler;
    public ItemStack potionCanister;
    public Player playerEntity;

    public PotionCanisterContainer(int windowId, Inventory playerInventory, Player player, RegistryFriendlyByteBuf extraData) {
        this(windowId, playerInventory, player, ItemStack.OPTIONAL_STREAM_CODEC.decode(extraData));
    }

    public PotionCanisterContainer(int windowId, Inventory playerInventory, Player player, ItemStack potionCanister) {
        super(Registration.PotionCanister_Container.get(), windowId);
        playerEntity = player;
        handler = new PotionCanisterHandler(potionCanister, JustDireDataComponents.TOOL_CONTENTS.get(), 1);
        this.potionCanister = potionCanister;
        if (handler != null)
            addItemSlots(handler, 0, 80, 35, 1, 18);

        addPlayerSlots(playerInventory, 8, 84);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return playerIn.getMainHandItem().equals(potionCanister);
    }

    protected int addItemSlots(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, i, x, y) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof PotionItem;
                }
            });
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
                slot.set(currentStack);
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
