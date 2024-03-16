package com.direwolf20.justdirethings.common.blocks.resources;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class CelestigemBlock extends Block {
    public CelestigemBlock() {
        super(Properties.of()
                .sound(SoundType.AMETHYST)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 6.0F)
        );
    }
}
