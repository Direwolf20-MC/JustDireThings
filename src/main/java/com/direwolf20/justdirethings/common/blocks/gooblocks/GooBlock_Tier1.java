package com.direwolf20.justdirethings.common.blocks.gooblocks;


import com.direwolf20.justdirethings.common.blockentities.gooblocks.GooBlockBE_Tier1;
import com.direwolf20.justdirethings.datagen.JustDireItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class GooBlock_Tier1 extends GooBlock_Base implements EntityBlock {
    public GooBlock_Tier1() {
        super();
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return (lvl, pos, blockState, t) -> {
                if (t instanceof GooBlockBE_Tier1 tile) {
                    tile.tickClient();
                }
            };
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof GooBlockBE_Tier1 tile) {
                tile.tickServer();
            }
        };
    }

    @Override
    protected boolean validRevivalItem(ItemStack itemStack) {
        return itemStack.is(JustDireItemTags.GOO_REVIVE_TIER_1);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GooBlockBE_Tier1(pos, state);
    }

}
