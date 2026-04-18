package com.direwolf20.justdirethings.common.fluids.refinedt2fuel;

import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.world.level.block.LiquidBlock;

public class RefinedT2FuelBlock extends LiquidBlock {
    public RefinedT2FuelBlock(Properties properties) {
        super(JDTRegistration.REFINED_T2_FLUID_SOURCE.get(), properties);
    }
}
