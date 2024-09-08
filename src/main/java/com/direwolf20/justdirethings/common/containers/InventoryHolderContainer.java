package com.direwolf20.justdirethings.common.containers;

import com.direwolf20.justdirethings.common.blockentities.InventoryHolderBE;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.common.containers.slots.InventoryHolderSlot;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.ItemStackKey;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.neoforge.items.IItemHandler;

public class InventoryHolderContainer extends BaseMachineContainer {
    public static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET = ResourceLocation.parse("item/empty_armor_slot_helmet");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE = ResourceLocation.parse("item/empty_armor_slot_chestplate");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS = ResourceLocation.parse("item/empty_armor_slot_leggings");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS = ResourceLocation.parse("item/empty_armor_slot_boots");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_SHIELD = ResourceLocation.parse("item/empty_armor_slot_shield");
    static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{
            EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET
    };
    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public InventoryHolderBE inventoryHolderBE;
    public InventoryHolderContainer(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, extraData.readBlockPos());
    }

    public InventoryHolderContainer(int windowId, Inventory playerInventory, BlockPos blockPos) {
        super(Registration.InventoryHolder_Container.get(), windowId, playerInventory, blockPos);
        addPlayerSlots(player.getInventory());
        addArmorSlots(player, player.getInventory());
        if (this.baseMachineBE instanceof InventoryHolderBE inventoryHolderBE)
            this.inventoryHolderBE = inventoryHolderBE;
    }

    public void addMachineSlots() {
        machineHandler = baseMachineBE.getMachineHandler();
        addMachineSlotRange(machineHandler, 0, 8, 50, 9, 18); //Hotbar
        addMachineSlotBox(machineHandler, 9, 8, -8, 9, 18, 3, 18);
        addMachineArmorSlots(machineHandler, player, 36, 44, -28);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, Registration.InventoryHolder.get());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack currentStack = slot.getItem();
            if (index < MACHINE_SLOTS) { //Machine Slots to Player Inventory
                if (!this.moveItemStackTo(currentStack, MACHINE_SLOTS, MACHINE_SLOTS + Inventory.INVENTORY_SIZE, true)) {
                    return ItemStack.EMPTY;
                }
            }
            if (index >= MACHINE_SLOTS) { //Player Inventory to Machine Slots
                if (!this.moveItemStackTo(currentStack, 0, MACHINE_SLOTS, false)) {
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
        for (int i = 0; i < filteredItems.getSlots(); i++) {
            ItemStack stack = filteredItems.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (key.equals(new ItemStackKey(stack, inventoryHolderBE.compareNBT))) {
                int amtNeeded = stack.getCount() - machineHandler.getStackInSlot(i).getCount();
                ItemStack sendStack = currentStack.split(amtNeeded);
                if (this.moveItemStackTo(sendStack, i, i + 1, false))
                    return true;
                else
                    currentStack.grow(sendStack.getCount());
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

    //Overrides for custom slot
    protected int addMachineSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        if (this.baseMachineBE instanceof InventoryHolderBE inventoryHolderBE) {
            for (int i = 0; i < amount; i++) {
                addSlot(new InventoryHolderSlot(handler, index, x, y, inventoryHolderBE));
                x += dx;
                index++;
            }
        }
        return index;
    }

    protected int addMachineSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addMachineSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    public void addMachineArmorSlots(IItemHandler itemHandler, Player playerEntity, int index, int x, int y) {
        if (this.baseMachineBE instanceof InventoryHolderBE inventoryHolderBE) {
            for (int k = 0; k < 4; ++k) {
                final EquipmentSlot equipmentslot = SLOT_IDS[k];
                this.addSlot(new InventoryHolderSlot(itemHandler, index + k, x + k * 18, y, inventoryHolderBE) {
                    @Override
                    public int getMaxStackSize() {
                        return 1;
                    }

                    @Override
                    public boolean mayPlace(ItemStack p_39746_) {
                        return p_39746_.canEquip(equipmentslot, playerEntity);
                    }

                    @Override
                    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                        return Pair.of(InventoryMenu.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[equipmentslot.getIndex()]);
                    }
                });
            }

            this.addSlot(new InventoryHolderSlot(itemHandler, index + 4, x + 4 * 18, y, inventoryHolderBE) {
                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_ARMOR_SLOT_SHIELD);
                }
            });
        }
    }

    public void addArmorSlots(Player playerEntity, Inventory playerInventory) {
        for (int k = 0; k < 4; ++k) {
            final EquipmentSlot equipmentslot = SLOT_IDS[k];
            this.addSlot(new Slot(playerInventory, 39 - k, 44 + k * 18, 82) {
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

                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[equipmentslot.getIndex()]);
                }
            });
        }

        this.addSlot(new Slot(playerInventory, 40, 44 + 4 * 18, 82) {
            @Override
            public void setByPlayer(ItemStack p_270479_, ItemStack p_299920_) {
                onEquipItem(playerEntity, EquipmentSlot.OFFHAND, p_270479_, p_299920_);
                super.setByPlayer(p_270479_, p_299920_);
            }

            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_ARMOR_SLOT_SHIELD);
            }
        });
    }

    static void onEquipItem(Player pPlayer, EquipmentSlot pSlot, ItemStack pNewItem, ItemStack pOldItem) {
        pPlayer.onEquipItem(pSlot, pOldItem, pNewItem);
    }
}
