package com.direwolf20.justdirethings.common.fluids.unrefinedt2fuel;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.world.level.block.LiquidBlock;

public class UnrefinedT2FuelBlock extends LiquidBlock {
    public UnrefinedT2FuelBlock(Properties properties) {
        super(Registration.UNREFINED_T2_FLUID_SOURCE.get(), properties);
    }
}
