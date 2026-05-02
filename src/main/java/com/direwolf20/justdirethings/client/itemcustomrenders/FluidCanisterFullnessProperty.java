package com.direwolf20.justdirethings.client.itemcustomrenders;

import com.direwolf20.justdirethings.common.items.FluidCanister;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record FluidCanisterFullnessProperty() implements RangeSelectItemModelProperty {
    public static final MapCodec<FluidCanisterFullnessProperty> MAP_CODEC = MapCodec.unit(new FluidCanisterFullnessProperty());

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        return FluidCanister.getFullness(stack);
    }

    @Override
    public MapCodec<FluidCanisterFullnessProperty> type() {
        return MAP_CODEC;
    }
}
