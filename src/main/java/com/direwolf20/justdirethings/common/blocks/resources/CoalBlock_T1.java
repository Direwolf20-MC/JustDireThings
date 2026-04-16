package com.direwolf20.justdirethings.common.blocks.resources;

import net.minecraft.world.level.block.Block;

public class CoalBlock_T1 extends Block {
    public CoalBlock_T1(Properties properties) {
        super(properties);
    }

    public int getBurnSpeedMultiplier() {
        return 2;
    }
}
