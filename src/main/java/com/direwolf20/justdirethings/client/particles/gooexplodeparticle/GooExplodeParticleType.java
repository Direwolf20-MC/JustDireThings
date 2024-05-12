package com.direwolf20.justdirethings.client.particles.gooexplodeparticle;


import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class GooExplodeParticleType extends ParticleType<GooExplodeParticleData> {
    public GooExplodeParticleType(boolean pOverrideLimiter) {
        super(pOverrideLimiter);
    }

    public GooExplodeParticleType getType() {
        return this;
    }

    @Override
    public MapCodec<GooExplodeParticleData> codec() {
        return GooExplodeParticleData.MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, GooExplodeParticleData> streamCodec() {
        return GooExplodeParticleData.STREAM_CODEC;
    }

}
