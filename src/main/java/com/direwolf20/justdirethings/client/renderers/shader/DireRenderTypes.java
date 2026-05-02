package com.direwolf20.justdirethings.client.renderers.shader;

import com.direwolf20.justdirethings.JustDireThings;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.TextureTransform;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import org.joml.Math;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

// Iris/Optifine fallback path dropped (1.21.1 used per-draw ShaderInstance swap via ShaderStateShard,
// which does not exist in 26.1 — the pipeline's shader is baked in). The portal does not composite
// with shaderpacks; ShaderMods becomes a dead call site until Stage 18 cleanup.
// Animated texture swirl is driven by a TextureTransform whose Matrix4f supplier is re-evaluated
// every draw by RenderType.draw() → DynamicUniforms.writeTransform(..., textureMatrix).
public class DireRenderTypes {

    public static final RenderPipeline PORTAL_ENTITY_PIPELINE = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "pipeline/portal_entity"))
            .withVertexShader(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "core/portal_entity"))
            .withFragmentShader(Identifier.fromNamespaceAndPath(JustDireThings.MODID, "core/portal_entity"))
            .withSampler("Sampler0")
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withCull(false)
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .build();

    private static final TextureTransform PORTAL_SWIRL_TEXTURING = new TextureTransform(
            "portal_swirl_texturing", DireRenderTypes::buildSwirlMatrix);

    private static final Map<String, ShaderRenderType> RENDER_TYPES = Util.make(() -> {
        Map<String, ShaderRenderType> map = new HashMap<>();
        map.put("portal_entity", new ShaderRenderType("portal_entity", (textures, type) -> {
            RenderSetup.RenderSetupBuilder builder = RenderSetup.builder(PORTAL_ENTITY_PIPELINE)
                    .setTextureTransform(PORTAL_SWIRL_TEXTURING);
            int slot = 0;
            for (ShaderTexture texture : textures) {
                builder.withTexture("Sampler" + slot, texture.location());
                slot++;
            }
            return RenderType.create(type.formattedName(), builder.createRenderSetup());
        }));
        return Map.copyOf(map);
    });

    private static Matrix4f buildSwirlMatrix() {
        long millis = Util.getMillis();
        // Use unbounded continuous offsets instead of modulo wraps. The 1.21.1 code wrapped at
        // 110000/60000ms intervals, which produced visible UV jumps on 26.1 (the previous wrap
        // values weren't exactly integer multiples of the texture period after the rotate/scale
        // transform). Float precision is fine here for many days of session length.
        float horizontal = (float) (millis * 8L) / 110000.0F;
        float vertical = (float) (millis * 8L) / 60000.0F;
        Matrix4f transformation = new Matrix4f().setTranslation(-horizontal, vertical, 0.0F);
        transformation.rotateZ(Math.sin((float) millis / 2000.0f) / 18).scale(1.2f);
        // Smuggle a wallclock seconds value through an unused slot of the 2D UV texture matrix
        // so the fragment shader has a smooth time source. Vanilla's GameTime uniform wraps every
        // 20 minutes and pauses with the world, which caused visible stutter/reset.
        float seconds = (float) ((millis % 600000L) / 1000.0);
        transformation.m32(seconds);
        return transformation;
    }

    public static void registerPipelines(RegisterRenderPipelinesEvent event) {
        event.registerPipeline(PORTAL_ENTITY_PIPELINE);
    }

    public static Map<String, ShaderRenderType> getRenderTypes() {
        return RENDER_TYPES;
    }

    public static ShaderRenderType getRenderType(String name) {
        return RENDER_TYPES.get(name);
    }

    private DireRenderTypes() {}

    public static class ShaderRenderType {
        private final String name;
        private final BiFunction<List<ShaderTexture>, ShaderRenderType, RenderType> builder;

        public ShaderRenderType(String name, BiFunction<List<ShaderTexture>, ShaderRenderType, RenderType> builder) {
            this.name = name;
            this.builder = Util.memoize(builder);
        }

        public RenderType using(List<ShaderTexture> textures) {
            return builder.apply(textures, this);
        }

        public String formattedName() {
            return "%s_%s".formatted(JustDireThings.MODID, name);
        }

        public String name() {
            return name;
        }
    }
}
