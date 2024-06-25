package com.direwolf20.justdirethings.common.fluids.unstableportalfluid;

import com.direwolf20.justdirethings.datagen.JustDireBiomeTags;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

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
    public boolean isVaporizedOnPlacement(Level level, BlockPos pos, FluidStack stack) {
        return !level.getBiome(pos).is(JustDireBiomeTags.UNSTABLE_PORTAL_FLUID_VIABLE);
    }

    public void onVaporize(@Nullable Player player, Level level, BlockPos pos, FluidStack stack) {
        SoundEvent sound = this.getSound(player, level, pos, SoundActions.FLUID_VAPORIZE);
        level.playSound(player, pos, sound != null ? sound : SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);

        for (int l = 0; l < 8; ++l)
            level.addAlwaysVisibleParticle(ParticleTypes.DRAGON_BREATH, (double) pos.getX() + Math.random(), (double) pos.getY() + Math.random(), (double) pos.getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
    }

    @Override
    public boolean canConvertToSource(FluidState state, LevelReader reader, BlockPos pos) {
        return false;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            private static final ResourceLocation UNDERWATER_LOCATION = ResourceLocation.parse("textures/misc/underwater.png"),
                    UNSTABLE_PORTALFLUID_STILL = ResourceLocation.fromNamespaceAndPath(MODID, "block/fluid_source"),
                    UNSTABLE_PORTALFLUID_FLOW = ResourceLocation.fromNamespaceAndPath(MODID, "block/fluid_flowing"),
                    UNSTABLE_PORTALFLUID_OVERLAY = ResourceLocation.fromNamespaceAndPath(MODID, "block/fluid_overlay");

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
