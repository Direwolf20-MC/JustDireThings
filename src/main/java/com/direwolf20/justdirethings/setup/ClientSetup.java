package com.direwolf20.justdirethings.setup;


import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.KeyBindings;
import com.direwolf20.justdirethings.client.blockentityrenders.ItemCollectorRenderer;
import com.direwolf20.justdirethings.client.blockentityrenders.gooblocks.GooBlockRender_Tier1;
import com.direwolf20.justdirethings.client.blockentityrenders.gooblocks.GooBlockRender_Tier2;
import com.direwolf20.justdirethings.client.blockentityrenders.gooblocks.GooBlockRender_Tier3;
import com.direwolf20.justdirethings.client.blockentityrenders.gooblocks.GooBlockRender_Tier4;
import com.direwolf20.justdirethings.client.events.EventKeyInput;
import com.direwolf20.justdirethings.client.events.RenderHighlight;
import com.direwolf20.justdirethings.client.events.RenderLevelLast;
import com.direwolf20.justdirethings.client.screens.FuelCanisterScreen;
import com.direwolf20.justdirethings.client.screens.PocketGeneratorScreen;
import com.direwolf20.justdirethings.client.screens.ToolSettingScreen;
import com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod.EventBusSubscriber(modid = JustDireThings.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    public static void init(final FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.addListener(KeyBindings::onClientInput);

        //Register our Render Events Class
        NeoForge.EVENT_BUS.register(RenderLevelLast.class);
        NeoForge.EVENT_BUS.register(EventKeyInput.class);
        NeoForge.EVENT_BUS.register(RenderHighlight.class);

        //Item Properties
        event.enqueueWork(() -> {
            ItemProperties.register(Registration.BlazegoldPickaxe.get(),
                    new ResourceLocation(JustDireThings.MODID, "enabled"), (stack, level, living, id) -> {
                        return ToggleableTool.getEnabled(stack) ? 1.0f : 0.0f;
                    });
        });

    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(Registration.FuelCanister_Container.get(), FuelCanisterScreen::new);
        event.register(Registration.PocketGenerator_Container.get(), PocketGeneratorScreen::new);
        event.register(Registration.Tool_Settings_Container.get(), ToolSettingScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        //Register Block Entity Renders
        event.registerBlockEntityRenderer(Registration.GooBlockBE_Tier1.get(), GooBlockRender_Tier1::new);
        event.registerBlockEntityRenderer(Registration.GooBlockBE_Tier2.get(), GooBlockRender_Tier2::new);
        event.registerBlockEntityRenderer(Registration.GooBlockBE_Tier3.get(), GooBlockRender_Tier3::new);
        event.registerBlockEntityRenderer(Registration.GooBlockBE_Tier4.get(), GooBlockRender_Tier4::new);
        event.registerBlockEntityRenderer(Registration.ItemCollectorBE.get(), ItemCollectorRenderer::new);
    }
}
