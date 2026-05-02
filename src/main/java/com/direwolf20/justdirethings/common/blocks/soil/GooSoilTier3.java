package com.direwolf20.justdirethings.common.blocks.soil;

import com.direwolf20.justdirethings.common.blockentities.GooSoilBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class GooSoilTier3 extends GooSoilBase implements EntityBlock {
    public GooSoilTier3(Properties properties) {
        super(properties);
    }

    /**
     * Performs a random tick on a block.
     */
    @Override
    protected void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
        for (int i = 0; i < 3; i++) {
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
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (directionToNeighbour == Direction.UP && level instanceof ServerLevel serverLevel) {
            autoHarvest(serverLevel, pos);
        }

        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }
}
