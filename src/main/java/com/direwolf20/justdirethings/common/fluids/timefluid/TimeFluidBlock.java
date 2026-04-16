package com.direwolf20.justdirethings.common.fluids.timefluid;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.world.level.block.LiquidBlock;

public class TimeFluidBlock extends LiquidBlock {
    public TimeFluidBlock(Properties properties) {
        super(Registration.TIME_FLUID_SOURCE.get(), properties);
    }
}
