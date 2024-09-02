package com.direwolf20.justdirethings.client.particles.paradoxparticle;


import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ParadoxParticleType extends ParticleType<ParadoxParticleData> {
    public ParadoxParticleType(boolean pOverrideLimiter) {
        super(pOverrideLimiter);
    }

    public ParadoxParticleType getType() {
        return this;
    }

    @Override
    public MapCodec<ParadoxParticleData> codec() {
        return ParadoxParticleData.MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ParadoxParticleData> streamCodec() {
        return ParadoxParticleData.STREAM_CODEC;
    }

}
