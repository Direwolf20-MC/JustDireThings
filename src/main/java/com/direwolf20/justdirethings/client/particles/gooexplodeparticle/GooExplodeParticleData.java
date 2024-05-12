package com.direwolf20.justdirethings.client.particles.gooexplodeparticle;

import com.direwolf20.justdirethings.client.particles.ModParticles;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class GooExplodeParticleData implements ParticleOptions {
    public static final MapCodec<GooExplodeParticleData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ItemStack.CODEC.fieldOf("itemStack").forGetter(GooExplodeParticleData::getItemStack)
            ).apply(instance, GooExplodeParticleData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, GooExplodeParticleData> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            GooExplodeParticleData::getItemStack,
            GooExplodeParticleData::new
    );

    private final ItemStack itemStack;

    public GooExplodeParticleData(ItemStack itemStack) {
        this.itemStack = itemStack.copy(); //Forge: Fix stack updating after the fact causing particle changes.
    }

    @Nonnull
    @Override
    public ParticleType<GooExplodeParticleData> getType() {
        return ModParticles.GOOEXPLODEPARTICLE.get();
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }
}

