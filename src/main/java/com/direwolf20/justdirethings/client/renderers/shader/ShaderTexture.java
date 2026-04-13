package com.direwolf20.justdirethings.client.renderers.shader;

import net.minecraft.resources.Identifier;
/**
 * Basic tuple for a shader texture
 * Borrowed from: https://github.com/Buuz135/Replication/pull/1/files#diff-b0f97cb0ff19aa95d607e08ed017c5ec52f28ad90f276016dd43991696262505
 * Credit: Jared + Buuz135
 */

public record ShaderTexture(Identifier location, boolean blur, boolean mipmap) {
	public ShaderTexture(Identifier location, boolean blur) {
		this(location, blur, false);
	}
	public ShaderTexture(Identifier location) {
		this(location, false, false);
	}
}
