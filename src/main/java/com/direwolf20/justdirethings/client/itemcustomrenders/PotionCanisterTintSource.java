package com.direwolf20.justdirethings.client.itemcustomrenders;

import com.direwolf20.justdirethings.common.items.PotionCanister;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record PotionCanisterTintSource() implements ItemTintSource {
    public static final MapCodec<PotionCanisterTintSource> MAP_CODEC = MapCodec.unit(new PotionCanisterTintSource());

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        return PotionCanister.getPotionColor(stack) | 0xFF000000;
    }

    @Override
    public MapCodec<PotionCanisterTintSource> type() {
        return MAP_CODEC;
    }
}
