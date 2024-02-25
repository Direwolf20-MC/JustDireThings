package com.direwolf20.justdirethings.common.blocks.gooblocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class GooBlock_Base extends Block {
    public GooBlock_Base() {
        super(Properties.of()
                .sound(SoundType.FUNGUS)
                .strength(2.0f)
                .dynamicShape()
                .noOcclusion()
        );
    }

    /*@Override
    public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        return 1.0F;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return super.getOcclusionShape(pState, pLevel, pPos);
    }

    @Override
    @Deprecated
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }*/
}
