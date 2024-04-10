package com.direwolf20.justdirethings.common.blocks.resources;

import com.direwolf20.justdirethings.common.blocks.baseblocks.BaseRawOre;

public class RawCoal_T2 extends BaseRawOre {
    public RawCoal_T2() {
        super(Properties.of()
                .requiresCorrectToolForDrops()
                .noOcclusion()
                .strength(5.0F, 6.0F)
        );
    }
}
