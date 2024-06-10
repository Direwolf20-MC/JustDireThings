package com.direwolf20.justdirethings.common.fluids.polymorphicfluid;

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

import java.awt.*;
import java.util.function.Consumer;

import static com.direwolf20.justdirethings.JustDireThings.MODID;

public class PolymorphicFluidType extends FluidType {
    public PolymorphicFluidType() {
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
                    POLYMORPHICFLUID_STILL = new ResourceLocation(MODID, "block/fluid_source"),
                    POLYMORPHICFLUID_FLOW = new ResourceLocation(MODID, "block/fluid_flowing"),
                    POLYMORPHICFLUID_OVERLAY = new ResourceLocation(MODID, "block/fluid_overlay");

            @Override
            public ResourceLocation getStillTexture() {
                return POLYMORPHICFLUID_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return POLYMORPHICFLUID_FLOW;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return POLYMORPHICFLUID_OVERLAY;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return UNDERWATER_LOCATION;
            }

            @Override
            public int getTintColor() {
                /*Level level = Minecraft.getInstance().level;
                if (level != null) {
                    float colorChangeSpeed = 0.5f;
                    return getRainbowColor(level.getGameTime(), colorChangeSpeed);
                }*/
                return 0xFFFFFFFF; // Default color if getter is not an instance of Level
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                /*Level level = Minecraft.getInstance().level;
                if (level != null) {
                    float colorChangeSpeed = 0.5f;
                    return getRainbowColor(level.getGameTime(), colorChangeSpeed);
                }*/
                return 0xFFFFFFFF; // Default color if getter is not an instance of Level
            }

            private int getRainbowColor(long worldTime, float speed) {
                float hue = (worldTime * speed / 1000.0f) % 1.0f;
                int rgb = Color.HSBtoRGB(hue, 1.0f, 1.0f);
                //System.out.printf("Time: %d, Hue: %f, RGB: #%06X\n", worldTime, hue, rgb & 0xFFFFFF);
                return (0xFF << 24) | (rgb & 0xFFFFFF); // Add alpha channel to the RGB value
            }
        });
    }
}
