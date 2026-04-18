package com.direwolf20.justdirethings.client.renderers;

import com.direwolf20.justdirethings.JustDireThings;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;

import java.util.Optional;

public final class OurRenderTypes {

    public static final RenderPipeline ORE_XRAY_PIPELINE = RenderPipelines.SOLID_BLOCK.toBuilder()
            .withLocation(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "pipeline/ore_xray"))
            .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN, true))
            .build();

    public static final RenderPipeline RENDER_BLOCK_BACKFACE_PIPELINE = RenderPipelines.TRANSLUCENT_BLOCK.toBuilder()
            .withLocation(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "pipeline/render_block_backface"))
            .withCull(false)
            .withDepthStencilState(new DepthStencilState(CompareOp.EQUAL, true))
            .build();

    public static final RenderPipeline GOO_PATTERN_PIPELINE = RenderPipelines.TRANSLUCENT_BLOCK.toBuilder()
            .withLocation(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "pipeline/goo_pattern"))
            .withCull(false)
            .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true))
            .withColorTargetState(new ColorTargetState(Optional.empty(), ColorTargetState.WRITE_NONE))
            .build();

    public static final RenderPipeline TRIANGLE_STRIP_PIPELINE = RenderPipeline.builder(RenderPipelines.GUI_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "pipeline/triangle_strip"))
            .withCull(false)
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_STRIP)
            .build();

    public static final RenderType OreXRAY = RenderType.create("OreXRAY",
            RenderSetup.builder(ORE_XRAY_PIPELINE)
                    .withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS)
                    .useLightmap()
                    .useOverlay()
                    .createRenderSetup());

    public static final RenderType RenderBlockBackface = RenderType.create("GadgetRenderBlockBackface",
            RenderSetup.builder(RENDER_BLOCK_BACKFACE_PIPELINE)
                    .withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS)
                    .useLightmap()
                    .useOverlay()
                    .createRenderSetup());

    public static final RenderType GooPattern = RenderType.create("GooPattern",
            RenderSetup.builder(GOO_PATTERN_PIPELINE)
                    .withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS)
                    .useLightmap()
                    .createRenderSetup());

    public static final RenderType SolidBoxArea = RenderType.create("SolidBoxArea",
            RenderSetup.builder(RenderPipelines.DEBUG_QUADS)
                    .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                    .createRenderSetup());

    public static final RenderType SolidBoxAreaOpaque = RenderType.create("SolidBoxAreaOpaque",
            RenderSetup.builder(RenderPipelines.DEBUG_QUADS)
                    .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                    .createRenderSetup());

    public static final RenderType TRANSPARENT_BOX = RenderType.create("transparent_box",
            RenderSetup.builder(RenderPipelines.DEBUG_QUADS)
                    .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                    .sortOnUpload()
                    .createRenderSetup());

    public static final RenderType TRIANGLE_STRIP = RenderType.create("triangle_strip",
            RenderSetup.builder(TRIANGLE_STRIP_PIPELINE)
                    .createRenderSetup());

    public static final RenderType RenderBlockFade = RenderType.create("RenderBlockFade",
            RenderSetup.builder(RenderPipelines.TRANSLUCENT_BLOCK)
                    .withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS)
                    .useLightmap()
                    .useOverlay()
                    .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                    .createRenderSetup());

    public static final RenderType RenderBlockFadeNoCull = RenderType.create("GadgetRenderBlockFadeNoCull",
            RenderSetup.builder(RENDER_BLOCK_BACKFACE_PIPELINE)
                    .withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS)
                    .useLightmap()
                    .useOverlay()
                    .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                    .createRenderSetup());

    public static RenderType lines() {
        return RenderTypes.lines();
    }

    public static final RenderType LINES = RenderTypes.lines();

    public static void registerPipelines(RegisterRenderPipelinesEvent event) {
        event.registerPipeline(ORE_XRAY_PIPELINE);
        event.registerPipeline(RENDER_BLOCK_BACKFACE_PIPELINE);
        event.registerPipeline(GOO_PATTERN_PIPELINE);
        event.registerPipeline(TRIANGLE_STRIP_PIPELINE);
    }

    private OurRenderTypes() {}
}
