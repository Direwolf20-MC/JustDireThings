package com.direwolf20.justdirethings.common.fluids.unrefinedt3fuel;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.world.level.block.LiquidBlock;

public class UnrefinedT3FuelBlock extends LiquidBlock {
    public UnrefinedT3FuelBlock(Properties properties) {
        super(Registration.UNREFINED_T3_FLUID_SOURCE.get(), properties);
    }
}
