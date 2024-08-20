package com.direwolf20.justdirethings.common.blocks.resources;

import com.direwolf20.justdirethings.client.particles.glitterparticle.GlitterParticleData;
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

import java.util.Random;

public class TimeCrystalBuddingBlock extends BuddingAmethystBlock {
    private final static Random rand = new Random();
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

    public boolean canAdvanceTo1(Level level, BlockState state) {
        int stage = state.getValue(STAGE);
        return stage == 0 && level.dimension() == Level.OVERWORLD;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int stage = state.getValue(STAGE);
        if (canAdvanceTo1(level, state)) {
            level.setBlockAndUpdate(pos, state.setValue(STAGE, 1));
            level.playSound(null, pos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0F, 0.25F);
        }
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

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(STAGE) == 3) return;
        double d0 = (double) pos.getX() + 0.5;
        double d1 = (double) pos.getY() + 0.5;
        double d2 = (double) pos.getZ() + 0.5;

        float r, g, b;
        if (canAdvanceTo1(level, state)) {
            r = 0.25f;
            g = 0.55f;
            b = 1f;
        } else {
            return;
        }

        for (int i = 0; i < 3; i++) {
            // Helper method to generate random positions outside the block (either -0.5 to 0 or 1 to 1.5)
            double offsetX = random.nextBoolean() ? -0.5 + random.nextDouble() * 0.5 : 1.0 + random.nextDouble() * 0.5;
            double offsetY = random.nextBoolean() ? -0.5 + random.nextDouble() * 0.5 : 1.0 + random.nextDouble() * 0.5;
            double offsetZ = random.nextBoolean() ? -0.5 + random.nextDouble() * 0.5 : 1.0 + random.nextDouble() * 0.5;

            // Calculate the start position with the random offsets
            double startX = (double) pos.getX() + offsetX;
            double startY = (double) pos.getY() + offsetY;
            double startZ = (double) pos.getZ() + offsetZ;

            float randomPartSize = 0.05f + (0.025f - 0.05f) * rand.nextFloat();
            GlitterParticleData data = GlitterParticleData.playerparticle("glitter", d0, d1, d2, randomPartSize, r, g, b, 0.5f, 120, false);
            level.addParticle(data, startX, startY, startZ, 0.025, 0.025f, 0.025);
        }
    }
}
