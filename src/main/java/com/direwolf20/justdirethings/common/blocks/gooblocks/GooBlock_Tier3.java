package com.direwolf20.justdirethings.common.blocks.gooblocks;


import com.direwolf20.justdirethings.common.blockentities.gooblocks.GooBlockBE_Tier3;
import com.direwolf20.justdirethings.datagen.JustDireItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class GooBlock_Tier3 extends GooBlock_Base implements EntityBlock {
    public GooBlock_Tier3() {
        super();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GooBlockBE_Tier3(pos, state);
    }

    @Override
    protected boolean validRevivalItem(ItemStack itemStack) {
        return itemStack.is(JustDireItemTags.GOO_REVIVE_TIER_3);
    }

}
