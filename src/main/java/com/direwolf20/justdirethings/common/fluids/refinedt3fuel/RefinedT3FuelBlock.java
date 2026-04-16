package com.direwolf20.justdirethings.common.fluids.refinedt3fuel;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.world.level.block.LiquidBlock;

public class RefinedT3FuelBlock extends LiquidBlock {
    public RefinedT3FuelBlock(Properties properties) {
        super(Registration.REFINED_T3_FLUID_SOURCE.get(), properties);
    }
}
