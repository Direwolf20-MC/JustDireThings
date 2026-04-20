package com.direwolf20.justdirethings.client.fluid;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Optional;

/**
 * SpriteSource that wraps a texture asset and emits a {@link RainbowFluidSpriteContents} for it.
 * Drop an entry like {@code { "type": "justdirethings:rainbow_fluid", "resource":
 * "justdirethings:block/fluid_source" }} into an atlas JSON ({@code atlases/blocks.json}).
 */
public final class RainbowFluidSpriteSource implements SpriteSource {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final FileToIdConverter TEXTURE_ID_CONVERTER = new FileToIdConverter("textures", ".png");

    public static final MapCodec<RainbowFluidSpriteSource> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("resource").forGetter(src -> src.resource),
                    Identifier.CODEC.fieldOf("sprite").forGetter(src -> src.sprite)
            ).apply(instance, RainbowFluidSpriteSource::new)
    );

    private final Identifier resource;
    private final Identifier sprite;

    public RainbowFluidSpriteSource(Identifier resource, Identifier sprite) {
        this.resource = resource;
        this.sprite = sprite;
    }

    @Override
    public void run(ResourceManager resourceManager, Output output) {
        Identifier textureFile = TEXTURE_ID_CONVERTER.idToFile(resource);
        Optional<Resource> optionalResource = resourceManager.getResource(textureFile);
        if (optionalResource.isEmpty()) {
            LOGGER.warn("Missing texture for rainbow_fluid sprite source: {}", textureFile);
            return;
        }
        Resource res = optionalResource.get();
        output.add(sprite, (SpriteSource.DiscardableLoader) (SpriteResourceLoader loader) ->
                loadTinted(loader, sprite, res));
    }

    private static @Nullable SpriteContents loadTinted(SpriteResourceLoader loader,
                                                       Identifier id,
                                                       Resource res) {
        return loader.loadSprite(id, res, RainbowFluidSpriteContents::new);
    }

    @Override
    public MapCodec<? extends SpriteSource> codec() {
        return CODEC;
    }
}
