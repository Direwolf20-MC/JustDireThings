package com.direwolf20.justdirethings.common.blocks;

import com.direwolf20.justdirethings.common.blockentities.BlockSwapperT2BE;
import com.direwolf20.justdirethings.common.blocks.baseblocks.BaseMachineBlock;
import com.direwolf20.justdirethings.common.containers.BlockSwapperT2Container;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BlockSwapperT2 extends BaseMachineBlock {
    public BlockSwapperT2() {
        super(Properties.of()
                .sound(SoundType.METAL)
                .strength(2.0f)
                .isRedstoneConductor(BaseMachineBlock::never)
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockSwapperT2BE(pos, state);
    }

    @Override
    public void openMenu(Player player, BlockPos blockPos) {
        player.openMenu(new SimpleMenuProvider(
                (windowId, playerInventory, playerEntity) -> new BlockSwapperT2Container(windowId, playerInventory, blockPos), Component.translatable("")), (buf -> {
            buf.writeBlockPos(blockPos);
        }));
    }

    @Override
    public boolean isValidBE(BlockEntity blockEntity) {
        return blockEntity instanceof BlockSwapperT2BE;
    }
}
