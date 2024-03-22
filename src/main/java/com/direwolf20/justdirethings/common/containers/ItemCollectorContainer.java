package com.direwolf20.justdirethings.common.containers;

import com.direwolf20.justdirethings.common.blockentities.ItemCollectorBE;
import com.direwolf20.justdirethings.common.containers.basecontainers.AreaAffectingContainer;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.common.containers.slots.FilterBasicSlot;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ItemCollectorContainer extends AreaAffectingContainer {
    public static final int SLOTS = 9;
    public FilterBasicHandler handler;
    private Player player;
    private BlockPos pos;

    public ItemCollectorContainer(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, extraData.readBlockPos());
    }

    public ItemCollectorContainer(int windowId, Inventory playerInventory, BlockPos blockPos) {
        super(Registration.Item_Collector_Container.get(), windowId);
        this.pos = blockPos;
        this.player = playerInventory.player;
        BlockEntity blockEntity = player.level().getBlockEntity(pos);
        if (blockEntity instanceof ItemCollectorBE itemCollectorBE) {
            this.areaAffectingBE = itemCollectorBE;
            handler = itemCollectorBE.getHandler();
            addSlotRange(handler, 0, 8, 63, 9, 18);
        }
        addPlayerSlots(player.getInventory());
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, Registration.ItemCollector.get());
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (slotId >= 0 && slotId < SLOTS) {
            return;
        }
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            if (index >= SLOTS) { //Only do this if we click from the players inventory
                ItemStack currentStack = slot.getItem().copy();
                currentStack.setCount(1);
                return quickMoveBasicFilter(currentStack, SLOTS);
            }
        }
        return itemstack;
    }

    @Override
    protected int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            if (handler instanceof FilterBasicHandler)
                addSlot(new FilterBasicSlot(handler, index, x, y));
            else
                addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
    }
}
