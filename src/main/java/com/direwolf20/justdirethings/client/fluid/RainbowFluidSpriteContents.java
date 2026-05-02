package com.direwolf20.justdirethings.client.fluid;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * SpriteContents backed by a static base image whose hue is re-tinted each tick from
 * {@link com.direwolf20.justdirethings.client.FluidModels#currentRainbowArgb(float)}.
 * The actual per-tick GPU work lives in {@link RainbowFluidAnimationState}.
 */
public final class RainbowFluidSpriteContents extends SpriteContents {
    public RainbowFluidSpriteContents(Identifier name,
                                      FrameSize frameSize,
                                      NativeImage image,
                                      Optional<AnimationMetadataSection> animationInfo,
                                      List<MetadataSectionType.WithValue<?>> additionalMetadata,
                                      Optional<TextureMetadataSection> textureInfo) {
        super(name, frameSize, image, animationInfo, additionalMetadata, textureInfo);
    }

    @Override
    public boolean isAnimated() {
        // Force the atlas to give us an AnimationState slot even for mcmeta-less / single-frame
        // base images — we need ticks to re-tint.
        return true;
    }

    @Override
    public @Nullable AnimationState createAnimationState(GpuBufferSlice uboSlice, int spriteUboSize) {
        // Reuse vanilla's AnimatedTexture so frame indexing / ubo layout match; we only swap in
        // a custom tickable state that does the per-frame recolor upload.
        return new RainbowFluidAnimationState(this, this.animatedTexture, uboSlice, spriteUboSize);
    }
}
