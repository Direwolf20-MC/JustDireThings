package com.direwolf20.justdirethings.client.itemcustomrenders;

import com.direwolf20.justdirethings.common.items.PotionCanister;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record PotionCanisterFullnessProperty() implements RangeSelectItemModelProperty {
    public static final MapCodec<PotionCanisterFullnessProperty> MAP_CODEC = MapCodec.unit(new PotionCanisterFullnessProperty());

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        int amount = PotionCanister.getPotionAmount(stack);
        int maxMB = PotionCanister.getMaxMB();
        if (maxMB <= 0 || amount <= 0) return 0F;
        // Return 1..4 for the 4 fullness levels the old model predicates used.
        float ratio = amount / (float) maxMB;
        return Math.max(1F, Math.min(4F, (float) Math.ceil(ratio * 4F)));
    }

    @Override
    public MapCodec<PotionCanisterFullnessProperty> type() {
        return MAP_CODEC;
    }
}
