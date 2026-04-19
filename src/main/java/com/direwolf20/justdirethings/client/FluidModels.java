package com.direwolf20.justdirethings.client;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.client.fluid.FluidTintSources;

import java.awt.*;
import java.util.function.Supplier;

@EventBusSubscriber(modid = JustDireThings.MODID, value = Dist.CLIENT)
public final class FluidModels {
    private static final Material STILL = material("block/fluid_source");
    private static final Material FLOW = material("block/fluid_flowing");
    private static final Material OVERLAY = material("block/fluid_overlay");

    private FluidModels() {
    }

    @SubscribeEvent
    static void onRegisterFluidModels(RegisterFluidModelsEvent event) {
        // The polymorphic fluid block renders with a white tint so the per-frame overlay
        // (see PolymorphicFluidOverlayRenderer) can supply the shifting rainbow color without
        // requiring chunk remeshes. Bucket / GUI contexts still get the animated rainbow via
        // color(FluidState).
        register(event, JDTRegistration.POLYMORPHIC_FLUID_SOURCE, JDTRegistration.POLYMORPHIC_FLUID_FLOWING, rainbowTint());
        register(event, JDTRegistration.PORTAL_FLUID_SOURCE, JDTRegistration.PORTAL_FLUID_FLOWING, FluidTintSources.constant(0xFF00DD00));
        register(event, JDTRegistration.UNSTABLE_PORTAL_FLUID_SOURCE, JDTRegistration.UNSTABLE_PORTAL_FLUID_FLOWING, FluidTintSources.constant(0xFF9400D3));
        register(event, JDTRegistration.TIME_FLUID_SOURCE, JDTRegistration.TIME_FLUID_FLOWING, FluidTintSources.constant(0x7700FF00));
        register(event, JDTRegistration.XP_FLUID_SOURCE, JDTRegistration.XP_FLUID_FLOWING, FluidTintSources.constant(0xFF32CD32));
        register(event, JDTRegistration.UNREFINED_T2_FLUID_SOURCE, JDTRegistration.UNREFINED_T2_FLUID_FLOWING, FluidTintSources.constant(0xFF8B4500));
        register(event, JDTRegistration.REFINED_T2_FLUID_SOURCE, JDTRegistration.REFINED_T2_FLUID_FLOWING, FluidTintSources.constant(0xFF8B0000));
        register(event, JDTRegistration.UNREFINED_T3_FLUID_SOURCE, JDTRegistration.UNREFINED_T3_FLUID_FLOWING, FluidTintSources.constant(0xFF64D5AD));
        register(event, JDTRegistration.REFINED_T3_FLUID_SOURCE, JDTRegistration.REFINED_T3_FLUID_FLOWING, FluidTintSources.constant(0xFF40C7C7));
        register(event, JDTRegistration.UNREFINED_T4_FLUID_SOURCE, JDTRegistration.UNREFINED_T4_FLUID_FLOWING, FluidTintSources.constant(0xFF36484A));
        register(event, JDTRegistration.REFINED_T4_FLUID_SOURCE, JDTRegistration.REFINED_T4_FLUID_FLOWING, FluidTintSources.constant(0xFF1B2027));
    }

    private static void register(RegisterFluidModelsEvent event,
                                 Supplier<? extends net.minecraft.world.level.material.Fluid> still,
                                 Supplier<? extends net.minecraft.world.level.material.Fluid> flowing,
                                 FluidTintSource tint) {
        event.register(new FluidModel.Unbaked(STILL, FLOW, OVERLAY, tint), still, flowing);
    }

    private static Material material(String path) {
        return new Material(Identifier.fromNamespaceAndPath(JustDireThings.MODID, path));
    }

    /**
     * Cycles per tick of the rainbow hue. One full cycle every 1 / (HUE_SPEED * 20) seconds.
     */
    public static final float HUE_SPEED = 4.0f / 1000.0f;

    private static FluidTintSource rainbowTint() {
        return new FluidTintSource() {
            @Override
            public int color(FluidState state) {
                return currentRainbowArgb(0f);
            }

            @Override
            public int colorInWorld(FluidState fluidState, BlockState blockState, BlockAndTintGetter level, BlockPos pos) {
                // Baked into the chunk mesh — must stay constant or we'd trigger remeshes.
                // Dark gray so the per-frame overlay in PolymorphicFluidOverlayRenderer can
                // saturate cleanly; pure white washes the overlay color out into pastels.
                return 0x4D404040;
            }
        };
    }

    public static int currentRainbowArgb(float partialTicks) {
        var level = Minecraft.getInstance().level;
        float time = level != null ? level.getGameTime() + partialTicks : 0f;
        float hue = (time * HUE_SPEED) % 1.0f;
        if (hue < 0f) hue += 1.0f;
        int rgb = Color.HSBtoRGB(hue, 1.0f, 1.0f);
        return 0xFF000000 | (rgb & 0x00FFFFFF);
    }
}
