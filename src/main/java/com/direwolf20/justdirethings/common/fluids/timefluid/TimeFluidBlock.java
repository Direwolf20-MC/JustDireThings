package com.direwolf20.justdirethings.common.fluids.timefluid;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class TimeFluidBlock extends LiquidBlock {
    public TimeFluidBlock() {
        super(Registration.TIME_FLUID_SOURCE.get(), Properties.of()
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
