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
import com.direwolf20.justdirethings.client.renderers.JustDireItemRenderer;
import com.direwolf20.justdirethings.client.renderers.RenderHelpers;
import com.direwolf20.justdirethings.client.renderers.shader.DireRenderTypes;
import com.direwolf20.justdirethings.client.screens.*;
import com.direwolf20.justdirethings.common.items.FluidCanister;
import com.direwolf20.justdirethings.common.items.PocketGenerator;
import com.direwolf20.justdirethings.common.items.PortalGunV2;
import com.direwolf20.justdirethings.common.items.PotionCanister;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableItem;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseBow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.io.IOException;

import static com.direwolf20.justdirethings.JustDireThings.MODID;

@EventBusSubscriber(modid = JustDireThings.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
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
            for (var bow : Registration.BOWS.getEntries()) {
                if (bow.get() instanceof BaseBow baseBow) {
                    ItemProperties.register(bow.get(), ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "pull"), (stack, level, living, id) -> {
                        if (living == null || living.getUseItem() != stack) return 0.0F;
                        return (stack.getUseDuration(living) - (living.getUseItemRemainingTicks() + (20 - baseBow.getMaxDraw()))) / baseBow.getMaxDraw();
                    });
                    ItemProperties.register(bow.get(), ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "pulling"), (stack, level, living, id) -> {
                        return living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F;
                    });
                }
            }
        });

        event.enqueueWork(() -> {
            ItemProperties.register(Registration.FluidCanister.get(),
                    ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "fullness"), (stack, level, living, id) -> FluidCanister.getFullness(stack));
        });

        event.enqueueWork(() -> {
            ItemProperties.register(Registration.PotionCanister.get(),
                    ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "potion_fullness"), (stack, level, living, id) -> PotionCanister.getFullness(stack));
        });

        event.enqueueWork(() -> {
            ItemProperties.register(Registration.PortalGunV2.get(),
                    ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "fullness"), (stack, level, living, id) -> PortalGunV2.getFullness(stack));
        });

        ItemBlockRenderTypes.setRenderLayer(Registration.UNSTABLE_PORTAL_FLUID_SOURCE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(Registration.UNSTABLE_PORTAL_FLUID_FLOWING.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(Registration.TIME_FLUID_SOURCE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(Registration.TIME_FLUID_FLOWING.get(), RenderType.translucent());
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "abilitycooldownoverlay"), AbilityCooldownOverlay.INSTANCE);
    }

    private static void onTexturesStitched(final TextureAtlasStitchedEvent event) {
        //noinspection deprecation
        if (event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            RenderHelpers.captureDummySprite(event.getAtlas());
        }
    }

    public static void registerEnabledToolTextures(Item tool) {
        if (tool instanceof ToggleableItem toggleableItem) {
            ItemProperties.register(tool,
                    ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "enabled"), (stack, level, living, id) -> {
                        if (stack.getItem() instanceof PocketGenerator) {
                            if (!toggleableItem.getEnabled(stack)) return 0.0f;
                            IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
                            if (energyStorage == null) return 0.0f;
                            if (energyStorage.getEnergyStored() > 0) return 1.0f;
                            if (!(stack.getOrDefault(JustDireDataComponents.POCKETGEN_COUNTER, 0) > 0)) return 0.0f;
                            return 1.0f;
                        } else
                            return toggleableItem.getEnabled(stack) ? 1.0f : 0.0f;
                    });
        }
    }

    @SubscribeEvent
    public static void mrl(ModelEvent.RegisterAdditional e) {
        e.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "item/creaturecatcher_base")));
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PortalProjectileModel.Portal_Projectile_Layer, PortalProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    private static void registerShaders(RegisterShadersEvent event) {
        try {
            for(DireRenderTypes.ShaderRenderType type : DireRenderTypes.getRenderTypes().values()) {
                type.register(event.getResourceProvider(), event::registerShader);
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
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
    }

    @SubscribeEvent
    public static void registerItemDecorators(RegisterItemDecorationsEvent event) {
        event.register(Registration.TimeWand.get(), new FluidbarDecorator());
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
        event.registerItem(new IClientItemExtensions() {
            JustDireItemRenderer diremodel = new JustDireItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return diremodel;
            }
        }, Registration.CreatureCatcher.get());

        final ResourceLocation UNDERWATER_LOCATION = ResourceLocation.parse("textures/misc/underwater.png");
        final ResourceLocation WATER_STILL = ResourceLocation.fromNamespaceAndPath(MODID, "block/fluid_source");
        final ResourceLocation WATER_FLOW = ResourceLocation.fromNamespaceAndPath(MODID, "block/fluid_flowing");
        final ResourceLocation WATER_OVERLAY = ResourceLocation.fromNamespaceAndPath(MODID, "block/fluid_overlay");
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return WATER_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return WATER_FLOW;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return WATER_OVERLAY;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return UNDERWATER_LOCATION;
            }

            @Override
            public int getTintColor() {
                return 0xFFFFFFFF;
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return 0xFFFFFFFF;
            }
        }, Registration.POLYMORPHIC_FLUID_TYPE.get());
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return WATER_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return WATER_FLOW;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return WATER_OVERLAY;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return UNDERWATER_LOCATION;
            }

            @Override
            public int getTintColor() {
                return 0xFF00DD00;
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return 0xFF00DD00;
            }
        }, Registration.PORTAL_FLUID_TYPE.get());
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return WATER_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return WATER_FLOW;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return WATER_OVERLAY;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return UNDERWATER_LOCATION;
            }

            @Override
            public int getTintColor() {
                return 0xFF9400D3;
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return 0xFF9400D3;
            }
        }, Registration.UNSTABLE_PORTAL_FLUID_TYPE.get());
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return WATER_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return WATER_FLOW;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return WATER_OVERLAY;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return UNDERWATER_LOCATION;
            }

            @Override
            public int getTintColor() {
                return 0xFF8B0000;
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return 0xFF8B0000;
            }
        }, Registration.REFINED_T2_FLUID_TYPE.get());
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return WATER_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return WATER_FLOW;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return WATER_OVERLAY;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return UNDERWATER_LOCATION;
            }

            @Override
            public int getTintColor() {
                return 0xFF40C7C7;
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return 0xFF40C7C7;
            }
        }, Registration.REFINED_T3_FLUID_TYPE.get());
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return WATER_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return WATER_FLOW;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return WATER_OVERLAY;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return UNDERWATER_LOCATION;
            }

            @Override
            public int getTintColor() {
                return 0xFF1B2027;
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return 0xFF1B2027;
            }
        }, Registration.REFINED_T4_FLUID_TYPE.get());
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return WATER_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return WATER_FLOW;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return WATER_OVERLAY;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return UNDERWATER_LOCATION;
            }

            @Override
            public int getTintColor() {
                return 0xFF8B4500;
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return 0xFF8B4500;
            }
        }, Registration.UNREFINED_T2_FLUID_TYPE.get());
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return WATER_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return WATER_FLOW;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return WATER_OVERLAY;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return UNDERWATER_LOCATION;
            }

            @Override
            public int getTintColor() {
                return 0xFF64D5AD;
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return 0xFF64D5AD;
            }
        }, Registration.UNREFINED_T3_FLUID_TYPE.get());
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return WATER_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return WATER_FLOW;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return WATER_OVERLAY;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return UNDERWATER_LOCATION;
            }

            @Override
            public int getTintColor() {
                return 0xFF36484A;
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return 0xFF36484A;
            }
        }, Registration.UNREFINED_T4_FLUID_TYPE.get());
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return WATER_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return WATER_FLOW;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return WATER_OVERLAY;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return UNDERWATER_LOCATION;
            }

            @Override
            public int getTintColor() {
                return 0x3300FF00;
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return 0x7700FF00;
            }
        }, Registration.TIME_FLUID_TYPE.get());
    }

    @SubscribeEvent
    static void itemColors(RegisterColorHandlersEvent.Item event) {
        final ItemColors colors = event.getItemColors();

        for (var bucket : Registration.BUCKET_ITEMS.getEntries()) {
            colors.register((stack, index) -> {
                if (index == 1 && stack.getItem() instanceof BucketItem bucketItem) {
                    return IClientFluidTypeExtensions.of(bucketItem.content).getTintColor();
                }
                return 0xFFFFFFFF;
            }, bucket.get());
        }

        colors.register((stack, index) -> {
            if (index == 1 && stack.getItem() instanceof FluidCanister) {
                return FluidCanister.getFluidColor(stack);
            }
            return 0xFFFFFFFF;
        }, Registration.FluidCanister.get());

        colors.register((stack, index) -> {
            if (index == 1 && stack.getItem() instanceof PotionCanister) {
                return PotionCanister.getPotionColor(stack);
            }
            return 0xFFFFFFFF;
        }, Registration.PotionCanister.get());
    }
}
