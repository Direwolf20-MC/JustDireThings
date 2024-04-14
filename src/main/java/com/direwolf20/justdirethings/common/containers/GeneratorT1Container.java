package com.direwolf20.justdirethings.common.containers;

import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.containers.slots.FuelSlot;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class GeneratorT1Container extends BaseMachineContainer {

    public GeneratorT1Container(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, extraData.readBlockPos());
    }

    public GeneratorT1Container(int windowId, Inventory playerInventory, BlockPos blockPos) {
        super(Registration.GeneratorT1_Container.get(), windowId, playerInventory, blockPos);
        addPlayerSlots(player.getInventory());
    }

    @Override
    public void addMachineSlots() {
        machineHandler = baseMachineBE.getMachineHandler();
        addFuelSlotRange(machineHandler, 0, 80, 13, 1, 18);
    }

    public int getBurnRemaining() {
        return this.data == null ? 0 : this.data.get(2);
    }

    public int getMaxBurn() {
        return this.data == null ? 0 : this.data.get(3);
    }

    protected int addFuelSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new FuelSlot(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, Registration.GeneratorT1.get());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        return super.quickMoveStack(playerIn, index);
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
    }
}
