package com.direwolf20.justdirethings.common.capabilities;

import net.minecraft.world.item.ItemStack;
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
        // TODO(port, stage-7): getBurnTime now requires FuelValues which depends on Level;
        // at handler-construction time we have neither. Allow any non-empty item here;
        // concrete BE logic gates actual burning via burnTime>0 check in doBurn().
        return !itemStack.isEmpty();
    }
}
