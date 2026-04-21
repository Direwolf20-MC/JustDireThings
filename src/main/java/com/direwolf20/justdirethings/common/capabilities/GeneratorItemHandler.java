package com.direwolf20.justdirethings.common.capabilities;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jetbrains.annotations.Nullable;

public class GeneratorItemHandler extends ItemStacksResourceHandler {
    @Nullable
    private BlockEntity holder;

    public GeneratorItemHandler() {
        super(1);
    }

    public GeneratorItemHandler(int size) {
        super(size);
    }

    public void setHolder(@Nullable BlockEntity holder) {
        this.holder = holder;
    }

    @Override
    public boolean isValid(int slot, ItemResource resource) {
        if (resource.isEmpty()) return false;
        Level level = holder == null ? null : holder.getLevel();
        if (level == null) return true;
        return resource.toStack().getBurnTime(RecipeType.SMELTING, level.fuelValues()) > 0;
    }
}
