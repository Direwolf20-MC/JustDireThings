package com.direwolf20.justdirethings.client.renderers;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class OurRenderTypes extends RenderType {
    /*public static final RenderType RenderBlock = create("GadgetRenderBlock",
            DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.RENDERTYPE_SOLID_SHADER)
                    .setLightmapState(LIGHTMAP)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(LEQUAL_DEPTH_TEST)
                    .setCullState(CULL)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false));*/
    public static final RenderType OreXRAY = create("OreXRAY",
            DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.RENDERTYPE_SOLID_SHADER)
                    .setLightmapState(LIGHTMAP)
                    .setTextureState(BLOCK_SHEET)
                    .setTransparencyState(NO_TRANSPARENCY)
                    .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                    .setDepthTestState(GREATER_DEPTH_TEST)
                    .createCompositeState(false));

    public static final RenderType RenderBlockBackface = create("GadgetRenderBlockBackface",
            DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.RENDERTYPE_TRANSLUCENT_SHADER)
                    .setLightmapState(LIGHTMAP)
                    .setTextureState(BLOCK_SHEET)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(EQUAL_DEPTH_TEST)
                    .setCullState(NO_CULL)
                    .setOverlayState(RenderStateShard.OVERLAY)
                    .createCompositeState(false));

    public static final RenderType GooPattern = create("GooPattern",
            DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_ALPHA_SHADER)
                    .setLightmapState(LIGHTMAP)
                    .setTextureState(BLOCK_SHEET)
                    .setCullState(NO_CULL)
                    .setWriteMaskState(RenderStateShard.DEPTH_WRITE)
                    .createCompositeState(true));

    /*private static final Function<ResourceLocation, RenderType> GooPattern = Util.memoize(
            p_286150_ -> {
                RenderType.CompositeState overlay = RenderType.CompositeState.builder()
                        .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_ALPHA_SHADER)
                        .setLightmapState(LIGHTMAP)
                        .setTextureState(BLOCK_SHEET)
                        .setCullState(NO_CULL)
                        .setWriteMaskState(RenderStateShard.DEPTH_WRITE)
                        .createCompositeState(true);
                return create("GooPattern", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 1536, false, false, overlay);
            });

    public static RenderType gooPatternAlpha(ResourceLocation pId) {
        return GooPattern.apply(pId);
    }*/

    public OurRenderTypes(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }

    /*public static void updateRenders() { //Only used when testing
        OreXRAY = create("OreXRAY",
                DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, false,
                RenderType.CompositeState.builder()
                        .setShaderState(RenderStateShard.RENDERTYPE_SOLID_SHADER)
                        .setLightmapState(LIGHTMAP)
                        .setTextureState(BLOCK_SHEET)
                        .setTransparencyState(NO_TRANSPARENCY)
                        .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                        .setDepthTestState(GREATER_DEPTH_TEST)
                        .createCompositeState(false));
    }*/
}
