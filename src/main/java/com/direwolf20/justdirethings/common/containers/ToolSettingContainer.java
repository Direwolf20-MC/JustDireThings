package com.direwolf20.justdirethings.common.containers;

import com.direwolf20.justdirethings.common.containers.basecontainers.BaseContainer;
import com.direwolf20.justdirethings.common.items.PotionCanister;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import java.util.ArrayList;
import java.util.List;

public class ToolSettingContainer extends BaseContainer {
    public Player playerEntity;
    static final Identifier[] TEXTURE_EMPTY_SLOTS = new Identifier[]{
            InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET
    };
    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public final List<Slot> dynamicSlots = new ArrayList<>();
    public ResourceHandler<ItemResource> componentItemHandler;


    public ToolSettingContainer(int windowId, Inventory playerInventory, Player player, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, player);
    }

    public ToolSettingContainer(int windowId, Inventory playerInventory, Player player) {
        super(JDTRegistration.Tool_Settings_Container.get(), windowId);
        playerEntity = player;

        addPlayerSlots(playerInventory, 8, 84);

        for (int k = 0; k < 4; ++k) {
            final EquipmentSlot equipmentslot = SLOT_IDS[k];
            Slot armorSlot = new Slot(playerInventory, 39 - k, 44 + k * 18, 66) {
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

        Slot shieldSlot = new Slot(playerInventory, 40, 44 + 4 * 18, 66) {
            @Override
            public void setByPlayer(ItemStack p_270479_, ItemStack p_299920_) {
                onEquipItem(playerEntity, EquipmentSlot.OFFHAND, p_270479_, p_299920_);
                super.setByPlayer(p_270479_, p_299920_);
            }
        };
        shieldSlot.setBackground(InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
        this.addSlot(shieldSlot);

        refreshSlots(player.getMainHandItem());
    }

    private void addSelectedItemSlots() {
        for (int i = 0; i < componentItemHandler.size(); i++) { // Example slot count
            int x = 134 + (i % 2) * 18; // 2 slots per row
            int y = 66 - (i / 2) * 18; // 2 rows
            Slot slot = new ResourceHandlerSlot(componentItemHandler, PocketGeneratorContainer.transactionalSet(componentItemHandler), i, x, y) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof PotionCanister; // Define valid items for bow slots
                }
            };
            this.addSlot(slot);
            dynamicSlots.add(slot);
        }
    }

    public void clearDynamicSlots() {
        for (Slot slot : dynamicSlots) {
            this.slots.remove(slot);
            this.remoteSlots.remove(remoteSlots.size() - 1);
        }
        dynamicSlots.clear();
    }

    public void refreshSlots(ItemStack selectedStack) {
        clearDynamicSlots();
        componentItemHandler = getItemSlots(selectedStack);
        if (componentItemHandler != null) {
            addSelectedItemSlots();
        }
    }

    public ResourceHandler<ItemResource> getItemSlots(ItemStack itemStack) {
        return ItemAccess.forStack(itemStack).getCapability(Capabilities.Item.ITEM);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        if (!dynamicSlots.isEmpty()) {
            Slot slot = this.slots.get(index);
            if (slot.hasItem()) {
                ItemStack currentStack = slot.getItem();
                if (index >= Inventory.INVENTORY_SIZE + 5) { //Dynamic Slots to Player Inventory
                    if (!this.moveItemStackTo(currentStack, 0, Inventory.INVENTORY_SIZE, true)) {
                        return ItemStack.EMPTY;
                    }
                }
                if (index < Inventory.INVENTORY_SIZE) { //Player Inventory to Dynamic Slots
                    if (!this.moveItemStackTo(currentStack, Inventory.INVENTORY_SIZE + 5, Inventory.INVENTORY_SIZE + 5 + dynamicSlots.size(), false)) {
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
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    static void onEquipItem(Player pPlayer, EquipmentSlot pSlot, ItemStack pNewItem, ItemStack pOldItem) {
        pPlayer.onEquipItem(pSlot, pOldItem, pNewItem);
    }
}
