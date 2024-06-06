package com.direwolf20.justdirethings.client.renderers;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class OurRenderTypes extends RenderType {
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

    public static final RenderType SolidBoxArea = create("SolidBoxArea",
            DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING) // view_offset_z_layering
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setTextureState(NO_TEXTURE)
                    .setDepthTestState(LEQUAL_DEPTH_TEST)
                    .setCullState(NO_CULL)
                    .setLightmapState(NO_LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false));

    public static final RenderType SolidBoxAreaOpaque = create("SolidBoxArea",
            DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING) // view_offset_z_layering
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setTextureState(NO_TEXTURE)
                    .setDepthTestState(LEQUAL_DEPTH_TEST)
                    .setCullState(NO_CULL)
                    .setLightmapState(NO_LIGHTMAP)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .createCompositeState(false));

    public static final RenderType TRANSPARENT_BOX = create("transparent_box",
            DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, true, true,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)  // Use the translucent shader
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)  // View offset Z layering
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)  // Enable translucent transparency
                    .setTextureState(NO_TEXTURE)  // No texture state
                    .setDepthTestState(LEQUAL_DEPTH_TEST)  // Depth test state
                    .setCullState(NO_CULL)  // No cull state
                    .setLightmapState(NO_LIGHTMAP)  // No lightmap state
                    .setWriteMaskState(COLOR_WRITE)  // Only write color
                    .createCompositeState(true));  // Enable sort on transparency

    public static final RenderType TRIANGLE_STRIP =
            create("triangle_strip", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_STRIP, 256, false, false,
                    RenderType.CompositeState.builder()
                            .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
                            .setTextureState(NO_TEXTURE)
                            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                            .setCullState(NO_CULL)
                            .setLightmapState(NO_LIGHTMAP)
                            .createCompositeState(false));

    public OurRenderTypes(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }

    /*public static void updateRenders() { //Only used when testing
        TRANSPARENT_BOX = create("transparent_box",
                DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, true, true,
                RenderType.CompositeState.builder()
                        .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)  // Use the translucent shader
                        .setLayeringState(VIEW_OFFSET_Z_LAYERING)  // View offset Z layering
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)  // Enable translucent transparency
                        .setTextureState(NO_TEXTURE)  // No texture state
                        .setDepthTestState(LEQUAL_DEPTH_TEST)  // Depth test state
                        .setCullState(NO_CULL)  // No cull state
                        .setLightmapState(NO_LIGHTMAP)  // No lightmap state
                        .setWriteMaskState(COLOR_WRITE)  // Only write color
                        .createCompositeState(true));  // Enable sort on transparency
    }*/
}
