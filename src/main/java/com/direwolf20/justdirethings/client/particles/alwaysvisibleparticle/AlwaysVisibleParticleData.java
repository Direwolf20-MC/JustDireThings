package com.direwolf20.justdirethings.client.particles.alwaysvisibleparticle;

import com.direwolf20.justdirethings.client.particles.ModParticles;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public class AlwaysVisibleParticleData implements ParticleOptions {
    public static final MapCodec<AlwaysVisibleParticleData> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            ResourceLocation.CODEC.fieldOf("resourceLocation").forGetter(AlwaysVisibleParticleData::getResourceLocation)
                    )
                    .apply(instance, AlwaysVisibleParticleData::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AlwaysVisibleParticleData> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            AlwaysVisibleParticleData::getResourceLocation,
            AlwaysVisibleParticleData::new
    );

    ResourceLocation resourceLocation;

    public AlwaysVisibleParticleData(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticles.ALWAYSVISIBLEPARTICLE.get();
    }

}
