package com.direwolf20.justdirethings.common.blocks.resources;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class RawFerricoreOre extends Block {
    public RawFerricoreOre() {
        super(Properties.of()
                .sound(SoundType.AMETHYST)
                .requiresCorrectToolForDrops()
                .noOcclusion()
                .strength(5.0F, 6.0F)
        );
    }
}
