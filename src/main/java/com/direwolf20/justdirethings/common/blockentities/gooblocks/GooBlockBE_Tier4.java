package com.direwolf20.justdirethings.common.blockentities.gooblocks;

import com.direwolf20.justdirethings.common.blockentities.basebe.GooBlockBE_Base;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GooBlockBE_Tier4 extends GooBlockBE_Base {
    public GooBlockBE_Tier4(BlockPos pos, BlockState state) {
        super(Registration.GooBlockBE_Tier4.get(), pos, state);
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public int counterReducer() {
        return 10;
    }
}
