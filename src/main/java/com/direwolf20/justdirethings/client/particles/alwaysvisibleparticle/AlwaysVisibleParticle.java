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
    static final ParticleRenderType AlwaysOn = new ParticleRenderType() {
        @Override
        public BufferBuilder begin(Tesselator p_350993_, TextureManager p_107442_) {
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            RenderSystem.setShader(GameRenderer::getParticleShader);
            RenderSystem.disableDepthTest();
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
            return p_350993_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
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
            spriteSet = particleEngine.spriteSets.get(ResourceLocation.withDefaultNamespace("smoke"));
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
