package com.direwolf20.justdirethings.client.particles;


import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.particles.alwaysvisibleparticle.AlwaysVisibleParticle;
import com.direwolf20.justdirethings.client.particles.glitterparticle.GlitterParticleType;
import com.direwolf20.justdirethings.client.particles.gooexplodeparticle.GooExplodeParticle;
import com.direwolf20.justdirethings.client.particles.itemparticle.ItemFlowParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = JustDireThings.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ParticleRenderDispatcher {

    @SubscribeEvent
    public static void registerProviders(RegisterParticleProvidersEvent evt) {
        evt.registerSpecial(ModParticles.GOOEXPLODEPARTICLE.get(), GooExplodeParticle.FACTORY);
        evt.registerSpecial(ModParticles.ITEMFLOWPARTICLE.get(), ItemFlowParticle.FACTORY);
        evt.registerSpecial(ModParticles.ALWAYSVISIBLEPARTICLE.get(), AlwaysVisibleParticle.FACTORY);
        evt.registerSpriteSet(ModParticles.GLITTERPARTICLE.get(), GlitterParticleType.FACTORY::new);
    }
}
