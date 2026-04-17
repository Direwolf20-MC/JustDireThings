package com.direwolf20.justdirethings.client.itemcustomrenders;

import com.direwolf20.justdirethings.common.items.PortalGunV2;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record PortalGunFullnessProperty() implements RangeSelectItemModelProperty {
    public static final MapCodec<PortalGunFullnessProperty> MAP_CODEC = MapCodec.unit(new PortalGunFullnessProperty());

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        return PortalGunV2.getFullness(stack);
    }

    @Override
    public MapCodec<PortalGunFullnessProperty> type() {
        return MAP_CODEC;
    }
}
