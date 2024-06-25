package com.direwolf20.justdirethings.common.fluids.unrefinedt2fuel;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class UnrefinedT2FuelBlock extends LiquidBlock {
    public UnrefinedT2FuelBlock() {
        super(Registration.UNREFINED_T2_FLUID_SOURCE.get(), Properties.of()
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
