package com.direwolf20.justdirethings.common.capabilities;

import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class GeneratorItemHandler extends ItemStacksResourceHandler {
    public GeneratorItemHandler() {
        super(1);
    }

    public GeneratorItemHandler(int size) {
        super(size);
    }

    @Override
    public boolean isValid(int slot, ItemResource resource) {
        return !resource.isEmpty();
    }
}
