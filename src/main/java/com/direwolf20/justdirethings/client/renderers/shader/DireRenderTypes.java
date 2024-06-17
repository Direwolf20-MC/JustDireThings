package com.direwolf20.justdirethings.client.renderers.shader;

import com.direwolf20.justdirethings.JustDireThings;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Borrowed from: https://github.com/Buuz135/Replication/pull/1/files#diff-b0f97cb0ff19aa95d607e08ed017c5ec52f28ad90f276016dd43991696262505
 * Credit: Jared + Buuz135
 */

public class DireRenderTypes extends RenderType {
	private static final Map<String, ShaderRenderType> RENDER_TYPES = Util.make(() -> {
		Map<String, ShaderRenderType> map = new HashMap<>();
		map.put("portal_entity", new ShaderRenderType("portal_entity", DefaultVertexFormat.POSITION_TEX, (textures, renderType) -> {
			CompositeState compState = CompositeState.builder()
					.setShaderState(renderType.shaderState)
					.setTextureState(new FixedMultiTextureStateShard(textures))
					.createCompositeState(false);
			return create(renderType.formattedName(), renderType.format, VertexFormat.Mode.QUADS, 256, false, false, compState);
		}));

		//noinspection Java9CollectionFactory TODO remove when you have more shaders
		return Collections.unmodifiableMap(map);
	});

	public static Map<String, ShaderRenderType> getRenderTypes() {
		return RENDER_TYPES;
	}

	public static ShaderRenderType getRenderType(String name) {
		return getRenderTypes().get(name);
	}

	private DireRenderTypes(String s, VertexFormat v, VertexFormat.Mode m, int i, boolean b, boolean b2, Runnable r, Runnable r2) {
		super(s, v, m, i, b, b2, r, r2);
		throw new IllegalStateException("This class is not meant to be constructed!");
	}

	public static class ShaderRenderType {
		private final String name;
		public ShaderInstance shader;
		private final ShaderStateShard shaderState = new ShaderStateShard(() -> shader);
		private final VertexFormat format;
		private final BiFunction<List<ShaderTexture>, ShaderRenderType, RenderType> builder;
		private final ResourceLocation shaderLocation;

		public ShaderRenderType(String name, VertexFormat format, BiFunction<List<ShaderTexture>, ShaderRenderType, RenderType> builder) {
			this.name = name;
			this.format = format;
			this.builder = Util.memoize(builder);
			this.shaderLocation = ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, this.name);
		}

		public RenderType using(List<ShaderTexture> textures) {
			return builder.apply(textures, this);
		}

		public void register(ResourceProvider resourceManager, BiConsumer<ShaderInstance, Consumer<ShaderInstance>> registerFunc) throws IOException {
			registerFunc.accept(new ShaderInstance(resourceManager, shaderLocation(), format), this::shader);
		}

		public String formattedName() {
			return "%s_%s".formatted(JustDireThings.MODID, name);
		}

		public ResourceLocation shaderLocation() {
			return shaderLocation;
		}

		public String name() {
			return name;
		}

		public VertexFormat format() {
			return format;
		}

		private void shader(ShaderInstance shader) {
			this.shader = shader;
		}
	}
}
