package com.direwolf20.justdirethings.common.fluids.portalfluid;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.function.Consumer;

import static com.direwolf20.justdirethings.JustDireThings.MODID;

public class PortalFluidType extends FluidType {
    public PortalFluidType() {
        super(FluidType.Properties.create().density(1000).viscosity(1000).temperature(300));
    }

    @Override
    public boolean canConvertToSource(FluidState state, LevelReader reader, BlockPos pos) {
        return false;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            private static final ResourceLocation UNDERWATER_LOCATION = new ResourceLocation("textures/misc/underwater.png"),
                    PORTALFLUID_STILL = new ResourceLocation(MODID, "block/portal_fluid_source"),
                    PORTALFLUID_FLOW = new ResourceLocation(MODID, "block/portal_fluid_flowing"),
                    PORTALFLUID_OVERLAY = new ResourceLocation(MODID, "block/portal_fluid_overlay");

            @Override
            public ResourceLocation getStillTexture() {
                return PORTALFLUID_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return PORTALFLUID_FLOW;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return PORTALFLUID_OVERLAY;
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
        });
    }
}
