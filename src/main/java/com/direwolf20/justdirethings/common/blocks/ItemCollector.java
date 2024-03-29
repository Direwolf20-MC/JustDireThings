package com.direwolf20.justdirethings.common.blocks;

import com.direwolf20.justdirethings.common.blockentities.ItemCollectorBE;
import com.direwolf20.justdirethings.common.blocks.baseblocks.BaseMachineBlock;
import com.direwolf20.justdirethings.common.containers.ItemCollectorContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class ItemCollector extends BaseMachineBlock {
    protected static final VoxelShape[] shapes = new VoxelShape[]{
            Stream.of(
                    Block.box(5, 2, 10, 6, 6, 11),
                    Block.box(4, 0, 4, 12, 1, 12),
                    Block.box(6, 2, 6, 10, 9, 10),
                    Block.box(10, 2, 5, 11, 6, 6),
                    Block.box(3, 1, 3, 13, 2, 13),
                    Block.box(5, 6, 5, 11, 7, 11),
                    Block.box(10, 2, 10, 11, 6, 11),
                    Block.box(5, 2, 5, 6, 6, 6),
                    Block.box(7, 9, 7, 9, 11, 9)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), //DOWN
            Stream.of(
                    Block.box(5, 10, 10, 6, 14, 11),
                    Block.box(4, 15, 4, 12, 16, 12),
                    Block.box(6, 7, 6, 10, 14, 10),
                    Block.box(10, 10, 5, 11, 14, 6),
                    Block.box(3, 14, 3, 13, 15, 13),
                    Block.box(5, 9, 5, 11, 10, 11),
                    Block.box(10, 10, 10, 11, 14, 11),
                    Block.box(5, 10, 5, 6, 14, 6),
                    Block.box(7, 5, 7, 9, 7, 9)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), //UP
            Stream.of(
                    Block.box(5, 5, 2, 6, 6, 6),
                    Block.box(4, 4, 0, 12, 12, 1),
                    Block.box(6, 6, 2, 10, 10, 9),
                    Block.box(10, 10, 2, 11, 11, 6),
                    Block.box(3, 3, 1, 13, 13, 2),
                    Block.box(5, 5, 6, 11, 11, 7),
                    Block.box(10, 5, 2, 11, 6, 6),
                    Block.box(5, 10, 2, 6, 11, 6),
                    Block.box(7, 7, 9, 9, 9, 11)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), //NORTH
            Stream.of(
                    Block.box(5, 5, 10, 6, 6, 14),
                    Block.box(4, 4, 15, 12, 12, 16),
                    Block.box(6, 6, 7, 10, 10, 14),
                    Block.box(10, 10, 10, 11, 11, 14),
                    Block.box(3, 3, 14, 13, 13, 15),
                    Block.box(5, 5, 9, 11, 11, 10),
                    Block.box(10, 5, 10, 11, 6, 14),
                    Block.box(5, 10, 10, 6, 11, 14),
                    Block.box(7, 7, 5, 9, 9, 7)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), //SOUTH
            Stream.of(
                    Block.box(2, 5, 5, 6, 6, 6),
                    Block.box(0, 4, 4, 1, 12, 12),
                    Block.box(2, 6, 6, 9, 10, 10),
                    Block.box(2, 10, 10, 6, 11, 11),
                    Block.box(1, 3, 3, 2, 13, 13),
                    Block.box(6, 5, 5, 7, 11, 11),
                    Block.box(2, 5, 10, 6, 6, 11),
                    Block.box(2, 10, 5, 6, 11, 6),
                    Block.box(9, 7, 7, 11, 9, 9)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), //WEST
            Stream.of(
                    Block.box(10, 5, 5, 14, 6, 6),
                    Block.box(15, 4, 4, 16, 12, 12),
                    Block.box(7, 6, 6, 14, 10, 10),
                    Block.box(10, 10, 10, 14, 11, 11),
                    Block.box(14, 3, 3, 15, 13, 13),
                    Block.box(9, 5, 5, 10, 11, 11),
                    Block.box(10, 5, 10, 14, 6, 11),
                    Block.box(10, 10, 5, 14, 11, 6),
                    Block.box(5, 7, 7, 7, 9, 9)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()//EAST
    };

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public ItemCollector() {
        super(Properties.of()
                .sound(SoundType.METAL)
                .strength(2.0f)
                .noOcclusion()
                .forceSolidOn()
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ItemCollectorBE(pos, state);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        BlockEntity te = level.getBlockEntity(blockPos);
        if (!(te instanceof ItemCollectorBE))
            return InteractionResult.FAIL;

        player.openMenu(new SimpleMenuProvider(
                (windowId, playerInventory, playerEntity) -> new ItemCollectorContainer(windowId, playerInventory, blockPos), Component.translatable("")), (buf -> {
            buf.writeBlockPos(blockPos);
        }));
        return InteractionResult.SUCCESS;
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
