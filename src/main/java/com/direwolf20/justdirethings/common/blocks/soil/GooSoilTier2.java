package com.direwolf20.justdirethings.common.blocks.soil;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class GooSoilTier2 extends GooSoilBase {
    public GooSoilTier2() {
        super();
    }

    /**
     * Performs a random tick on a block.
     */
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
        bonemealMe(pLevel, pPos, pRandom, 9); //1 in 9 chance of the random tick causing a bonemeal
        autoHarvest(pLevel, pPos);
    }
}
