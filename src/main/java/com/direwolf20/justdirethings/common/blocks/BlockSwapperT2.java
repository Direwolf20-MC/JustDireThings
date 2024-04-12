package com.direwolf20.justdirethings.common.blocks;

import com.direwolf20.justdirethings.common.blockentities.BlockSwapperT2BE;
import com.direwolf20.justdirethings.common.blocks.baseblocks.BaseMachineBlock;
import com.direwolf20.justdirethings.common.containers.BlockSwapperT2Container;
import com.direwolf20.justdirethings.common.items.FerricoreWrench;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class BlockSwapperT2 extends BaseMachineBlock {
    public BlockSwapperT2() {
        super(Properties.of()
                .sound(SoundType.METAL)
                .strength(2.0f)
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockSwapperT2BE(pos, state);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        ItemStack playerHolding = player.getItemInHand(hand);
        if (playerHolding.getItem() instanceof FerricoreWrench) return InteractionResult.PASS;

        BlockEntity te = level.getBlockEntity(blockPos);
        if (!(te instanceof BlockSwapperT2BE))
            return InteractionResult.FAIL;

        player.openMenu(new SimpleMenuProvider(
                (windowId, playerInventory, playerEntity) -> new BlockSwapperT2Container(windowId, playerInventory, blockPos), Component.translatable("")), (buf -> {
            buf.writeBlockPos(blockPos);
        }));
        return InteractionResult.SUCCESS;
    }
}
