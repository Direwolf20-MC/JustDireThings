package com.direwolf20.justdirethings.client.particles.itemparticle;


import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ItemFlowParticleType extends ParticleType<ItemFlowParticleData> {
    public ItemFlowParticleType(boolean pOverrideLimiter) {
        super(pOverrideLimiter);
    }

    public ItemFlowParticleType getType() {
        return this;
    }

    @Override
    public MapCodec<ItemFlowParticleData> codec() {
        return ItemFlowParticleData.MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ItemFlowParticleData> streamCodec() {
        return ItemFlowParticleData.STREAM_CODEC;
    }

}
