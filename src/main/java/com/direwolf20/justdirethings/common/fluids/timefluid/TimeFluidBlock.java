package com.direwolf20.justdirethings.common.fluids.timefluid;

import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.world.level.block.LiquidBlock;

public class TimeFluidBlock extends LiquidBlock {
    public TimeFluidBlock(Properties properties) {
        super(JDTRegistration.TIME_FLUID_SOURCE.get(), properties);
    }
}
