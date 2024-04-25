package com.direwolf20.justdirethings.setup;


import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.KeyBindings;
import com.direwolf20.justdirethings.client.blockentityrenders.*;
import com.direwolf20.justdirethings.client.blockentityrenders.gooblocks.GooBlockRender_Tier1;
import com.direwolf20.justdirethings.client.blockentityrenders.gooblocks.GooBlockRender_Tier2;
import com.direwolf20.justdirethings.client.blockentityrenders.gooblocks.GooBlockRender_Tier3;
import com.direwolf20.justdirethings.client.blockentityrenders.gooblocks.GooBlockRender_Tier4;
import com.direwolf20.justdirethings.client.entityrenders.CreatureCatcherEntityRender;
import com.direwolf20.justdirethings.client.events.EventKeyInput;
import com.direwolf20.justdirethings.client.events.PlayerEvents;
import com.direwolf20.justdirethings.client.events.RenderHighlight;
import com.direwolf20.justdirethings.client.events.RenderLevelLast;
import com.direwolf20.justdirethings.client.screens.*;
import com.direwolf20.justdirethings.common.items.PocketGenerator;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableItem;
import com.direwolf20.justdirethings.util.NBTHelpers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.energy.IEnergyStorage;

@Mod.EventBusSubscriber(modid = JustDireThings.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    public static void init(final FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.addListener(KeyBindings::onClientInput);

        //Register our Render Events Class
        NeoForge.EVENT_BUS.register(RenderLevelLast.class);
        NeoForge.EVENT_BUS.register(EventKeyInput.class);
        NeoForge.EVENT_BUS.register(RenderHighlight.class);
        NeoForge.EVENT_BUS.register(PlayerEvents.class);

        //Item Properties
        event.enqueueWork(() -> {
            for (var tool : Registration.TOOLS.getEntries()) {
                registerEnabledToolTextures(tool.get());
            }
            registerEnabledToolTextures(Registration.Pocket_Generator.get());
        });
    }

    public static void registerEnabledToolTextures(Item tool) {
        if (tool instanceof ToggleableItem toggleableItem) {
            ItemProperties.register(tool,
                    new ResourceLocation(JustDireThings.MODID, "enabled"), (stack, level, living, id) -> {
                        if (stack.getItem() instanceof PocketGenerator) {
                            if (!toggleableItem.getEnabled(stack)) return 0.0f;
                            IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
                            if (energyStorage == null) return 0.0f;
                            if (energyStorage.getEnergyStored() > 0) return 1.0f;
                            if (!(NBTHelpers.getIntValue(stack, PocketGenerator.COUNTER) > 0)) return 0.0f;
                            return 1.0f;
                        } else
                            return toggleableItem.getEnabled(stack) ? 1.0f : 0.0f;
                    });
        }
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(Registration.FuelCanister_Container.get(), FuelCanisterScreen::new);
        event.register(Registration.PocketGenerator_Container.get(), PocketGeneratorScreen::new);
        event.register(Registration.Tool_Settings_Container.get(), ToolSettingScreen::new);
        event.register(Registration.Item_Collector_Container.get(), ItemCollectorScreen::new);
        event.register(Registration.BlockBreakerT1_Container.get(), BlockBreakerT1Screen::new);
        event.register(Registration.BlockBreakerT2_Container.get(), BlockBreakerT2Screen::new);
        event.register(Registration.BlockPlacerT1_Container.get(), BlockPlacerT1Screen::new);
        event.register(Registration.BlockPlacerT2_Container.get(), BlockPlacerT2Screen::new);
        event.register(Registration.ClickerT1_Container.get(), ClickerT1Screen::new);
        event.register(Registration.ClickerT2_Container.get(), ClickerT2Screen::new);
        event.register(Registration.SensorT1_Container.get(), SensorT1Screen::new);
        event.register(Registration.SensorT2_Container.get(), SensorT2Screen::new);
        event.register(Registration.DropperT1_Container.get(), DropperT1Screen::new);
        event.register(Registration.DropperT2_Container.get(), DropperT2Screen::new);
        event.register(Registration.GeneratorT1_Container.get(), GeneratorT1Screen::new);
        event.register(Registration.EnergyTransmitter_Container.get(), EnergyTransmitterScreen::new);
        event.register(Registration.BlockSwapperT1_Container.get(), BlockSwapperT1Screen::new);
        event.register(Registration.BlockSwapperT2_Container.get(), BlockSwapperT2Screen::new);
        event.register(Registration.PlayerAccessor_Container.get(), PlayerAccessorScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        //Register Block Entity Renders
        event.registerBlockEntityRenderer(Registration.GooBlockBE_Tier1.get(), GooBlockRender_Tier1::new);
        event.registerBlockEntityRenderer(Registration.GooBlockBE_Tier2.get(), GooBlockRender_Tier2::new);
        event.registerBlockEntityRenderer(Registration.GooBlockBE_Tier3.get(), GooBlockRender_Tier3::new);
        event.registerBlockEntityRenderer(Registration.GooBlockBE_Tier4.get(), GooBlockRender_Tier4::new);
        event.registerBlockEntityRenderer(Registration.ItemCollectorBE.get(), ItemCollectorRenderer::new);
        event.registerBlockEntityRenderer(Registration.BlockBreakerT2BE.get(), BlockBreakerT2BER::new);
        event.registerBlockEntityRenderer(Registration.BlockPlacerT2BE.get(), BlockPlacerT2BER::new);
        event.registerBlockEntityRenderer(Registration.ClickerT2BE.get(), ClickerT2BER::new);
        event.registerBlockEntityRenderer(Registration.SensorT2BE.get(), SensorT2BER::new);
        event.registerBlockEntityRenderer(Registration.DropperT2BE.get(), DropperT2BER::new);
        event.registerBlockEntityRenderer(Registration.EnergyTransmitterBE.get(), EnergyTransmitterRenderer::new);
        event.registerBlockEntityRenderer(Registration.BlockSwapperT2BE.get(), BlockSwapperT2BER::new);
        event.registerBlockEntityRenderer(Registration.EclipseGateBE.get(), EclipseGateRenderer::new);

        //Entities
        event.registerEntityRenderer(Registration.CreatureCatcherEntity.get(), CreatureCatcherEntityRender::new);
    }
}
