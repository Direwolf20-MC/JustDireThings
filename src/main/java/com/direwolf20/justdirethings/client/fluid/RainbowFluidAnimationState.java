package com.direwolf20.justdirethings.client.fluid;

import com.direwolf20.justdirethings.client.FluidModels;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.util.ARGB;

/**
 * AnimationState that re-tints the base sprite with the current rainbow color every client tick,
 * uploads the tinted pixels to a private GpuTexture, and blits that texture into the block atlas
 * during the animation draw pass (mirroring vanilla's ANIMATE_SPRITE_BLIT path).
 * <p>
 * We subclass vanilla's AnimationState (exposed via accesstransformer.cfg) but override every
 * method so none of the parent's fields (frame/subFrame/animationInfo) get read. The
 * animationInfo passed to super is used by vanilla only as a reference passed back to us via
 * {@link #getDrawUbo}, which we also override.
 */
final class RainbowFluidAnimationState extends SpriteContents.AnimationState {
    private final int mipLevels;
    private final GpuTexture tintedTexture;
    private final GpuTextureView tintedView;
    private final GpuBufferSlice[] spriteUbosByMip;
    private final int frameCount;
    private final int framesPerRow;

    /**
     * Base ARGB pixels cached per mip level. Each entry covers the entire sprite sheet at that
     * mip, indexed {@code [y * sheetWidthAtMip + x]}. Frames are addressed via framesPerRow.
     * Levels where the frame size would degenerate to 0 are {@code null} and skipped during tick.
     */
    private final int[][] baseSheetPixels;
    private final int[] sheetWidthByMip;

    /**
     * Reusable scratch images used for each mip's tick upload. {@code null} for degenerate mips.
     */
    private final NativeImage[] scratchByMip;

    private int tickCount;

    RainbowFluidAnimationState(SpriteContents contents,
                               SpriteContents.AnimatedTexture animationInfo,
                               GpuBufferSlice uboSlice,
                               int spriteUboSize) {
        // AnimationState is a non-static inner class of SpriteContents, so the parent
        // constructor requires the enclosing instance explicitly.
        contents.super(animationInfo, Int2ObjectMaps.emptyMap(), sliceAllMips(contents, uboSlice, spriteUboSize));
        this.mipLevels = contents.byMipLevel.length;

        this.spriteUbosByMip = new GpuBufferSlice[mipLevels];
        for (int i = 0; i < mipLevels; i++) {
            spriteUbosByMip[i] = uboSlice.slice((long) i * spriteUboSize, spriteUboSize);
        }

        int width = contents.width();
        int height = contents.height();
        NativeImage originalMip0 = contents.byMipLevel[0];
        this.framesPerRow = Math.max(1, originalMip0.getWidth() / width);
        int framesPerCol = Math.max(1, originalMip0.getHeight() / height);
        this.frameCount = Math.max(1, framesPerRow * framesPerCol);

        // Cache the full sheet pixels at each mip so tick can just read + multiply.
        this.baseSheetPixels = new int[mipLevels][];
        this.sheetWidthByMip = new int[mipLevels];
        this.scratchByMip = new NativeImage[mipLevels];
        for (int m = 0; m < mipLevels; m++) {
            int fw = width >> m;
            int fh = height >> m;
            if (fw <= 0 || fh <= 0) continue;
            NativeImage mipImage = contents.byMipLevel[m];
            int sheetW = mipImage.getWidth();
            int sheetH = mipImage.getHeight();
            sheetWidthByMip[m] = sheetW;
            int[] buf = new int[sheetW * sheetH];
            for (int y = 0; y < sheetH; y++) {
                for (int x = 0; x < sheetW; x++) {
                    buf[y * sheetW + x] = mipImage.getPixel(x, y);
                }
            }
            baseSheetPixels[m] = buf;
            scratchByMip[m] = new NativeImage(NativeImage.Format.RGBA, fw, fh, false);
        }

        this.tintedTexture = RenderSystem.getDevice().createTexture(
                () -> contents.name() + " rainbow-tinted",
                GpuTexture.USAGE_COPY_DST | GpuTexture.USAGE_TEXTURE_BINDING,
                TextureFormat.RGBA8,
                width,
                height,
                1,
                mipLevels
        );
        this.tintedView = RenderSystem.getDevice().createTextureView(tintedTexture);

        // Seed all mips with an initial tint so the very first draw is not black.
        retint();
    }

    private static GpuBufferSlice[] sliceAllMips(SpriteContents contents, GpuBufferSlice uboSlice, int spriteUboSize) {
        GpuBufferSlice[] out = new GpuBufferSlice[contents.byMipLevel.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = uboSlice.slice((long) i * spriteUboSize, spriteUboSize);
        }
        return out;
    }

    @Override
    public void tick() {
        tickCount++;
        retint();
    }

    private void retint() {
        int color = FluidModels.currentRainbowArgb(0f);
        // Drive frame index off tickCount. Source mcmeta uses frametime=2 and flowing uses the
        // default of 1; pick the slower one here and accept that flowing will look a touch slower
        // than vanilla would render it — close enough to the old overlay behavior.
        int frame = frameCount > 0 ? (tickCount / 2) % frameCount : 0;
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        for (int m = 0; m < mipLevels; m++) {
            NativeImage scratch = scratchByMip[m];
            int[] sheet = baseSheetPixels[m];
            if (scratch == null || sheet == null) continue;
            int fw = scratch.getWidth();
            int fh = scratch.getHeight();
            int sheetW = sheetWidthByMip[m];
            int fx = (frame % framesPerRow) * fw;
            int fy = (frame / framesPerRow) * fh;
            for (int y = 0; y < fh; y++) {
                int rowStart = (fy + y) * sheetW + fx;
                for (int x = 0; x < fw; x++) {
                    int src = sheet[rowStart + x];
                    int sa = ARGB.alpha(src);
                    int tr = clampChannel(ARGB.red(src) * r);
                    int tg = clampChannel(ARGB.green(src) * g);
                    int tb = clampChannel(ARGB.blue(src) * b);
                    scratch.setPixel(x, y, ARGB.color(sa, tr, tg, tb));
                }
            }
            RenderSystem.getDevice()
                    .createCommandEncoder()
                    .writeToTexture(tintedTexture, scratch, m, 0, 0, 0, fw, fh, 0, 0);
        }
    }

    private static int clampChannel(float v) {
        if (v <= 0f) return 0;
        if (v >= 255f) return 255;
        return (int) v;
    }

    @Override
    public GpuBufferSlice getDrawUbo(int level) {
        return spriteUbosByMip[level];
    }

    @Override
    public boolean needsToDraw() {
        // Rainbow shifts every tick; always redraw so the atlas stays in sync.
        return true;
    }

    @Override
    public void drawToAtlas(RenderPass renderPass, GpuBufferSlice ubo) {
        GpuSampler sampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST, true);
        renderPass.setPipeline(RenderPipelines.ANIMATE_SPRITE_BLIT);
        renderPass.bindTexture("Sprite", tintedView, sampler);
        renderPass.setUniform("SpriteAnimationInfo", ubo);
        renderPass.draw(0, 6);
    }

    @Override
    public void close() {
        tintedView.close();
        tintedTexture.close();
        for (NativeImage img : scratchByMip) {
            if (img != null) img.close();
        }
    }
}
