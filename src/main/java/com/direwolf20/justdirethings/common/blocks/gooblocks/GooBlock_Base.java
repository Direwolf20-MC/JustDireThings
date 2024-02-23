package com.direwolf20.justdirethings.common.blocks.gooblocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class GooBlock_Base extends Block {
    public GooBlock_Base() {
        super(Properties.of()
                .sound(SoundType.FUNGUS)
                .strength(2.0f)
        );
    }
}
