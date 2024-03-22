package com.direwolf20.justdirethings.common.blockentities.basebe;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface BaseBEInterface {
    BlockEntity getBlockEntity();

    default void markDirtyClient() {
        getBlockEntity().setChanged();
        if (getBlockEntity().getLevel() != null) {
            BlockState state = getBlockEntity().getLevel().getBlockState(getBlockEntity().getBlockPos());
            getBlockEntity().getLevel().sendBlockUpdated(getBlockEntity().getBlockPos(), state, state, 3);
        }
    }
}
