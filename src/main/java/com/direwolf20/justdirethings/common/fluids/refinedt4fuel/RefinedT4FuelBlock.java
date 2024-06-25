package com.direwolf20.justdirethings.common.fluids.refinedt4fuel;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class RefinedT4FuelBlock extends LiquidBlock {
    public RefinedT4FuelBlock() {
        super(Registration.REFINED_T4_FLUID_SOURCE.get(), Properties.of()
                .mapColor(MapColor.COLOR_RED)
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
