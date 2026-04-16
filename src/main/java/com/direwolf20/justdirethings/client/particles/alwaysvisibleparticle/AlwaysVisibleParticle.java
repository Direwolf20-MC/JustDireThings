package com.direwolf20.justdirethings.client.particles.alwaysvisibleparticle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;

public class AlwaysVisibleParticle extends SingleQuadParticle {

    private final Layer layer;

    public AlwaysVisibleParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, Identifier resourceLocation) {
        super(pLevel, pX, pY, pZ, resolveSprite(resourceLocation));
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.layer = Layer.bySprite(this.sprite);
    }

    private static TextureAtlasSprite resolveSprite(Identifier particleId) {
        TextureAtlas atlas = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.PARTICLES);
        Identifier spriteId = Identifier.fromNamespaceAndPath(particleId.getNamespace(), particleId.getPath() + "_0");
        TextureAtlasSprite sprite = atlas.getSprite(spriteId);
        if (sprite == atlas.missingSprite()) {
            // Fallback — try the un-suffixed form (some particles are single-sprite, not animated).
            sprite = atlas.getSprite(particleId);
        }
        return sprite;
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    protected int getLightCoords(float pPartialTick) {
        return 0xF00080;
    }

    public static ParticleProvider<AlwaysVisibleParticleData> FACTORY =
            (data, level, x, y, z, xAux, yAux, zAux, random) ->
                    new AlwaysVisibleParticle(level, x, y, z, xAux, yAux, zAux, data.resourceLocation);
}
