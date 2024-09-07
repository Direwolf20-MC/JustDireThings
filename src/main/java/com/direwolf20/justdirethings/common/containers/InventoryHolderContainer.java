package com.direwolf20.justdirethings.common.containers;

import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.setup.Registration;
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
import net.neoforged.neoforge.items.SlotItemHandler;

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

    public InventoryHolderContainer(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, extraData.readBlockPos());
    }

    public InventoryHolderContainer(int windowId, Inventory playerInventory, BlockPos blockPos) {
        super(Registration.InventoryHolder_Container.get(), windowId, playerInventory, blockPos);
        addPlayerSlots(player.getInventory());
        addArmorSlots(player, player.getInventory());
    }

    public void addMachineSlots() {
        machineHandler = baseMachineBE.getMachineHandler();
        addSlotRange(machineHandler, 0, 8, 50, 9, 18); //Hotbar
        addSlotBox(machineHandler, 9, 8, -8, 9, 18, 3, 18);
        addArmorSlots(machineHandler, player, 36, 44, -28);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, Registration.InventoryHolder.get());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        return super.quickMoveStack(playerIn, index);
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

    public void addArmorSlots(IItemHandler itemHandler, Player playerEntity, int index, int x, int y) {
        for (int k = 0; k < 4; ++k) {
            final EquipmentSlot equipmentslot = SLOT_IDS[k];
            this.addSlot(new SlotItemHandler(itemHandler, index + k, x + k * 18, y) {
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

        this.addSlot(new SlotItemHandler(itemHandler, index + 4, x + 4 * 18, y) {
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_ARMOR_SLOT_SHIELD);
            }
        });
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
