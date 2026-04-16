package com.direwolf20.justdirethings.common.blocks;

import com.direwolf20.justdirethings.common.blockentities.FluidPlacerT2BE;
import com.direwolf20.justdirethings.common.blocks.baseblocks.BaseMachineBlock;
import com.direwolf20.justdirethings.common.containers.FluidPlacerT2Container;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import javax.annotation.Nullable;

public class FluidPlacerT2 extends BaseMachineBlock {
    public FluidPlacerT2(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FluidPlacerT2BE(pos, state);
    }

    @Override
    public void openMenu(Player player, BlockPos blockPos) {
        player.openMenu(new SimpleMenuProvider(
                (windowId, playerInventory, playerEntity) -> new FluidPlacerT2Container(windowId, playerInventory, blockPos), Component.translatable("")), (buf -> {
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
        return blockEntity instanceof FluidPlacerT2BE;
    }
}
