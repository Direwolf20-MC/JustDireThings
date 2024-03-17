package com.direwolf20.justdirethings.client.particles;


import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.particles.alwaysvisibleparticle.AlwaysVisibleParticle;
import com.direwolf20.justdirethings.client.particles.gooexplodeparticle.GooExplodeParticle;
import com.direwolf20.justdirethings.client.particles.itemparticle.ItemFlowParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@Mod.EventBusSubscriber(modid = JustDireThings.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleRenderDispatcher {

    @SubscribeEvent
    public static void registerProviders(RegisterParticleProvidersEvent evt) {
        evt.registerSpecial(ModParticles.GOOEXPLODEPARTICLE.get(), GooExplodeParticle.FACTORY);
        evt.registerSpecial(ModParticles.ITEMFLOWPARTICLE.get(), ItemFlowParticle.FACTORY);
        evt.registerSpecial(ModParticles.ALWAYSVISIBLEPARTICLE.get(), AlwaysVisibleParticle.FACTORY);
    }
}
