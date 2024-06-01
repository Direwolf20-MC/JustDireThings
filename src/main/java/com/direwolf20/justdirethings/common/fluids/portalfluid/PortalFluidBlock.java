package com.direwolf20.justdirethings.common.fluids.portalfluid;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class PortalFluidBlock extends LiquidBlock {
    public PortalFluidBlock() {
        super(Registration.PORTAL_FLUID_SOURCE.get(), BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_GREEN)
                .replaceable()
                .noCollission()
                .strength(100.0F)
                .pushReaction(PushReaction.DESTROY)
                .noLootTable()
                .liquid()
                .sound(SoundType.EMPTY)
        );
    }
}
