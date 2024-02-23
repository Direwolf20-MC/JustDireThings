package com.direwolf20.justdirethings.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class DireIronBlock extends Block {
    public DireIronBlock() {
        super(Properties.of()
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 6.0F)
        );
    }
}
