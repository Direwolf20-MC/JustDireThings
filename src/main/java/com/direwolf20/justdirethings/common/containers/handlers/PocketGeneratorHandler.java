package com.direwolf20.justdirethings.common.containers.handlers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class PocketGeneratorHandler extends ItemStackHandler {
    public ItemStack stack;

    public PocketGeneratorHandler(int size, ItemStack itemStack) {
        super(size);
        this.stack = itemStack;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return CommonHooks.getBurnTime(stack, RecipeType.SMELTING) > 0; //Todo Test Lava bucket
    }
}
