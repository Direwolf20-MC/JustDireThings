package com.direwolf20.justdirethings.client.particles;


import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.particles.alwaysvisibleparticle.AlwaysVisibleParticleData;
import com.direwolf20.justdirethings.client.particles.alwaysvisibleparticle.AlwaysVisibleParticleType;
import com.direwolf20.justdirethings.client.particles.itemparticle.ItemFlowParticleData;
import com.direwolf20.justdirethings.client.particles.itemparticle.ItemFlowParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, JustDireThings.MODID);
    public static final Supplier<ParticleType<ItemFlowParticleData>> ITEMFLOWPARTICLE = PARTICLE_TYPES.register("itemflowparticle", ItemFlowParticleType::new);
    public static final Supplier<ParticleType<AlwaysVisibleParticleData>> ALWAYSVISIBLEPARTICLE = PARTICLE_TYPES.register("alwaysvisibleparticle", AlwaysVisibleParticleType::new);
}