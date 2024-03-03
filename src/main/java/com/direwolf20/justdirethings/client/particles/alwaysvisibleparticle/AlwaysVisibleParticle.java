package com.direwolf20.justdirethings.client.particles.alwaysvisibleparticle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

public class AlwaysVisibleParticle extends TextureSheetParticle {
    ParticleRenderType AlwaysOn = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder pBuilder, TextureManager pTextureManager) {
            RenderSystem.disableBlend();
            RenderSystem.depthMask(false);
            RenderSystem.setShader(GameRenderer::getParticleShader);
            RenderSystem.disableDepthTest();
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            pBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator pTesselator) {
            pTesselator.end();
            RenderSystem.enableDepthTest();
        }

        @Override
        public String toString() {
            return "AlwaysOn";
        }
    };

    public AlwaysVisibleParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, ResourceLocation resourceLocation) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;
        SpriteSet spriteSet = particleEngine.spriteSets.get(resourceLocation);
        if (spriteSet == null)
            spriteSet = particleEngine.spriteSets.get(new ResourceLocation("minecraft", "smoke"));
        this.pickSprite(spriteSet);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return AlwaysOn;
    }

    protected int getLightColor(float pPartialTick) {
        return 0xF00080;
    }

    public static ParticleProvider<AlwaysVisibleParticleData> FACTORY =
            (data, level, x, y, z, xSpeed, ySpeed, zSpeed) -> {
                AlwaysVisibleParticle alwaysVisibleParticle = new AlwaysVisibleParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, data.resourceLocation);
                return alwaysVisibleParticle;
            };
}
