package com.direwolf20.justdirethings.client.particles.alwaysvisibleparticle;

import com.direwolf20.justdirethings.client.particles.ModParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class AlwaysVisibleParticleData implements ParticleOptions {
    ResourceLocation resourceLocation;

    public AlwaysVisibleParticleData(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticles.ALWAYSVISIBLEPARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeResourceLocation(resourceLocation);
    }

    @Override
    public String writeToString() {
        return BuiltInRegistries.PARTICLE_TYPE.getKey(ModParticles.ALWAYSVISIBLEPARTICLE.get()).toString();
    }

    public static final ParticleOptions.Deserializer<AlwaysVisibleParticleData> DESERIALIZER = new ParticleOptions.Deserializer<AlwaysVisibleParticleData>() {
        @Override
        public AlwaysVisibleParticleData fromCommand(ParticleType<AlwaysVisibleParticleData> pParticleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            String RL = reader.readString();

            return new AlwaysVisibleParticleData(new ResourceLocation(RL));
        }

        public AlwaysVisibleParticleData fromNetwork(ParticleType<AlwaysVisibleParticleData> pParticleType, FriendlyByteBuf pBuffer) {
            return new AlwaysVisibleParticleData(pBuffer.readResourceLocation());
        }
    };
}
