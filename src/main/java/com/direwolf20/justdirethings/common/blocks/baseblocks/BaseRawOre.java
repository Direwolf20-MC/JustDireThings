package com.direwolf20.justdirethings.common.blocks.baseblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.Stream;

public class BaseRawOre extends Block {
    protected static final VoxelShape[] shapes = new VoxelShape[]{
            Stream.of(
                    Block.box(8, 12, 10, 12, 16, 14),
                    Block.box(8, 8, 4, 12, 14, 8),
                    Block.box(2, 10, 8, 8, 16, 14),
                    Block.box(4, 14, 2, 14, 16, 12),
                    Block.box(6, 2, 6, 10, 14, 10),
                    Block.box(4, 6, 8, 8, 10, 12)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), //DOWN
            Stream.of(
                    Block.box(8, 2, 8, 12, 8, 12),
                    Block.box(2, 0, 2, 8, 6, 8),
                    Block.box(4, 0, 4, 14, 2, 14),
                    Block.box(6, 2, 6, 10, 14, 10),
                    Block.box(8, 0, 2, 12, 4, 6),
                    Block.box(4, 6, 4, 8, 10, 8)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), //UP
            Stream.of(
                    Block.box(8, 2, 12, 12, 6, 16),
                    Block.box(8, 8, 8, 12, 12, 14),
                    Block.box(2, 2, 10, 8, 8, 16),
                    Block.box(4, 4, 14, 14, 14, 16),
                    Block.box(6, 6, 2, 10, 10, 14),
                    Block.box(4, 4, 6, 8, 8, 10)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), //NORTH
            Stream.of(
                    Block.box(4, 2, 0, 8, 6, 4),
                    Block.box(4, 8, 2, 8, 12, 8),
                    Block.box(8, 2, 0, 14, 8, 6),
                    Block.box(2, 4, 0, 12, 14, 2),
                    Block.box(6, 6, 2, 10, 10, 14),
                    Block.box(8, 4, 6, 12, 8, 10)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), //SOUTH
            Stream.of(
                    Block.box(12, 2, 4, 16, 6, 8),
                    Block.box(8, 8, 4, 14, 12, 8),
                    Block.box(10, 2, 8, 16, 8, 14),
                    Block.box(14, 4, 2, 16, 14, 12),
                    Block.box(2, 6, 6, 14, 10, 10),
                    Block.box(6, 4, 8, 10, 8, 12)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), //West
            Stream.of(
                    Block.box(0, 2, 8, 4, 6, 12),
                    Block.box(2, 8, 8, 8, 12, 12),
                    Block.box(0, 2, 2, 6, 8, 8),
                    Block.box(0, 4, 4, 2, 14, 14),
                    Block.box(2, 6, 6, 14, 10, 10),
                    Block.box(6, 4, 4, 10, 8, 8)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get() //East
    };

    public BaseRawOre(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.FACING, context.getClickedFace());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return shapes[state.getValue(BlockStateProperties.FACING).get3DDataValue()];
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return shapes[state.getValue(BlockStateProperties.FACING).get3DDataValue()];
    }
}
