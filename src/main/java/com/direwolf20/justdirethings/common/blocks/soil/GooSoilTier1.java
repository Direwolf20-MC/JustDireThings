package com.direwolf20.justdirethings.common.blocks.soil;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class GooSoilTier1 extends GooSoilBase {
    public GooSoilTier1() {
        super();
    }

    /**
     * Performs a random tick on a block.
     */
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
        bonemealMe(pLevel, pPos, pRandom, 13); //1 in 13 chance of the random tick causing a bonemeal
    }
}
