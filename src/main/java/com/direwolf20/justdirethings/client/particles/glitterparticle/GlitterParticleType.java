package com.direwolf20.justdirethings.client.particles.glitterparticle;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class GlitterParticleType extends ParticleType<GlitterParticleData> {
    public GlitterParticleType(boolean pOverrideLimiter) {
        super(pOverrideLimiter);
    }

    public GlitterParticleType getType() {
        return this;
    }

    @Override
    public MapCodec<GlitterParticleData> codec() {
        return GlitterParticleData.MAP_CODEC;
    }

    @Override
    public StreamCodec<FriendlyByteBuf, GlitterParticleData> streamCodec() {
        return GlitterParticleData.STREAM_CODEC;
    }

    public static class FACTORY implements ParticleProvider<GlitterParticleData> {
        private final SpriteSet sprites;

        public FACTORY(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(GlitterParticleData data, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new GlitterParticle(world, x, y, z, data.targetX, data.targetY, data.targetZ, xSpeed, ySpeed, zSpeed, data.size, data.r, data.g, data.b, data.a, data.depthTest, data.maxAgeMul, data.partType, this.sprites);
        }
    }
}
