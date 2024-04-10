package com.direwolf20.justdirethings.common.blocks.resources;

import net.minecraft.world.level.block.Block;

public class CoalBlock_T3 extends Block {
    public CoalBlock_T3() {
        super(Properties.of()
                .requiresCorrectToolForDrops()
                .strength(5.0F, 6.0F)
        );
    }
}
