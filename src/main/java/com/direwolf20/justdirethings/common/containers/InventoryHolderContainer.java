package com.direwolf20.justdirethings.common.containers;

import com.direwolf20.justdirethings.common.blockentities.InventoryHolderBE;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.common.containers.slots.InventoryHolderSlot;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.direwolf20.justdirethings.util.ItemStackKey;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class InventoryHolderContainer extends BaseMachineContainer {
    static final Identifier[] TEXTURE_EMPTY_SLOTS = new Identifier[]{
            InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS,
            InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET
    };
    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public InventoryHolderBE inventoryHolderBE;
    public InventoryHolderContainer(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, extraData.readBlockPos());
    }

    public InventoryHolderContainer(int windowId, Inventory playerInventory, BlockPos blockPos) {
        super(JDTRegistration.InventoryHolder_Container.get(), windowId, playerInventory, blockPos);
        addPlayerSlots(player.getInventory());
        addArmorSlots(player, player.getInventory());
        if (this.baseMachineBE instanceof InventoryHolderBE inventoryHolderBE)
            this.inventoryHolderBE = inventoryHolderBE;
    }

    public void addMachineSlots() {
        machineHandler = baseMachineBE.getMachineHandler();
        addMachineSlotBox(machineHandler, machineHandler::set, 0, 8, -8, 9, 18, 3, 18);
        addMachineSlotRange(machineHandler, machineHandler::set, 27, 8, 50, 9, 18); //Hotbar
        addMachineArmorSlots(machineHandler, machineHandler::set, player, 36, 44, -28);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, JDTRegistration.InventoryHolder.get());
    }

    public void sendAllItemsToMachine() {
        for (int i = MACHINE_SLOTS; i < MACHINE_SLOTS + Inventory.INVENTORY_SIZE + 5; i++) {
            quickMoveStack(this.player, i); // Move each stack from player's inventory to the machine
        }
    }

    public void sendAllItemsToPlayer() {
        // Loop over all the machine slots
        for (int i = 0; i < MACHINE_SLOTS; i++) {
            // Try to move each item from the machine slots to the player's inventory
            quickMoveStack(this.player, i); // Move the item from machine slot to player's inventory
        }
    }

    public void swapItems() {
        // Loop through the player's inventory slots
        for (int playerSlot = MACHINE_SLOTS; playerSlot < MACHINE_SLOTS + Inventory.INVENTORY_SIZE + 5; playerSlot++) {
            Slot playerInventorySlot = this.slots.get(playerSlot);
            ItemStack playerStack = playerInventorySlot.getItem(); // Get the item from the player's inventory slot

            // Calculate the corresponding machine slot index
            int machineSlot = playerSlot - MACHINE_SLOTS;
            if (machineSlot >= MACHINE_SLOTS) {
                // Out of bounds, skip
                continue;
            }

            Slot machineInventorySlot = this.slots.get(machineSlot);
            ItemStack machineStack = machineInventorySlot.getItem(); // Copy the Machine's Item Stack
            if (playerStack.isEmpty() && machineStack.isEmpty()) continue;
            if (playerSlot >= 77 && playerSlot <= 80 && EnchantmentHelper.has(playerStack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) //Armor Slots
                continue;

            ItemStack machineStackCopy = machineStack.copy();
            ItemStack playerStackCopy = playerStack.copy();

            // Try to insert the player's item into the machine
            if (!playerStack.isEmpty()) {
                machineInventorySlot.set(ItemStack.EMPTY); //Temporarily clear the Machine's Item Stack
                if (moveItemStackTo(playerStack, machineSlot, machineSlot + 1, false) && playerStack.isEmpty()) { //Try to insert into the exact slot, ensure the whole thing fits
                    // If the insertion was successful, swap the items
                    playerInventorySlot.set(machineStackCopy); // Place the machine item in the player's slot
                    machineInventorySlot.setChanged();
                    playerInventorySlot.setChanged();
                } else {
                    machineInventorySlot.set(machineStackCopy); //Set the Machine Slot back
                    playerInventorySlot.set(playerStackCopy); //Do this incase there was a partial insert above
                }
            } else if (!machineStack.isEmpty()) {
                moveItemStackTo(machineStack, playerSlot, playerSlot + 1, false);
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack currentStack = slot.getItem();
            if (index >= 77 && index <= 80 && EnchantmentHelper.has(currentStack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) { //Armor Slots
                return ItemStack.EMPTY;
            }
            if (index < MACHINE_SLOTS) { //Machine Slots to Player Inventory
                if (this.moveItemStackTo(currentStack, index + MACHINE_SLOTS, index + MACHINE_SLOTS + 1, false)) {
                    //No-Op
                } else if (!this.moveItemStackTo(currentStack, MACHINE_SLOTS, MACHINE_SLOTS + Inventory.INVENTORY_SIZE + 4, true)) { //This is +4 to avoid going into offhand slot
                    return ItemStack.EMPTY;
                }
            }
            if (index >= MACHINE_SLOTS) { //Player Inventory to Machine Slots
                if (moveToFilteredSlot(currentStack)) {
                    //No-Op
                } else if (inventoryHolderBE.filtersOnly) {
                    return ItemStack.EMPTY;
                } else if (this.moveItemStackTo(currentStack, index - MACHINE_SLOTS, index - MACHINE_SLOTS + 1, false)) {
                    //No-Op
                } else if (!this.moveItemStackTo(currentStack, 0, MACHINE_SLOTS, false)) {
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

    public boolean moveToFilteredSlot(ItemStack currentStack) {
        if (this.inventoryHolderBE == null) return false;
        ItemStackKey key = new ItemStackKey(currentStack, inventoryHolderBE.compareNBT);

        FilterBasicHandler filteredItems = inventoryHolderBE.filterBasicHandler;
        for (int i = 0; i < filteredItems.size(); i++) {
            ItemStack stack = filteredItems.getResource(i).toStack(filteredItems.getAmountAsInt(i));
            if (stack.isEmpty()) continue;
            if (key.equals(new ItemStackKey(stack, inventoryHolderBE.compareNBT))) {
                if (this.moveItemStackTo(currentStack, i, i + 1, false) && currentStack.isEmpty())
                    return true;
            }
        }
        return false;
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        if (baseMachineBE != null)
            baseMachineBE.markDirtyClient();
    }

    @Override
    protected void addPlayerSlots(Inventory playerInventory) {
        addPlayerSlots(playerInventory, 8, 102);
    }

    @Override
    protected void addPlayerSlots(Inventory playerInventory, int inX, int inY) {
        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, 9 + col + row * 9, inX + col * 18, inY + row * 18));
            }
        }

        // Hotbar
        int hotbarY = inY + 58;
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, inX + col * 18, hotbarY));
        }
    }

    //Overrides for custom slot
    protected int addMachineSlotRange(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int x, int y, int amount, int dx) {
        if (this.baseMachineBE instanceof InventoryHolderBE inventoryHolderBE) {
            for (int i = 0; i < amount; i++) {
                addSlot(new InventoryHolderSlot(handler, slotModifier, index, x, y, inventoryHolderBE));
                x += dx;
                index++;
            }
        }
        return index;
    }

    protected int addMachineSlotBox(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addMachineSlotRange(handler, slotModifier, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    public void addMachineArmorSlots(ResourceHandler<ItemResource> itemHandler, IndexModifier<ItemResource> slotModifier, Player playerEntity, int index, int x, int y) {
        if (this.baseMachineBE instanceof InventoryHolderBE inventoryHolderBE) {
            for (int k = 0; k < 4; ++k) {
                final EquipmentSlot equipmentslot = SLOT_IDS[k];
                InventoryHolderSlot armorSlot = new InventoryHolderSlot(itemHandler, slotModifier, index + k, x + k * 18, y, inventoryHolderBE) {
                    @Override
                    public int getMaxStackSize() {
                        return 1;
                    }

                    @Override
                    public boolean mayPlace(ItemStack p_39746_) {
                        return p_39746_.canEquip(equipmentslot, playerEntity);
                    }
                };
                armorSlot.setBackground(TEXTURE_EMPTY_SLOTS[equipmentslot.getIndex()]);
                this.addSlot(armorSlot);
            }

            InventoryHolderSlot shieldSlot = new InventoryHolderSlot(itemHandler, slotModifier, index + 4, x + 4 * 18, y, inventoryHolderBE);
            shieldSlot.setBackground(InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            this.addSlot(shieldSlot);
        }
    }

    public void addArmorSlots(Player playerEntity, Inventory playerInventory) {
        for (int k = 0; k < 4; ++k) {
            final EquipmentSlot equipmentslot = SLOT_IDS[k];
            Slot armorSlot = new Slot(playerInventory, 39 - k, 44 + k * 18, 82) {
                @Override
                public void setByPlayer(ItemStack p_270969_, ItemStack p_299918_) {
                    onEquipItem(playerEntity, equipmentslot, p_270969_, p_299918_);
                    super.setByPlayer(p_270969_, p_299918_);
                }

                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public boolean mayPlace(ItemStack p_39746_) {
                    return p_39746_.canEquip(equipmentslot, playerEntity);
                }

                @Override
                public boolean mayPickup(Player p_39744_) {
                    ItemStack itemstack = this.getItem();
                    return !itemstack.isEmpty() && !p_39744_.isCreative() && EnchantmentHelper.has(itemstack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE) ? false : super.mayPickup(p_39744_);
                }
            };
            armorSlot.setBackground(TEXTURE_EMPTY_SLOTS[equipmentslot.getIndex()]);
            this.addSlot(armorSlot);
        }

        Slot shieldSlot = new Slot(playerInventory, 40, 44 + 4 * 18, 82) {
            @Override
            public void setByPlayer(ItemStack p_270479_, ItemStack p_299920_) {
                onEquipItem(playerEntity, EquipmentSlot.OFFHAND, p_270479_, p_299920_);
                super.setByPlayer(p_270479_, p_299920_);
            }
        };
        shieldSlot.setBackground(InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
        this.addSlot(shieldSlot);
    }

    static void onEquipItem(Player pPlayer, EquipmentSlot pSlot, ItemStack pNewItem, ItemStack pOldItem) {
        pPlayer.onEquipItem(pSlot, pOldItem, pNewItem);
    }
}
