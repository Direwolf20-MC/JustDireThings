package com.direwolf20.justdirethings.common.fluids.unrefinedt4fuel;

import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.world.level.block.LiquidBlock;

public class UnrefinedT4FuelBlock extends LiquidBlock {
    public UnrefinedT4FuelBlock(Properties properties) {
        super(JDTRegistration.UNREFINED_T4_FLUID_SOURCE.get(), properties);
    }
}
