package com.direwolf20.justdirethings.client.particles.alwaysvisibleparticle;

import com.direwolf20.justdirethings.client.renderers.OurRenderTypes;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;

import java.io.BufferedReader;
import java.util.Optional;

public class AlwaysVisibleParticle extends SingleQuadParticle {

    // Custom particle layer bound to the depth-test-disabled pipeline so the scanner markers
    // render through solid geometry. The `translucent` flag being false matches OPAQUE_PARTICLE,
    // which is the pipeline we derived from.
    private static final Layer ALWAYS_ON_LAYER = new Layer(false, TextureAtlas.LOCATION_PARTICLES,
            OurRenderTypes.ALWAYS_VISIBLE_PARTICLE_PIPELINE);

    public AlwaysVisibleParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, Identifier resourceLocation) {
        super(pLevel, pX, pY, pZ, resolveSprite(resourceLocation));
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
    }

    private static TextureAtlasSprite resolveSprite(Identifier particleId) {
        TextureAtlas atlas = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.PARTICLES);
        TextureAtlasSprite missing = atlas.missingSprite();

        // Try the common animated form first: happy_villager -> happy_villager_0
        TextureAtlasSprite sprite = atlas.getSprite(Identifier.fromNamespaceAndPath(particleId.getNamespace(), particleId.getPath() + "_0"));
        if (sprite != missing) return sprite;

        // Single-sprite particles where the sprite id equals the particle id
        sprite = atlas.getSprite(particleId);
        if (sprite != missing) return sprite;

        // Last resort: read the particle's JSON description from resources to find the real sprite id
        // (e.g. happy_villager.json points at "minecraft:glint")
        return resolveFromParticleJson(atlas, particleId).orElse(missing);
    }

    private static Optional<TextureAtlasSprite> resolveFromParticleJson(TextureAtlas atlas, Identifier particleId) {
        Identifier jsonPath = Identifier.fromNamespaceAndPath(particleId.getNamespace(), "particles/" + particleId.getPath() + ".json");
        Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(jsonPath);
        if (resource.isEmpty()) return Optional.empty();
        try (BufferedReader reader = resource.get().openAsReader()) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray textures = root.getAsJsonArray("textures");
            if (textures == null || textures.isEmpty()) return Optional.empty();
            JsonElement first = textures.get(0);
            Identifier spriteId = Identifier.parse(first.getAsString());
            TextureAtlasSprite sprite = atlas.getSprite(spriteId);
            return sprite == atlas.missingSprite() ? Optional.empty() : Optional.of(sprite);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Layer getLayer() {
        return ALWAYS_ON_LAYER;
    }

    @Override
    protected int getLightCoords(float pPartialTick) {
        return 0xF00080;
    }

    public static ParticleProvider<AlwaysVisibleParticleData> FACTORY =
            (data, level, x, y, z, xAux, yAux, zAux, random) ->
                    new AlwaysVisibleParticle(level, x, y, z, xAux, yAux, zAux, data.resourceLocation);
}
