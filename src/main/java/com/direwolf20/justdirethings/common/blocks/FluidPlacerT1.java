package com.direwolf20.justdirethings.common.blocks;

import com.direwolf20.justdirethings.common.blockentities.FluidPlacerT1BE;
import com.direwolf20.justdirethings.common.blocks.baseblocks.BaseMachineBlock;
import com.direwolf20.justdirethings.common.containers.FluidPlacerT1Container;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class FluidPlacerT1 extends BaseMachineBlock {
    public FluidPlacerT1() {
        super(Properties.of()
                .sound(SoundType.METAL)
                .strength(2.0f)
                .isRedstoneConductor(BaseMachineBlock::never)
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FluidPlacerT1BE(pos, state);
    }

    @Override
    public void openMenu(Player player, BlockPos blockPos) {
        player.openMenu(new SimpleMenuProvider(
                (windowId, playerInventory, playerEntity) -> new FluidPlacerT1Container(windowId, playerInventory, blockPos), Component.translatable("")), (buf -> {
            buf.writeBlockPos(blockPos);
        }));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (level.isClientSide) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (itemStack.getItem() instanceof BucketItem bucketItem) {
            Fluid fluid = bucketItem.content;
            if (fluid == Fluids.EMPTY) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            FluidStack fluidStack = new FluidStack(fluid, 1000);
            IFluidHandler cap = level.getCapability(Capabilities.FluidHandler.BLOCK, blockPos, blockHitResult.getDirection());
            if (cap == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            int insertAmt = cap.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE);
            if (insertAmt == 1000) {
                cap.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                if (!player.hasInfiniteMaterials()) {
                    ItemStack newitemStack = new ItemStack(Items.BUCKET);
                    player.setItemSlot(hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND, newitemStack);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public boolean isValidBE(BlockEntity blockEntity) {
        return blockEntity instanceof FluidPlacerT1BE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
    }
}
