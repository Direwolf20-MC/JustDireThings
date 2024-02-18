package com.direwolf20.justdirethings.common.containers.handlers;

import com.direwolf20.justdirethings.common.items.FuelCanister;
import com.direwolf20.justdirethings.datagen.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class FuelCanisterHandler extends ItemStackHandler {
    public ItemStack stack;

    public FuelCanisterHandler(int size, ItemStack itemStack) {
        super(size);
        this.stack = itemStack;
    }

    @Override
    protected void onContentsChanged(int slot) {
        ItemStack fuelStack = this.getStackInSlot(slot);
        if (!stack.isEmpty() && !fuelStack.isEmpty()) {
            FuelCanister.incrementFuel(stack, fuelStack);
        }
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return !(stack.getItem() instanceof FuelCanister) && CommonHooks.getBurnTime(stack, RecipeType.SMELTING) > 0 && !stack.hasCraftingRemainingItem() && !stack.is(ItemTags.FUEL_CANISTER_DENY);
    }
}
