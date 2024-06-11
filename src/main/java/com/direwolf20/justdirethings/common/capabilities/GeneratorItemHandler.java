package com.direwolf20.justdirethings.common.capabilities;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.items.ItemStackHandler;

public class GeneratorItemHandler extends ItemStackHandler {
    public GeneratorItemHandler() {
        super(1);
    }

    public GeneratorItemHandler(int size) {
        super(size);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack itemStack) {
        return itemStack.getBurnTime(RecipeType.SMELTING) > 0;
    }
}
