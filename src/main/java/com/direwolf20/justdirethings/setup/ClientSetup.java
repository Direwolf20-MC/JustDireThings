package com.direwolf20.justdirethings.setup;


import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.KeyBindings;
import com.direwolf20.justdirethings.client.blockentityrenders.*;
import com.direwolf20.justdirethings.client.blockentityrenders.gooblocks.GooBlockRender_Tier1;
import com.direwolf20.justdirethings.client.blockentityrenders.gooblocks.GooBlockRender_Tier2;
import com.direwolf20.justdirethings.client.blockentityrenders.gooblocks.GooBlockRender_Tier3;
import com.direwolf20.justdirethings.client.blockentityrenders.gooblocks.GooBlockRender_Tier4;
import com.direwolf20.justdirethings.client.entitymodels.PortalProjectileModel;
import com.direwolf20.justdirethings.client.entityrenders.*;
import com.direwolf20.justdirethings.client.events.EventKeyInput;
import com.direwolf20.justdirethings.client.events.PlayerEvents;
import com.direwolf20.justdirethings.client.events.RenderHighlight;
import com.direwolf20.justdirethings.client.events.RenderLevelLast;
import com.direwolf20.justdirethings.client.itemcustomrenders.*;
import com.direwolf20.justdirethings.client.overlays.AbilityCooldownOverlay;
import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.direwolf20.justdirethings.client.renderers.shader.DireRenderTypes;
import com.direwolf20.justdirethings.client.screens.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(modid = JustDireThings.MODID, value = Dist.CLIENT)
public class ClientSetup {
    public static void init(final FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.addListener(KeyBindings::onClientInput);

        //Register our Render Events Class
        NeoForge.EVENT_BUS.register(RenderLevelLast.class);
        NeoForge.EVENT_BUS.register(EventKeyInput.class);
        NeoForge.EVENT_BUS.register(RenderHighlight.class);
        NeoForge.EVENT_BUS.register(PlayerEvents.class);

        // Item model properties (tool enabled-state swap, fluid/potion/portal fullness range-select dispatches,
        // bucket/canister tint sources) are registered via the RegisterConditionalItemModelPropertyEvent /
        // RegisterRangeSelectItemModelPropertyEvent / RegisterColorHandlersEvent.ItemTintSources handlers below.
        // Render layers for fluids are declared on the FluidModel JSON produced by datagen — no Java-side
        // ItemBlockRenderTypes call is needed in 26.1.
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR,
                Identifier.fromNamespaceAndPath(JustDireThings.MODID, "abilitycooldownoverlay"),
                AbilityCooldownOverlay.INSTANCE);
    }

    @SubscribeEvent
    public static void onRegisterConditionalItemModelProperties(RegisterConditionalItemModelPropertyEvent event) {
        event.register(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "tool_enabled"), ToolEnabledProperty.MAP_CODEC);
    }

    @SubscribeEvent
    public static void onRegisterRangeSelectItemModelProperties(RegisterRangeSelectItemModelPropertyEvent event) {
        event.register(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "fluid_canister_fullness"), FluidCanisterFullnessProperty.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "potion_canister_fullness"), PotionCanisterFullnessProperty.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "portal_gun_fullness"), PortalGunFullnessProperty.MAP_CODEC);
    }

    @SubscribeEvent
    public static void onRegisterItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "bucket_fluid"), BucketFluidTintSource.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "fluid_canister"), FluidCanisterTintSource.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "potion_canister"), PotionCanisterTintSource.MAP_CODEC);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PortalProjectileModel.Portal_Projectile_Layer, PortalProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterRenderPipelines(RegisterRenderPipelinesEvent event) {
        OurRenderTypes.registerPipelines(event);
        DireRenderTypes.registerPipelines(event);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(JDTRegistration.FuelCanister_Container.get(), FuelCanisterScreen::new);
        event.register(JDTRegistration.PocketGenerator_Container.get(), PocketGeneratorScreen::new);
        event.register(JDTRegistration.Tool_Settings_Container.get(), ToolSettingScreen::new);
        event.register(JDTRegistration.Item_Collector_Container.get(), ItemCollectorScreen::new);
        event.register(JDTRegistration.BlockBreakerT1_Container.get(), BlockBreakerT1Screen::new);
        event.register(JDTRegistration.BlockBreakerT2_Container.get(), BlockBreakerT2Screen::new);
        event.register(JDTRegistration.BlockPlacerT1_Container.get(), BlockPlacerT1Screen::new);
        event.register(JDTRegistration.BlockPlacerT2_Container.get(), BlockPlacerT2Screen::new);
        event.register(JDTRegistration.ClickerT1_Container.get(), ClickerT1Screen::new);
        event.register(JDTRegistration.ClickerT2_Container.get(), ClickerT2Screen::new);
        event.register(JDTRegistration.SensorT1_Container.get(), SensorT1Screen::new);
        event.register(JDTRegistration.SensorT2_Container.get(), SensorT2Screen::new);
        event.register(JDTRegistration.DropperT1_Container.get(), DropperT1Screen::new);
        event.register(JDTRegistration.DropperT2_Container.get(), DropperT2Screen::new);
        event.register(JDTRegistration.GeneratorT1_Container.get(), GeneratorT1Screen::new);
        event.register(JDTRegistration.GeneratorFluidT1_Container.get(), GeneratorFluidT1Screen::new);
        event.register(JDTRegistration.EnergyTransmitter_Container.get(), EnergyTransmitterScreen::new);
        event.register(JDTRegistration.BlockSwapperT1_Container.get(), BlockSwapperT1Screen::new);
        event.register(JDTRegistration.BlockSwapperT2_Container.get(), BlockSwapperT2Screen::new);
        event.register(JDTRegistration.PlayerAccessor_Container.get(), PlayerAccessorScreen::new);
        event.register(JDTRegistration.FluidPlacerT1_Container.get(), FluidPlacerT1Screen::new);
        event.register(JDTRegistration.FluidPlacerT2_Container.get(), FluidPlacerT2Screen::new);
        event.register(JDTRegistration.FluidCollectorT1_Container.get(), FluidCollectorT1Screen::new);
        event.register(JDTRegistration.FluidCollectorT2_Container.get(), FluidCollectorT2Screen::new);
        event.register(JDTRegistration.PotionCanister_Container.get(), PotionCanisterScreen::new);
        event.register(JDTRegistration.ParadoxMachine_Container.get(), ParadoxMachineScreen::new);
        event.register(JDTRegistration.InventoryHolder_Container.get(), InventoryHolderScreen::new);
        event.register(JDTRegistration.Experience_Holder_Container.get(), ExperienceHolderScreen::new);
    }

    @SubscribeEvent
    public static void registerItemDecorators(RegisterItemDecorationsEvent event) {
        event.register(JDTRegistration.TimeWand.get(), new FluidbarDecorator());
        event.register(JDTRegistration.PortalGunV2.get(), new FluidbarDecorator());
        event.register(JDTRegistration.FluidCanister.get(), new FluidbarDecorator());
        event.register(JDTRegistration.PolymorphicWand.get(), new FluidbarDecorator());
        event.register(JDTRegistration.PolymorphicWandV2.get(), new FluidbarDecorator());
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        //Register Block Entity Renders
        event.registerBlockEntityRenderer(JDTRegistration.GooBlockBE_Tier1.get(), GooBlockRender_Tier1::new);
        event.registerBlockEntityRenderer(JDTRegistration.GooBlockBE_Tier2.get(), GooBlockRender_Tier2::new);
        event.registerBlockEntityRenderer(JDTRegistration.GooBlockBE_Tier3.get(), GooBlockRender_Tier3::new);
        event.registerBlockEntityRenderer(JDTRegistration.GooBlockBE_Tier4.get(), GooBlockRender_Tier4::new);
        event.registerBlockEntityRenderer(JDTRegistration.ItemCollectorBE.get(), ItemCollectorRenderer::new);
        event.registerBlockEntityRenderer(JDTRegistration.BlockBreakerT2BE.get(), BlockBreakerT2BER::new);
        event.registerBlockEntityRenderer(JDTRegistration.BlockPlacerT2BE.get(), BlockPlacerT2BER::new);
        event.registerBlockEntityRenderer(JDTRegistration.ClickerT2BE.get(), ClickerT2BER::new);
        event.registerBlockEntityRenderer(JDTRegistration.SensorT2BE.get(), SensorT2BER::new);
        event.registerBlockEntityRenderer(JDTRegistration.DropperT2BE.get(), DropperT2BER::new);
        event.registerBlockEntityRenderer(JDTRegistration.EnergyTransmitterBE.get(), EnergyTransmitterRenderer::new);
        event.registerBlockEntityRenderer(JDTRegistration.BlockSwapperT2BE.get(), BlockSwapperT2BER::new);
        event.registerBlockEntityRenderer(JDTRegistration.EclipseGateBE.get(), EclipseGateRenderer::new);
        event.registerBlockEntityRenderer(JDTRegistration.FluidPlacerT2BE.get(), FluidPlacerT2BER::new);
        event.registerBlockEntityRenderer(JDTRegistration.FluidCollectorT2BE.get(), FluidCollectorT2BER::new);
        event.registerBlockEntityRenderer(JDTRegistration.ParadoxMachineBE.get(), ParadoxMachineBER::new);
        event.registerBlockEntityRenderer(JDTRegistration.InventoryHolderBE.get(), InventoryHolderBER::new);
        event.registerBlockEntityRenderer(JDTRegistration.ExperienceHolderBE.get(), ExperienceHolderBER::new);

        //Entities
        event.registerEntityRenderer(JDTRegistration.CreatureCatcherEntity.get(), CreatureCatcherEntityRender::new);
        event.registerEntityRenderer(JDTRegistration.PortalEntity.get(), PortalEntityRender::new);
        event.registerEntityRenderer(JDTRegistration.PortalProjectile.get(), PortalProjectileRender::new);
        event.registerEntityRenderer(JDTRegistration.DecoyEntity.get(), DecoyEntityRender::new);
        event.registerEntityRenderer(JDTRegistration.JustDireArrow.get(), JustDireArrowRenderer::new);
        event.registerEntityRenderer(JDTRegistration.JustDireAreaEffectCloud.get(), NoopRenderer::new);
        event.registerEntityRenderer(JDTRegistration.TimeWandEntity.get(), TimeWandEntityRender::new);
        event.registerEntityRenderer(JDTRegistration.ParadoxEntity.get(), ParadoxEntityRender::new);
    }

    @SubscribeEvent
    static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        final Identifier UNDERWATER_LOCATION = Identifier.parse("textures/misc/underwater.png");
        IClientFluidTypeExtensions underwaterOverlay = new IClientFluidTypeExtensions() {
            @Override
            public Identifier getRenderOverlayTexture(Minecraft mc) {
                return UNDERWATER_LOCATION;
            }
        };
        event.registerFluidType(underwaterOverlay, JDTRegistration.POLYMORPHIC_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, JDTRegistration.PORTAL_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, JDTRegistration.UNSTABLE_PORTAL_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, JDTRegistration.REFINED_T2_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, JDTRegistration.REFINED_T3_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, JDTRegistration.REFINED_T4_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, JDTRegistration.UNREFINED_T2_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, JDTRegistration.UNREFINED_T3_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, JDTRegistration.UNREFINED_T4_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, JDTRegistration.TIME_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, JDTRegistration.XP_FLUID_TYPE.get());
    }
}
