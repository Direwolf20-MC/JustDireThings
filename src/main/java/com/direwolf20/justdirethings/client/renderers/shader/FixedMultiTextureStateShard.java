package com.direwolf20.justdirethings.client.renderers.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * The vanilla {@link MultiTextureStateShard} class uses `i++` to set each texture,
 * however the lightmap and overlay texture are hardcoded to use id 1 and 2 respectively. Which overrides any texture set to those IDs.
 * This class will skip ID 1 and 2, and start putting any extra textures on ID 3 and above.
 * Borrowed from: https://github.com/Buuz135/Replication/pull/1/files#diff-b0f97cb0ff19aa95d607e08ed017c5ec52f28ad90f276016dd43991696262505
 * Credit: Jared + Buuz135
 */
public class FixedMultiTextureStateShard extends RenderStateShard.EmptyTextureStateShard {

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private final Optional<ResourceLocation> cutoutTexture;

	public FixedMultiTextureStateShard(List<ShaderTexture> textures) {
		super(() -> {
			int i = 0;

			for (ShaderTexture texture : textures) {
				TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
				texturemanager.getTexture(texture.location()).setFilter(texture.blur(), texture.mipmap());
				RenderSystem.setShaderTexture(i, texture.location());
				if (i == 0) {
					i = 3;
				} else {
					i++;
				}
			}

		}, () -> {
		});
		this.cutoutTexture = textures.stream().findFirst().map(ShaderTexture::location);
	}

	protected @NotNull Optional<ResourceLocation> cutoutTexture() {
		return this.cutoutTexture;
	}
}