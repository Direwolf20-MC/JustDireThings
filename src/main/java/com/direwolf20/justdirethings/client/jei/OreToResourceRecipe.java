package com.direwolf20.justdirethings.client.jei;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class OreToResourceRecipe {
    private final ItemStack oreBlock;
    private final ItemStack output;

    public OreToResourceRecipe(Block oreBlock, ItemStack output) {
        this.oreBlock = new ItemStack(oreBlock);
        this.output = output;
    }

    public ItemStack getOreBlock() {
        return oreBlock;
    }

    public ItemStack getOutput() {
        return output;
    }
}
