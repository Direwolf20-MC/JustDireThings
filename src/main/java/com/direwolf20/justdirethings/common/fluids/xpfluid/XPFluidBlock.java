package com.direwolf20.justdirethings.common.fluids.xpfluid;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class XPFluidBlock extends LiquidBlock {
    public XPFluidBlock() {
        super(Registration.XP_FLUID_SOURCE.get(), Properties.of()
                .mapColor(MapColor.COLOR_LIGHT_GREEN)
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
