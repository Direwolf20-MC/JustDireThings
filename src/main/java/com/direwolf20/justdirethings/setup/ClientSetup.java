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
import com.direwolf20.justdirethings.client.itemcustomrenders.FluidbarDecorator;
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
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
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

        // TODO(port, stage-16): ItemProperties.register(...) is dead in 1.21.4+. Each item that
        // used it (every tool via registerEnabledToolTextures, PocketGenerator, Bows with pull/pulling,
        // FluidCanister fullness, PotionCanister potion_fullness, PortalGunV2 fullness) needs a
        // RangeSelectItemModelProperty / ConditionalItemModelProperty + a client-item JSON.
        // See PORTING §3.4 "1.21.4 — Client items" + §3.3 ID_MAPPER AT note.

        // TODO(port, stage-16): ItemBlockRenderTypes.setRenderLayer is gone. Render layer is now
        // declared on the Block itself or via FluidModel for fluids (see RENDER_PORTING §7). The
        // four fluid blocks below (UNSTABLE_PORTAL_FLUID_*, TIME_FLUID_*) need the translucent
        // layer set via whatever the fluid-model replacement is.
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR,
                Identifier.fromNamespaceAndPath(JustDireThings.MODID, "abilitycooldownoverlay"),
                AbilityCooldownOverlay.INSTANCE);
    }

    // TODO(port, stage-16): ModelEvent.RegisterAdditional is gone in 26.1 — the standalone-model
    // registration path for CreatureCatcher base went away with the client-item JSON system.
    // @SubscribeEvent
    // public static void mrl(ModelEvent.RegisterAdditional e) {
    //     e.register(ModelResourceLocation.standalone(
    //             Identifier.fromNamespaceAndPath(JustDireThings.MODID, "item/creaturecatcher_base")));
    // }

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
        event.register(Registration.GeneratorFluidT1_Container.get(), GeneratorFluidT1Screen::new);
        event.register(Registration.EnergyTransmitter_Container.get(), EnergyTransmitterScreen::new);
        event.register(Registration.BlockSwapperT1_Container.get(), BlockSwapperT1Screen::new);
        event.register(Registration.BlockSwapperT2_Container.get(), BlockSwapperT2Screen::new);
        event.register(Registration.PlayerAccessor_Container.get(), PlayerAccessorScreen::new);
        event.register(Registration.FluidPlacerT1_Container.get(), FluidPlacerT1Screen::new);
        event.register(Registration.FluidPlacerT2_Container.get(), FluidPlacerT2Screen::new);
        event.register(Registration.FluidCollectorT1_Container.get(), FluidCollectorT1Screen::new);
        event.register(Registration.FluidCollectorT2_Container.get(), FluidCollectorT2Screen::new);
        event.register(Registration.PotionCanister_Container.get(), PotionCanisterScreen::new);
        event.register(Registration.ParadoxMachine_Container.get(), ParadoxMachineScreen::new);
        event.register(Registration.InventoryHolder_Container.get(), InventoryHolderScreen::new);
        event.register(Registration.Experience_Holder_Container.get(), ExperienceHolderScreen::new);
    }

    @SubscribeEvent
    public static void registerItemDecorators(RegisterItemDecorationsEvent event) {
        event.register(Registration.TimeWand.get(), new FluidbarDecorator());
        event.register(Registration.PortalGunV2.get(), new FluidbarDecorator());
        event.register(Registration.FluidCanister.get(), new FluidbarDecorator());
        event.register(Registration.PolymorphicWand.get(), new FluidbarDecorator());
        event.register(Registration.PolymorphicWandV2.get(), new FluidbarDecorator());
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
        event.registerBlockEntityRenderer(Registration.FluidPlacerT2BE.get(), FluidPlacerT2BER::new);
        event.registerBlockEntityRenderer(Registration.FluidCollectorT2BE.get(), FluidCollectorT2BER::new);
        event.registerBlockEntityRenderer(Registration.ParadoxMachineBE.get(), ParadoxMachineBER::new);
        event.registerBlockEntityRenderer(Registration.InventoryHolderBE.get(), InventoryHolderBER::new);
        event.registerBlockEntityRenderer(Registration.ExperienceHolderBE.get(), ExperienceHolderBER::new);

        //Entities
        event.registerEntityRenderer(Registration.CreatureCatcherEntity.get(), CreatureCatcherEntityRender::new);
        event.registerEntityRenderer(Registration.PortalEntity.get(), PortalEntityRender::new);
        event.registerEntityRenderer(Registration.PortalProjectile.get(), PortalProjectileRender::new);
        event.registerEntityRenderer(Registration.DecoyEntity.get(), DecoyEntityRender::new);
        event.registerEntityRenderer(Registration.JustDireArrow.get(), JustDireArrowRenderer::new);
        event.registerEntityRenderer(Registration.JustDireAreaEffectCloud.get(), NoopRenderer::new);
        event.registerEntityRenderer(Registration.TimeWandEntity.get(), TimeWandEntityRender::new);
        event.registerEntityRenderer(Registration.ParadoxEntity.get(), ParadoxEntityRender::new);
    }

    @SubscribeEvent
    static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        // TODO(port, stage-16): IClientItemExtensions.getCustomRenderer() is gone in 26.1. The
        // CreatureCatcher custom renderer (JustDireItemRenderer) needs a SpecialModelRenderer or
        // ClientItem JSON rewrite — see RENDER_PORTING §2/§4 + PORTING §3.4 on client-items.
        // event.registerItem(new IClientItemExtensions() {
        //     JustDireItemRenderer diremodel = new JustDireItemRenderer();
        //     @Override public BlockEntityWithoutLevelRenderer getCustomRenderer() { return diremodel; }
        // }, Registration.CreatureCatcher.get());

        final Identifier UNDERWATER_LOCATION = Identifier.parse("textures/misc/underwater.png");
        // Still/flowing/overlay textures + per-fluid tint colors moved to FluidModel JSON in Stage 17
        // (see documentation/RENDER_PORTING.md §7). These registrations now only wire the underwater
        // overlay render; the tint values that used to live in getTintColor() are captured below as
        // reference for the Stage 17 FluidTintSource port:
        //   POLYMORPHIC_FLUID_TYPE      → 0xFFFFFFFF
        //   PORTAL_FLUID_TYPE           → 0xFF00DD00
        //   UNSTABLE_PORTAL_FLUID_TYPE  → 0xFF9400D3
        //   REFINED_T2_FLUID_TYPE       → 0xFF8B0000
        //   REFINED_T3_FLUID_TYPE       → 0xFF40C7C7
        //   REFINED_T4_FLUID_TYPE       → 0xFF1B2027
        //   UNREFINED_T2_FLUID_TYPE     → 0xFF8B4500
        //   UNREFINED_T3_FLUID_TYPE     → 0xFF64D5AD
        //   UNREFINED_T4_FLUID_TYPE     → 0xFF36484A
        //   TIME_FLUID_TYPE             → 0x3300FF00 stack / 0x7700FF00 in-world
        //   XP_FLUID_TYPE               → 0xFF32CD32
        IClientFluidTypeExtensions underwaterOverlay = new IClientFluidTypeExtensions() {
            @Override
            public Identifier getRenderOverlayTexture(Minecraft mc) {
                return UNDERWATER_LOCATION;
            }
        };
        event.registerFluidType(underwaterOverlay, Registration.POLYMORPHIC_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, Registration.PORTAL_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, Registration.UNSTABLE_PORTAL_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, Registration.REFINED_T2_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, Registration.REFINED_T3_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, Registration.REFINED_T4_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, Registration.UNREFINED_T2_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, Registration.UNREFINED_T3_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, Registration.UNREFINED_T4_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, Registration.TIME_FLUID_TYPE.get());
        event.registerFluidType(underwaterOverlay, Registration.XP_FLUID_TYPE.get());
    }

    // TODO(port, stage-16): ItemColors is gone in 26.1 — per-item tint is expressed via
    // ItemTintSource in the items/*.json client-item file. The three registrations here (all
    // bucket items, FluidCanister, PotionCanister) need to migrate to that format.
    // @SubscribeEvent
    // static void itemColors(RegisterColorHandlersEvent.Item event) {
    //     final ItemColors colors = event.getItemColors();
    //     for (var bucket : Registration.BUCKET_ITEMS.getEntries()) {
    //         colors.register((stack, index) -> {
    //             if (index == 1 && stack.getItem() instanceof BucketItem bucketItem) {
    //                 return IClientFluidTypeExtensions.of(bucketItem.content).getTintColor();
    //             }
    //             return 0xFFFFFFFF;
    //         }, bucket.get());
    //     }
    //     colors.register((stack, index) -> {
    //         if (index == 1 && stack.getItem() instanceof FluidCanister) {
    //             return FluidCanister.getFluidColor(stack);
    //         }
    //         return 0xFFFFFFFF;
    //     }, Registration.FluidCanister.get());
    //     colors.register((stack, index) -> {
    //         if (index == 1 && stack.getItem() instanceof PotionCanister) {
    //             return PotionCanister.getPotionColor(stack);
    //         }
    //         return 0xFFFFFFFF;
    //     }, Registration.PotionCanister.get());
    // }
}
