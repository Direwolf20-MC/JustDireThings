package com.direwolf20.justdirethings.common.blocks.resources;

import com.direwolf20.justdirethings.common.blocks.baseblocks.BaseRawOre;
import net.minecraft.world.level.block.SoundType;

public class RawBlazegoldOre extends BaseRawOre {
    public RawBlazegoldOre() {
        super(Properties.of()
                .sound(SoundType.AMETHYST)
                .requiresCorrectToolForDrops()
                .noOcclusion()
                .strength(5.0F, 6.0F)
        );
    }
}
