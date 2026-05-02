package com.direwolf20.justdirethings.client.itemcustomrenders;

import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record ToolEnabledProperty() implements ConditionalItemModelProperty {
    public static final MapCodec<ToolEnabledProperty> MAP_CODEC = MapCodec.unit(new ToolEnabledProperty());

    @Override
    public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        return stack.getOrDefault(JustDireDataComponents.TOOL_ENABLED, true);
    }

    @Override
    public MapCodec<ToolEnabledProperty> type() {
        return MAP_CODEC;
    }
}
