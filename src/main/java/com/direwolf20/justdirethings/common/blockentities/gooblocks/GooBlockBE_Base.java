package com.direwolf20.justdirethings.common.blockentities.gooblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GooBlockBE_Base extends BlockEntity {
    public GooBlockBE_Base(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tickClient() {

    }

    public void tickServer() {

    }
}
