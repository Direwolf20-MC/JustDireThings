package com.direwolf20.justdirethings.common.fluids.refinedt4fuel;

import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.world.level.block.LiquidBlock;

public class RefinedT4FuelBlock extends LiquidBlock {
    public RefinedT4FuelBlock(Properties properties) {
        super(JDTRegistration.REFINED_T4_FLUID_SOURCE.get(), properties);
    }
}
