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
                    Block.box(6.5, 4.25, 6.5, 9.5, 4.75, 9.5),
                    Block.box(5, 0, 5, 11, 1, 11),
                    Block.box(5, -2, 5, 11, -1.5, 11),
                    Block.box(6.75, 4, 6.75, 9.25, 5, 9.25),
                    Block.box(7.75, 5, 7.75, 8.25, 7.5, 8.25),
                    Block.box(7.5, 7.5, 7.5, 8.5, 8.5, 8.5),
                    Block.box(6.5, 1, 6.5, 9.5, 1.25, 9.5),
                    Block.box(7.5, 5.25, 7.5, 8.5, 5.5, 8.5),
                    Block.box(7.25, 5, 7.25, 8.75, 5.25, 8.75),
                    Block.box(6.75, 1, 9.5, 9.25, 1.25, 10.5),
                    Block.box(9.5, 1, 6.75, 10.5, 1.25, 9.25),
                    Block.box(6.75, 1, 5.5, 9.25, 1.25, 6.5),
                    Block.box(5.5, 1, 6.75, 6.5, 1.25, 9.25),
                    Block.box(5.25, -1.5, 5.25, 10.75, 0, 10.75),
                    Block.box(7, 1, 7, 9, 4, 9)
            ).reduce((v1, v2) -> {
                return Shapes.join(v1, v2, BooleanOp.OR);
            }).get(), //DOWN
            Stream.of(
                    Block.box(6.5, 11.25, 6.5, 9.5, 11.75, 9.5),
                    Block.box(5, 15, 5, 11, 16, 11),
                    Block.box(5, 17.5, 5, 11, 18, 11),
                    Block.box(6.75, 11, 6.75, 9.25, 12, 9.25),
                    Block.box(7.75, 8.5, 7.75, 8.25, 11, 8.25),
                    Block.box(7.5, 7.5, 7.5, 8.5, 8.5, 8.5),
                    Block.box(6.5, 14.75, 6.5, 9.5, 15, 9.5),
                    Block.box(7.5, 10.5, 7.5, 8.5, 10.75, 8.5),
                    Block.box(7.25, 10.75, 7.25, 8.75, 11, 8.75),
                    Block.box(6.75, 14.75, 9.5, 9.25, 15, 10.5),
                    Block.box(9.5, 14.75, 6.75, 10.5, 15, 9.25),
                    Block.box(6.75, 14.75, 5.5, 9.25, 15, 6.5),
                    Block.box(5.5, 14.75, 6.75, 6.5, 15, 9.25),
                    Block.box(5.25, 16, 5.25, 10.75, 17.5, 10.75),
                    Block.box(7, 12, 7, 9, 15, 9)
            ).reduce((v1, v2) -> {
                return Shapes.join(v1, v2, BooleanOp.OR);
            }).get(), //UP
            Stream.of(
                    Block.box(6.5, 6.5, 4.25, 9.5, 9.5, 4.75),
                    Block.box(5, 5, 0, 11, 11, 1),
                    Block.box(5, 5, -2, 11, 11, -1.5),
                    Block.box(6.75, 6.75, 4, 9.25, 9.25, 5),
                    Block.box(7.75, 7.75, 5, 8.25, 8.25, 7.5),
                    Block.box(7.5, 7.5, 7.5, 8.5, 8.5, 8.5),
                    Block.box(6.5, 6.5, 1, 9.5, 9.5, 1.25),
                    Block.box(7.5, 7.5, 5.25, 8.5, 8.5, 5.5),
                    Block.box(7.25, 7.25, 5, 8.75, 8.75, 5.25),
                    Block.box(6.75, 5.5, 1, 9.25, 6.5, 1.25),
                    Block.box(9.5, 6.75, 1, 10.5, 9.25, 1.25),
                    Block.box(6.75, 9.5, 1, 9.25, 10.5, 1.25),
                    Block.box(5.5, 6.75, 1, 6.5, 9.25, 1.25),
                    Block.box(5.25, 5.25, -1.5, 10.75, 10.75, 0),
                    Block.box(7, 7, 1, 9, 9, 4)
            ).reduce((v1, v2) -> {
                return Shapes.join(v1, v2, BooleanOp.OR);
            }).get(), //NORTH
            Stream.of(
                    Block.box(6.5, 6.5, 11.25, 9.5, 9.5, 11.75),
                    Block.box(5, 5, 15, 11, 11, 16),
                    Block.box(5, 5, 17.5, 11, 11, 18),
                    Block.box(6.75, 6.75, 11, 9.25, 9.25, 12),
                    Block.box(7.75, 7.75, 8.5, 8.25, 8.25, 11),
                    Block.box(7.5, 7.5, 7.5, 8.5, 8.5, 8.5),
                    Block.box(6.5, 6.5, 14.75, 9.5, 9.5, 15),
                    Block.box(7.5, 7.5, 10.5, 8.5, 8.5, 10.75),
                    Block.box(7.25, 7.25, 10.75, 8.75, 8.75, 11),
                    Block.box(6.75, 5.5, 14.75, 9.25, 6.5, 15),
                    Block.box(9.5, 6.75, 14.75, 10.5, 9.25, 15),
                    Block.box(6.75, 9.5, 14.75, 9.25, 10.5, 15),
                    Block.box(5.5, 6.75, 14.75, 6.5, 9.25, 15),
                    Block.box(5.25, 5.25, 16, 10.75, 10.75, 17.5),
                    Block.box(7, 7, 12, 9, 9, 15)
            ).reduce((v1, v2) -> {
                return Shapes.join(v1, v2, BooleanOp.OR);
            }).get(), //SOUTH
            Stream.of(
                    Block.box(4.25, 6.5, 6.5, 4.75, 9.5, 9.5),
                    Block.box(0, 5, 5, 1, 11, 11),
                    Block.box(-2, 5, 5, -1.5, 11, 11),
                    Block.box(4, 6.75, 6.75, 5, 9.25, 9.25),
                    Block.box(5, 7.75, 7.75, 7.5, 8.25, 8.25),
                    Block.box(7.5, 7.5, 7.5, 8.5, 8.5, 8.5),
                    Block.box(1, 6.5, 6.5, 1.25, 9.5, 9.5),
                    Block.box(5.25, 7.5, 7.5, 5.5, 8.5, 8.5),
                    Block.box(5, 7.25, 7.25, 5.25, 8.75, 8.75),
                    Block.box(1, 5.5, 6.75, 1.25, 6.5, 9.25),
                    Block.box(1, 6.75, 9.5, 1.25, 9.25, 10.5),
                    Block.box(1, 9.5, 6.75, 1.25, 10.5, 9.25),
                    Block.box(1, 6.75, 5.5, 1.25, 9.25, 6.5),
                    Block.box(-1.5, 5.25, 5.25, 0, 10.75, 10.75),
                    Block.box(1, 7, 7, 4, 9, 9)
            ).reduce((v1, v2) -> {
                return Shapes.join(v1, v2, BooleanOp.OR);
            }).get(), //WEST
            Stream.of(
                    Block.box(11.25, 6.5, 6.5, 11.75, 9.5, 9.5),
                    Block.box(15, 5, 5, 16, 11, 11),
                    Block.box(17.5, 5, 5, 18, 11, 11),
                    Block.box(11, 6.75, 6.75, 12, 9.25, 9.25),
                    Block.box(8.5, 7.75, 7.75, 11, 8.25, 8.25),
                    Block.box(7.5, 7.5, 7.5, 8.5, 8.5, 8.5),
                    Block.box(14.75, 6.5, 6.5, 15, 9.5, 9.5),
                    Block.box(10.5, 7.5, 7.5, 10.75, 8.5, 8.5),
                    Block.box(10.75, 7.25, 7.25, 11, 8.75, 8.75),
                    Block.box(14.75, 5.5, 6.75, 15, 6.5, 9.25),
                    Block.box(14.75, 6.75, 9.5, 15, 9.25, 10.5),
                    Block.box(14.75, 9.5, 6.75, 15, 10.5, 9.25),
                    Block.box(14.75, 6.75, 5.5, 15, 9.25, 6.5),
                    Block.box(16, 5.25, 5.25, 17.5, 10.75, 10.75),
                    Block.box(12, 7, 7, 15, 9, 9)
            ).reduce((v1, v2) -> {
                return Shapes.join(v1, v2, BooleanOp.OR);
            }).get()//EAST
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
