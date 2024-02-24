package com.direwolf20.justdirethings.client.renderers;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class OurRenderTypes extends RenderType {
    public static RenderType RenderBlockBackface = create("GadgetRenderBlockBackface",
            DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder()
//                    .setShadeModelState(SMOOTH_SHADE)
                    .setShaderState(RenderStateShard.RENDERTYPE_SOLID_SHADER)
                    .setLightmapState(LIGHTMAP)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(LEQUAL_DEPTH_TEST)
                    .setCullState(NO_CULL)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .createCompositeState(false));

    /*public static RenderType.CompositeState overlay = create("overlayComposite",
            DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder()
            .setShaderState(RENDERTYPE_ENTITY_ALPHA_SHADER)
            .setTextureState(BLOCK_SHEET_MIPPED)
            .setCullState(NO_CULL)
            .createCompositeState(true));*/


    private static Function<ResourceLocation, RenderType> GooPattern = Util.memoize(
            p_286150_ -> {
                RenderType.CompositeState overlay = RenderType.CompositeState.builder()
                        .setShaderState(RenderStateShard.RENDERTYPE_TRANSLUCENT_SHADER)
                        .setLightmapState(LIGHTMAP)
                        .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setTextureState(new RenderStateShard.TextureStateShard(p_286150_, false, false))
                        .setDepthTestState(LEQUAL_DEPTH_TEST)
                        .setCullState(NO_CULL)
                        .setWriteMaskState(COLOR_DEPTH_WRITE)
                        .createCompositeState(true);
                return create("GooPattern", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 1536, false, false, overlay);
            }
    );

    public static void updateRenders() {
        GooPattern = Util.memoize(
                p_286150_ -> {
                    RenderType.CompositeState overlay = RenderType.CompositeState.builder()
                            .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_ALPHA_SHADER)
                            .setLightmapState(LIGHTMAP)
                            //.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                            //.setDepthTestState(LEQUAL_DEPTH_TEST)
                            .setTextureState(new RenderStateShard.TextureStateShard(p_286150_, false, false))
                            .setCullState(NO_CULL)
                            //.setOverlayState(RenderStateShard.NO_OVERLAY)
                            .setWriteMaskState(RenderStateShard.DEPTH_WRITE)
                            .createCompositeState(true);
                    return create("GooPattern", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 1536, false, false, overlay);
                });

        RenderBlockBackface = create("GadgetRenderBlockBackface",
                DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, false,
                RenderType.CompositeState.builder()
                        .setShaderState(RenderStateShard.RENDERTYPE_TRANSLUCENT_SHADER)
                        .setLightmapState(LIGHTMAP)
                        .setTextureState(BLOCK_SHEET)
                        //.setLayeringState(RenderStateShard.NO_LAYERING)
                        //.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setDepthTestState(GREATER_DEPTH_TEST)
                        .setCullState(CULL)
                        //.setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                        .setOverlayState(RenderStateShard.OVERLAY)
                        .createCompositeState(false));
    }

    public static RenderType dragonExplosionAlpha(ResourceLocation pId) {
        return GooPattern.apply(pId);
    }


    public OurRenderTypes(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }
}
