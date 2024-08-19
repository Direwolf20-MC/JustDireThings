package com.direwolf20.justdirethings.common.blocks.resources;

import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.SoundType;

public class TimeCrystalBlock extends AmethystBlock {
    public TimeCrystalBlock() {
        super(Properties.of()
                .sound(SoundType.AMETHYST)
                .requiresCorrectToolForDrops()
                .strength(1.5F));
    }
}
