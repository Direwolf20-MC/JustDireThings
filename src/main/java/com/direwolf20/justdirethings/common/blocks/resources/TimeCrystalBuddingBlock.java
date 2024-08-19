package com.direwolf20.justdirethings.common.blocks.resources;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

public class TimeCrystalBuddingBlock extends BuddingAmethystBlock {
    private static final Direction[] DIRECTIONS = Direction.values();
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 3);

    public TimeCrystalBuddingBlock() {
        super(Properties.of()
                .sound(SoundType.AMETHYST)
                .requiresCorrectToolForDrops()
                .randomTicks()
                .strength(1.5F));
    }

    @Override
    public InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult hit) {
        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        int stage = blockState.getValue(STAGE);
        int newStage = stage == 3 ? 0 : stage + 1;

        level.setBlockAndUpdate(blockPos, blockState.setValue(STAGE, newStage));

        return InteractionResult.SUCCESS;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int stage = state.getValue(STAGE);
        if (stage != 3) return;
        if (random.nextInt(5) == 0) {
            Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            BlockPos blockpos = pos.relative(direction);
            BlockState blockstate = level.getBlockState(blockpos);
            Block block = null;
            if (canClusterGrowAtState(blockstate)) {
                block = Registration.TimeCrystalCluster_Small.get();
            } else if (blockstate.is(Registration.TimeCrystalCluster_Small.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = Registration.TimeCrystalCluster_Medium.get();
            } else if (blockstate.is(Registration.TimeCrystalCluster_Medium.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = Registration.TimeCrystalCluster_Large.get();
            } else if (blockstate.is(Registration.TimeCrystalCluster_Large.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = Registration.TimeCrystalCluster.get();
            }

            if (block != null) {
                BlockState blockstate1 = block.defaultBlockState()
                        .setValue(TimeCrystalCluster.FACING, direction)
                        .setValue(TimeCrystalCluster.WATERLOGGED, Boolean.valueOf(blockstate.getFluidState().getType() == Fluids.WATER));
                level.setBlockAndUpdate(blockpos, blockstate1);

                if (state.getValue(STAGE) == 3 && level.random.nextFloat() < 0.05f) {
                    // Update the block state to dead
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, 0));
                    level.playSound(null, pos, SoundEvents.RESPAWN_ANCHOR_DEPLETE.value(), SoundSource.BLOCKS, 1.0F, 0.25F);
                }
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(STAGE, 0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }
}
