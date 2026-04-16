package com.direwolf20.justdirethings.common.blocks;

import com.direwolf20.justdirethings.common.blockentities.ExperienceHolderBE;
import com.direwolf20.justdirethings.common.blocks.baseblocks.BaseMachineBlock;
import com.direwolf20.justdirethings.common.containers.ExperienceHolderContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class ExperienceHolder extends BaseMachineBlock {
    protected static final VoxelShape[] shapes = new VoxelShape[]{
            Stream.of(
                    Block.box(4, 4, 6, 6, 10, 10),
                    Block.box(4, 0, 4, 12, 1, 12),
                    Block.box(3, 1, 3, 13, 2, 13),
                    Block.box(6, 4, 10, 10, 10, 12),
                    Block.box(6, 4, 4, 10, 10, 6),
                    Block.box(10, 4, 6, 12, 10, 10),
                    Block.box(5, 2, 5, 11, 9, 11),
                    Block.box(6, 9, 6, 10, 11, 10),
                    Block.box(4, 3, 4, 12, 4, 12)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), // UP
            Stream.of(
                    Block.box(4, 6, 6, 6, 12, 10),
                    Block.box(4, 15, 4, 12, 16, 12),
                    Block.box(3, 14, 3, 13, 15, 13),
                    Block.box(6, 6, 10, 10, 12, 12),
                    Block.box(6, 6, 4, 10, 12, 6),
                    Block.box(10, 6, 6, 12, 12, 10),
                    Block.box(5, 7, 5, 11, 14, 11),
                    Block.box(6, 5, 6, 10, 7, 10),
                    Block.box(4, 12, 4, 12, 13, 12)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), // DOWN
            Stream.of(
                    Block.box(4, 6, 4, 6, 10, 10),
                    Block.box(4, 4, 0, 12, 12, 1),
                    Block.box(3, 3, 1, 13, 13, 2),
                    Block.box(6, 4, 4, 10, 6, 10),
                    Block.box(6, 10, 4, 10, 12, 10),
                    Block.box(10, 6, 4, 12, 10, 10),
                    Block.box(5, 5, 2, 11, 11, 9),
                    Block.box(6, 6, 9, 10, 10, 11),
                    Block.box(4, 4, 3, 12, 12, 4)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), // SOUTH
            Stream.of(
                    Block.box(10, 6, 6, 12, 10, 12),
                    Block.box(4, 4, 15, 12, 12, 16),
                    Block.box(3, 3, 14, 13, 13, 15),
                    Block.box(6, 4, 6, 10, 6, 12),
                    Block.box(6, 10, 6, 10, 12, 12),
                    Block.box(4, 6, 6, 6, 10, 12),
                    Block.box(5, 5, 7, 11, 11, 14),
                    Block.box(6, 6, 5, 10, 10, 7),
                    Block.box(4, 4, 12, 12, 12, 13)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), //NORTH
            Stream.of(
                    Block.box(4, 6, 10, 10, 10, 12),
                    Block.box(0, 4, 4, 1, 12, 12),
                    Block.box(1, 3, 3, 2, 13, 13),
                    Block.box(4, 4, 6, 10, 6, 10),
                    Block.box(4, 10, 6, 10, 12, 10),
                    Block.box(4, 6, 4, 10, 10, 6),
                    Block.box(2, 5, 5, 9, 11, 11),
                    Block.box(9, 6, 6, 11, 10, 10),
                    Block.box(3, 4, 4, 4, 12, 12)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), //EAST
            Stream.of(
                    Block.box(6, 6, 4, 12, 10, 6),
                    Block.box(15, 4, 4, 16, 12, 12),
                    Block.box(14, 3, 3, 15, 13, 13),
                    Block.box(6, 4, 6, 12, 6, 10),
                    Block.box(6, 10, 6, 12, 12, 10),
                    Block.box(6, 6, 10, 12, 10, 12),
                    Block.box(7, 5, 5, 14, 11, 11),
                    Block.box(5, 6, 6, 7, 10, 10),
                    Block.box(12, 4, 4, 13, 12, 12)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get() //WEST
    };

    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    public ExperienceHolder(Properties properties) {
        super(properties);
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
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (level.isClientSide()) return InteractionResult.TRY_WITH_EMPTY_HAND;
        if (itemStack.isEmpty()) return InteractionResult.TRY_WITH_EMPTY_HAND;
        ItemAccess access = ItemAccess.forPlayerInteraction(player, hand);
        ResourceHandler<FluidResource> fluidHandlerItem = access.getCapability(Capabilities.Fluid.ITEM);
        if (fluidHandlerItem != null) {
            ResourceHandler<FluidResource> cap = level.getCapability(Capabilities.Fluid.BLOCK, blockPos, blockHitResult.getDirection());
            if (cap == null) return InteractionResult.TRY_WITH_EMPTY_HAND;
            FluidResource itemResource = fluidHandlerItem.getResource(0);
            int itemAmount = fluidHandlerItem.getAmountAsInt(0);
            int itemCapacity = fluidHandlerItem.getCapacityAsInt(0, itemResource);
            FluidResource blockResource = cap.getResource(0);
            int blockAmount = cap.getAmountAsInt(0);
            if (itemAmount < itemCapacity && blockAmount > 0 && !blockResource.isEmpty()) {
                try (Transaction tx = Transaction.openRoot()) {
                    int extracted = cap.extract(0, blockResource, itemCapacity, tx);
                    if (extracted > 0) {
                        int inserted = fluidHandlerItem.insert(0, blockResource, extracted, tx);
                        if (inserted > 0) {
                            if (inserted < extracted) {
                                cap.insert(0, blockResource, extracted - inserted, tx);
                            }
                            tx.commit();
                            level.playSound(null, blockPos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1F, 1.0F);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            } else if (!itemResource.isEmpty() && itemAmount > 0) {
                try (Transaction tx = Transaction.openRoot()) {
                    int extracted = fluidHandlerItem.extract(0, itemResource, itemAmount, tx);
                    if (extracted > 0) {
                        int inserted = cap.insert(0, itemResource, extracted, tx);
                        if (inserted > 0) {
                            if (inserted < extracted) {
                                fluidHandlerItem.insert(0, itemResource, extracted - inserted, tx);
                            }
                            tx.commit();
                            level.playSound(null, blockPos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1F, 1.0F);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
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
    public VoxelShape getOcclusionShape(BlockState state) {
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
    public boolean propagatesSkylightDown(BlockState p_48740_) {
        return true;
    }
}
