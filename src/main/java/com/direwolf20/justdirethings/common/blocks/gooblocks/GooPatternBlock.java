package com.direwolf20.justdirethings.common.blocks.gooblocks;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class GooPatternBlock extends Block {
    public static final IntegerProperty GOOSTAGE = IntegerProperty.create("goostage", 0, 9);

    public GooPatternBlock() {
        super(Properties.of()
                .sound(SoundType.FUNGUS)
                .strength(2.0f)
                .noOcclusion()
        );
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(BlockStateProperties.FACING, context.getClickedFace().getOpposite())
                .setValue(GOOSTAGE, 0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, GOOSTAGE);
    }
}
