package com.direwolf20.justdirethings.client.particles.gooexplodeparticle;


import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class GooExplodeParticleType extends ParticleType<GooExplodeParticleData> {
    public GooExplodeParticleType() {
        super(false, GooExplodeParticleData.DESERIALIZER);
    }

    @Override
    public Codec<GooExplodeParticleData> codec() {
        return null;
    }

}
