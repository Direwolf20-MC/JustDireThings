package com.direwolf20.justdirethings.common.blocks.soil;

import com.direwolf20.justdirethings.common.blockentities.GooSoilBE;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class GooSoilTier3 extends GooSoilBase implements EntityBlock {
    public GooSoilTier3() {
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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GooSoilBE(pos, state);
    }
}
