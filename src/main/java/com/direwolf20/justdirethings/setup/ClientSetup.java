package com.direwolf20.justdirethings.setup;


import com.direwolf20.justdirethings.JustDireThings;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod.EventBusSubscriber(modid = JustDireThings.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    public static void init(final FMLClientSetupEvent event) {
        //NeoForge.EVENT_BUS.addListener(KeyBindings::onClientInput);

        //Register our Render Events Class
        //NeoForge.EVENT_BUS.register(RenderLevelLast.class);
        //NeoForge.EVENT_BUS.register(EventKeyInput.class);

        //Screens
        /*event.enqueueWork(() -> {
            MenuScreens.register(Registration.TemplateManager_Container.get(), TemplateManagerGUI::new); // Attach our container to the screen
        });*/
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        //Register Block Entity Renders
        //event.registerBlockEntityRenderer(Registration.RenderBlock_BE.get(), RenderBlockBER::new);
    }
}
