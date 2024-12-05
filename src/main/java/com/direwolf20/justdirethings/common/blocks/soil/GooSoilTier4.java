package com.direwolf20.justdirethings.common.blocks.soil;

import com.direwolf20.justdirethings.common.blockentities.GooSoilBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class GooSoilTier4 extends GooSoilBase implements EntityBlock {
    public GooSoilTier4() {
        super();
    }

    /**
     * Performs a random tick on a block.
     */
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
        for (int i = 0; i < 4; i++) {
            bonemealMe(pLevel, pPos);
        }
        autoHarvest(pLevel, pPos);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GooSoilBE(pos, state);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.UP && level instanceof ServerLevel serverLevel) {
            autoHarvest(serverLevel, currentPos);
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }
}
