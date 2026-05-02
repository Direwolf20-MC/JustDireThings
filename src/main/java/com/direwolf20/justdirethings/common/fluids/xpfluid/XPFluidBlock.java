package com.direwolf20.justdirethings.common.fluids.xpfluid;

import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.world.level.block.LiquidBlock;

public class XPFluidBlock extends LiquidBlock {
    public XPFluidBlock(Properties properties) {
        super(JDTRegistration.XP_FLUID_SOURCE.get(), properties);
    }
}
