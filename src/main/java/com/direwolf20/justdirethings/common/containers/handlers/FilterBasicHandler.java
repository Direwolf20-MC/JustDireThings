package com.direwolf20.justdirethings.common.containers.handlers;

import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class FilterBasicHandler extends ItemStacksResourceHandler {
    public FilterBasicHandler(int size) {
        super(size);
    }

    @Override
    protected int getCapacity(int index, ItemResource resource) {
        return 1;
    }
}
