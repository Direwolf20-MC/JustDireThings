package com.direwolf20.justdirethings.common.blockentities.gooblocks;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GooBlockBE_Tier1 extends GooBlockBE_Base {
    public GooBlockBE_Tier1(BlockPos pos, BlockState state) {
        super(Registration.GooBlockBE_Tier1.get(), pos, state);
    }
}
