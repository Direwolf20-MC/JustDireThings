package com.direwolf20.justdirethings.client.itemcustomrenders;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jspecify.annotations.Nullable;

public record BucketFluidTintSource() implements ItemTintSource {
    public static final MapCodec<BucketFluidTintSource> MAP_CODEC = MapCodec.unit(new BucketFluidTintSource());

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        if (!(stack.getItem() instanceof BucketItem bucket)) return 0xFFFFFFFF;
        Fluid fluid = bucket.content;
        if (fluid == Fluids.EMPTY) return 0xFFFFFFFF;
        FluidModel model = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluid.defaultFluidState());
        FluidTintSource tintSource = model.fluidTintSource();
        int tint = tintSource != null ? tintSource.colorAsStack(new FluidStack(fluid, 1000)) : 0xFFFFFFFF;
        return tint | 0xFF000000;
    }

    @Override
    public MapCodec<BucketFluidTintSource> type() {
        return MAP_CODEC;
    }
}
