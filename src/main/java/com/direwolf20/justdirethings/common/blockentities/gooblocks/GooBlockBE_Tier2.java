package com.direwolf20.justdirethings.common.blockentities.gooblocks;

import com.direwolf20.justdirethings.common.blockentities.basebe.GooBlockBE_Base;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GooBlockBE_Tier2 extends GooBlockBE_Base {
    public GooBlockBE_Tier2(BlockPos pos, BlockState state) {
        super(Registration.GooBlockBE_Tier2.get(), pos, state);
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public int counterReducer() {
        return 2;
    }
}
