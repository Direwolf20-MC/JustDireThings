package com.direwolf20.justdirethings.client.particles.alwaysvisibleparticle;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class AlwaysVisibleParticleType extends ParticleType<AlwaysVisibleParticleData> {
    public AlwaysVisibleParticleType() {
        super(false, AlwaysVisibleParticleData.DESERIALIZER);
    }

    @Override
    public Codec<AlwaysVisibleParticleData> codec() {
        return null;
    }
}
