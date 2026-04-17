package com.direwolf20.justdirethings.client.itemcustomrenders;

import com.direwolf20.justdirethings.common.items.FluidCanister;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record FluidCanisterTintSource() implements ItemTintSource {
    public static final MapCodec<FluidCanisterTintSource> MAP_CODEC = MapCodec.unit(new FluidCanisterTintSource());

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        return FluidCanister.getFluidColor(stack);
    }

    @Override
    public MapCodec<FluidCanisterTintSource> type() {
        return MAP_CODEC;
    }
}
