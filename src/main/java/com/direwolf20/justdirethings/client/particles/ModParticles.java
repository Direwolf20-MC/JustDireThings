package com.direwolf20.justdirethings.client.particles;


import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.particles.alwaysvisibleparticle.AlwaysVisibleParticleType;
import com.direwolf20.justdirethings.client.particles.glitterparticle.GlitterParticleType;
import com.direwolf20.justdirethings.client.particles.gooexplodeparticle.GooExplodeParticleType;
import com.direwolf20.justdirethings.client.particles.itemparticle.ItemFlowParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, JustDireThings.MODID);
    public static final DeferredHolder<ParticleType<?>, GooExplodeParticleType> GOOEXPLODEPARTICLE = PARTICLE_TYPES.register("gooexplodeparticle", () -> new GooExplodeParticleType(false));
    public static final DeferredHolder<ParticleType<?>, ItemFlowParticleType> ITEMFLOWPARTICLE = PARTICLE_TYPES.register("itemflowparticle", () -> new ItemFlowParticleType(false));
    public static final DeferredHolder<ParticleType<?>, AlwaysVisibleParticleType> ALWAYSVISIBLEPARTICLE = PARTICLE_TYPES.register("alwaysvisibleparticle", () -> new AlwaysVisibleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, GlitterParticleType> GLITTERPARTICLE = PARTICLE_TYPES.register("glitter", () -> new GlitterParticleType(false));
}