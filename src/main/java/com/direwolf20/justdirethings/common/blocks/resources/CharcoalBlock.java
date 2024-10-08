package com.direwolf20.justdirethings.common.blocks.resources;

import net.minecraft.world.level.block.Block;

public class CharcoalBlock extends Block {
    public CharcoalBlock() {
        super(Properties.of()
                .requiresCorrectToolForDrops()
                .strength(5.0F, 6.0F)
        );
    }
}
