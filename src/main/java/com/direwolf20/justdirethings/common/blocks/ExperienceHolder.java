package com.direwolf20.justdirethings.common.blocks;

import com.direwolf20.justdirethings.common.blockentities.ExperienceHolderBE;
import com.direwolf20.justdirethings.common.blocks.baseblocks.BaseMachineBlock;
import com.direwolf20.justdirethings.common.containers.ExperienceHolderContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class ExperienceHolder extends BaseMachineBlock {
    protected static final VoxelShape[] shapes = new VoxelShape[]{
            Stream.of(
                    Block.box(4, 0, 4, 12, 1, 12),
                    Block.box(3, 1, 3, 13, 2, 13),
                    Block.box(6, 2, 11, 10, 8, 12),
                    Block.box(5, 2, 5, 11, 9, 11),
                    Block.box(7, 9, 7, 9, 11, 9),
                    Block.box(6, 2, 4, 10, 8, 5),
                    Block.box(11, 2, 6, 12, 8, 10),
                    Block.box(4, 2, 6, 5, 8, 10)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), //UP
            Stream.of(
                    Block.box(4, 15, 4, 12, 16, 12),
                    Block.box(3, 14, 3, 13, 15, 13),
                    Block.box(6, 8, 11, 10, 14, 12),
                    Block.box(5, 7, 5, 11, 14, 11),
                    Block.box(7, 5, 7, 9, 7, 9),
                    Block.box(6, 8, 4, 10, 14, 5),
                    Block.box(11, 8, 6, 12, 14, 10),
                    Block.box(4, 8, 6, 5, 14, 10)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), //DOWN
            Stream.of(
                    Block.box(4, 4, 0, 12, 12, 1),
                    Block.box(3, 3, 1, 13, 13, 2),
                    Block.box(6, 4, 2, 10, 5, 8),
                    Block.box(5, 5, 2, 11, 11, 9),
                    Block.box(7, 7, 9, 9, 9, 11),
                    Block.box(6, 11, 2, 10, 12, 8),
                    Block.box(4, 6, 2, 5, 10, 8),
                    Block.box(11, 6, 2, 12, 10, 8)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), //SOUTH
            Stream.of(
                    Block.box(4, 4, 15, 12, 12, 16),
                    Block.box(3, 3, 14, 13, 13, 15),
                    Block.box(6, 4, 8, 10, 5, 14),
                    Block.box(5, 5, 7, 11, 11, 14),
                    Block.box(7, 7, 5, 9, 9, 7),
                    Block.box(6, 11, 8, 10, 12, 14),
                    Block.box(11, 6, 8, 12, 10, 14),
                    Block.box(4, 6, 8, 5, 10, 14)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), //NORTH
            Stream.of(
                    Block.box(0, 4, 4, 1, 12, 12),
                    Block.box(1, 3, 3, 2, 13, 13),
                    Block.box(2, 4, 6, 8, 5, 10),
                    Block.box(2, 5, 5, 9, 11, 11),
                    Block.box(9, 7, 7, 11, 9, 9),
                    Block.box(2, 11, 6, 8, 12, 10),
                    Block.box(2, 6, 11, 8, 10, 12),
                    Block.box(2, 6, 4, 8, 10, 5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), //EAST
            Stream.of(
                    Block.box(15, 4, 4, 16, 12, 12),
                    Block.box(14, 3, 3, 15, 13, 13),
                    Block.box(8, 4, 6, 14, 5, 10),
                    Block.box(7, 5, 5, 14, 11, 11),
                    Block.box(5, 7, 7, 7, 9, 9),
                    Block.box(8, 11, 6, 14, 12, 10),
                    Block.box(8, 6, 4, 14, 10, 5),
                    Block.box(8, 6, 11, 14, 10, 12)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get() //WEST
    };

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public ExperienceHolder() {
        super(Properties.of()
                .sound(SoundType.METAL)
                .strength(2.0f)
                .noOcclusion()
                .forceSolidOn()
                .isRedstoneConductor(BaseMachineBlock::never)
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ExperienceHolderBE(pos, state);
    }

    @Override
    public void openMenu(Player player, BlockPos blockPos) {
        player.openMenu(new SimpleMenuProvider(
                (windowId, playerInventory, playerEntity) -> new ExperienceHolderContainer(windowId, playerInventory, blockPos), Component.translatable("")), (buf -> {
            buf.writeBlockPos(blockPos);
        }));
    }

    @Override
    public boolean isValidBE(BlockEntity blockEntity) {
        return blockEntity instanceof ExperienceHolderBE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return shapes[state.getValue(FACING).get3DDataValue()];
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return shapes[state.getValue(FACING).get3DDataValue()];
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.FACING, context.getClickedFace().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
    }

    @Override
    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
        return true;
    }
}
