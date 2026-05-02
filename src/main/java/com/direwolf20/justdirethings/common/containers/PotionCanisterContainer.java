package com.direwolf20.justdirethings.common.containers;

import com.direwolf20.justdirethings.common.containers.basecontainers.BaseContainer;
import com.direwolf20.justdirethings.common.containers.handlers.PotionCanisterHandler;
import com.direwolf20.justdirethings.common.containers.slots.PotionSlot;
import com.direwolf20.justdirethings.common.items.PotionCanister;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class PotionCanisterContainer extends BaseContainer {
    public static final int SLOTS = 1;
    public PotionCanisterHandler handler;
    public ItemStack potionCanister;
    public Player playerEntity;

    public PotionCanisterContainer(int windowId, Inventory playerInventory, Player player, RegistryFriendlyByteBuf extraData) {
        this(windowId, playerInventory, player, ItemStack.OPTIONAL_STREAM_CODEC.decode(extraData));
    }

    public PotionCanisterContainer(int windowId, Inventory playerInventory, Player player, ItemStack potionCanister) {
        super(JDTRegistration.PotionCanister_Container.get(), windowId);
        playerEntity = player;
        handler = new PotionCanisterHandler(potionCanister, JustDireDataComponents.TOOL_CONTENTS.get(), 1);
        this.potionCanister = potionCanister;
        addItemSlots(handler, PocketGeneratorContainer.transactionalSet(handler), 0, 80, 35, 1, 18);

        addPlayerSlots(playerInventory, 8, 84);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return playerIn.getMainHandItem().equals(potionCanister);
    }

    protected int addItemSlots(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new PotionSlot(handler, slotModifier, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    @Override
    public void broadcastChanges() {
        if (playerEntity != null && !playerEntity.level().isClientSide()) {
            tryConsumeInsertedPotion();
        }
        super.broadcastChanges();
    }

    private void tryConsumeInsertedPotion() {
        ResourceHandler<ItemResource> capHandler = ItemAccess.forStack(potionCanister).getCapability(Capabilities.Item.ITEM);
        if (capHandler == null) return;
        ItemResource resource = capHandler.getResource(0);
        if (resource.isEmpty() || !(resource.getItem() instanceof PotionItem)) return;

        PotionContents currentContents = PotionCanister.getPotionContents(potionCanister);
        PotionContents newContents = resource.toStack(1).getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        if (!currentContents.equals(PotionContents.EMPTY) && !currentContents.equals(newContents)) return;

        int currentAmt = PotionCanister.getPotionAmount(potionCanister);
        if (currentAmt + 250 > PotionCanister.getMaxMB()) return;

        boolean committed = false;
        try (Transaction tx = Transaction.openRoot()) {
            int extracted = capHandler.extract(0, resource, 1, tx);
            if (extracted != 1) return;
            ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
            int inserted = capHandler.insert(0, ItemResource.of(bottle), bottle.getCount(), tx);
            if (inserted != bottle.getCount()) return;
            tx.commit();
            committed = true;
        }
        if (committed) {
            PotionCanister.setPotionContents(potionCanister, newContents);
            PotionCanister.addPotionAmount(potionCanister, 250);
        }
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
