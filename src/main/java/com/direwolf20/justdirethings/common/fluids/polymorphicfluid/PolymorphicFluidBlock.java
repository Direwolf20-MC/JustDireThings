package com.direwolf20.justdirethings.common.fluids.polymorphicfluid;

import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.world.level.block.LiquidBlock;

public class PolymorphicFluidBlock extends LiquidBlock {
    public PolymorphicFluidBlock(Properties properties) {
        super(JDTRegistration.POLYMORPHIC_FLUID_SOURCE.get(), properties);
    }
}
