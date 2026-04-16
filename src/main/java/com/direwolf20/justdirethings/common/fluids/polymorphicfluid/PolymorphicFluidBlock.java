package com.direwolf20.justdirethings.common.fluids.polymorphicfluid;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.world.level.block.LiquidBlock;

public class PolymorphicFluidBlock extends LiquidBlock {
    public PolymorphicFluidBlock(Properties properties) {
        super(Registration.POLYMORPHIC_FLUID_SOURCE.get(), properties);
    }
}
