package com.direwolf20.justdirethings.common.fluids.unstableportalfluid;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.function.Consumer;

import static com.direwolf20.justdirethings.JustDireThings.MODID;

public class UnstablePortalFluidType extends FluidType {
    public UnstablePortalFluidType() {
        super(Properties.create().density(1000).viscosity(1000).temperature(300)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH));
    }

    @Override
    public boolean canConvertToSource(FluidState state, LevelReader reader, BlockPos pos) {
        return false;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            private static final ResourceLocation UNDERWATER_LOCATION = new ResourceLocation("textures/misc/underwater.png"),
                    UNSTABLE_PORTALFLUID_STILL = new ResourceLocation(MODID, "block/fluid_source"),
                    UNSTABLE_PORTALFLUID_FLOW = new ResourceLocation(MODID, "block/fluid_flowing"),
                    UNSTABLE_PORTALFLUID_OVERLAY = new ResourceLocation(MODID, "block/fluid_overlay");

            @Override
            public ResourceLocation getStillTexture() {
                return UNSTABLE_PORTALFLUID_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return UNSTABLE_PORTALFLUID_FLOW;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return UNSTABLE_PORTALFLUID_OVERLAY;
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
        });
    }
}
