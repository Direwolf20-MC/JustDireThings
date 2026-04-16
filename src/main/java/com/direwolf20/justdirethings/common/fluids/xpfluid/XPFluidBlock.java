package com.direwolf20.justdirethings.common.fluids.xpfluid;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.world.level.block.LiquidBlock;

public class XPFluidBlock extends LiquidBlock {
    public XPFluidBlock(Properties properties) {
        super(Registration.XP_FLUID_SOURCE.get(), properties);
    }
}
