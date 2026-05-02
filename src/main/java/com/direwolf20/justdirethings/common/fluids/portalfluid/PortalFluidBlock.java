package com.direwolf20.justdirethings.common.fluids.portalfluid;

import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.world.level.block.LiquidBlock;

public class PortalFluidBlock extends LiquidBlock {
    public PortalFluidBlock(Properties properties) {
        super(JDTRegistration.PORTAL_FLUID_SOURCE.get(), properties);
    }
}
