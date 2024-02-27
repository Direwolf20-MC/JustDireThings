package com.direwolf20.justdirethings.common.blocks.gooblocks;


import com.direwolf20.justdirethings.common.blockentities.gooblocks.GooBlockBE_Tier2;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class GooBlock_Tier2 extends GooBlock_Base implements EntityBlock {
    public GooBlock_Tier2() {
        super();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GooBlockBE_Tier2(pos, state);
    }

}
