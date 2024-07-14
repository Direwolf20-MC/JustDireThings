package com.direwolf20.justdirethings.common.containers;

import com.direwolf20.justdirethings.common.containers.basecontainers.BaseContainer;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ToolSettingContainer extends BaseContainer {
    public Player playerEntity;
    public static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET = ResourceLocation.parse("item/empty_armor_slot_helmet");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE = ResourceLocation.parse("item/empty_armor_slot_chestplate");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS = ResourceLocation.parse("item/empty_armor_slot_leggings");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS = ResourceLocation.parse("item/empty_armor_slot_boots");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_SHIELD = ResourceLocation.parse("item/empty_armor_slot_shield");
    static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{
            EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET
    };
    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public final List<Slot> dynamicSlots = new ArrayList<>();
    public ComponentItemHandler componentItemHandler;


    public ToolSettingContainer(int windowId, Inventory playerInventory, Player player, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, player);
    }

    public ToolSettingContainer(int windowId, Inventory playerInventory, Player player) {
        super(Registration.Tool_Settings_Container.get(), windowId);
        playerEntity = player;
        for (int k = 0; k < 4; ++k) {
            final EquipmentSlot equipmentslot = SLOT_IDS[k];
            this.addSlot(new Slot(playerInventory, 39 - k, 44 + k * 18, 66) {
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

        this.addSlot(new Slot(playerInventory, 40, 44 + 4 * 18, 66) {
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

        addPlayerSlots(playerInventory, 8, 84);

        refreshSlots(player.getMainHandItem());
    }

    private void addSelectedItemSlots() {
        for (int i = 0; i < componentItemHandler.getSlots(); i++) { // Example slot count
            int x = 134 + (i % 2) * 18; // 2 slots per row
            int y = 66 - (i / 2) * 18; // 2 rows
            Slot slot = new SlotItemHandler(componentItemHandler, i, x, y) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return true; // Define valid items for bow slots
                }
            };
            this.addSlot(slot);
            dynamicSlots.add(slot);
        }
    }

    public void clearDynamicSlots() {
        for (Slot slot : dynamicSlots) {
            this.slots.remove(slot);
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

    public ComponentItemHandler getItemSlots(ItemStack itemStack) {
        IItemHandler itemHandler = itemStack.getCapability(Capabilities.ItemHandler.ITEM);
        if (itemHandler instanceof ComponentItemHandler componentItemHandler)
            return componentItemHandler;
        return null;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
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
