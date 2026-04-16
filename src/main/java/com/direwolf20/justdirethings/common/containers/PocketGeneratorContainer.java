package com.direwolf20.justdirethings.common.containers;

import com.direwolf20.justdirethings.common.containers.basecontainers.BaseContainer;
import com.direwolf20.justdirethings.common.containers.slots.FuelSlot;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemAccessItemHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class PocketGeneratorContainer extends BaseContainer {
    public static final int SLOTS = 1;
    public ItemAccessItemHandler handler;
    public ItemStack pocketGeneratorItemStack;
    public Player playerEntity;

    public PocketGeneratorContainer(int windowId, Inventory playerInventory, Player player, RegistryFriendlyByteBuf extraData) {
        this(windowId, playerInventory, player, ItemStack.OPTIONAL_STREAM_CODEC.decode(extraData));
    }

    public PocketGeneratorContainer(int windowId, Inventory playerInventory, Player player, ItemStack pocketGenerator) {
        super(Registration.PocketGenerator_Container.get(), windowId);
        playerEntity = player;
        handler = new ItemAccessItemHandler(ItemAccess.forStack(pocketGenerator), JustDireDataComponents.ITEMSTACK_HANDLER.get(), 1);
        this.pocketGeneratorItemStack = pocketGenerator;
        addGeneratorSlots(handler, transactionalSet(handler), 0, 80, 35, 1, 18);

        addPlayerSlots(playerInventory, 8, 84);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return playerIn.getMainHandItem().equals(pocketGeneratorItemStack);
    }

    protected int addGeneratorSlots(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new FuelSlot(handler, slotModifier, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    static IndexModifier<ItemResource> transactionalSet(ResourceHandler<ItemResource> handler) {
        return (idx, res, amount) -> {
            try (Transaction tx = Transaction.openRoot()) {
                int current = handler.getAmountAsInt(idx);
                if (current > 0) {
                    handler.extract(idx, handler.getResource(idx), current, tx);
                }
                if (!res.isEmpty() && amount > 0) {
                    handler.insert(idx, res, amount, tx);
                }
                tx.commit();
            }
        };
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
