package com.direwolf20.justdirethings.client.particles.alwaysvisibleparticle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class AlwaysVisibleParticleType extends ParticleType<AlwaysVisibleParticleData> {
    public AlwaysVisibleParticleType(boolean pOverrideLimiter) {
        super(pOverrideLimiter);
    }

    public AlwaysVisibleParticleType getType() {
        return this;
    }

    @Override
    public MapCodec<AlwaysVisibleParticleData> codec() {
        return AlwaysVisibleParticleData.MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, AlwaysVisibleParticleData> streamCodec() {
        return AlwaysVisibleParticleData.STREAM_CODEC;
    }
}
