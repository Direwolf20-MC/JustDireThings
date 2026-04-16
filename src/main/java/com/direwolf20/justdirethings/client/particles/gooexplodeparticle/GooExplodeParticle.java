package com.direwolf20.justdirethings.client.particles.gooexplodeparticle;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.AtlasIds;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.Random;

public class GooExplodeParticle extends BreakingItemParticle {
    Random random = new Random();
    private float partSize;

    public GooExplodeParticle(ClientLevel world, double x, double y, double z, TextureAtlasSprite sprite) {
        super(world, x, y, z, sprite);
        float minSize = 0.25f;
        float maxSize = 0.5f;
        this.partSize = minSize + random.nextFloat() * (maxSize - minSize);
        this.lifetime = 30;
        int longLifeChance = random.nextInt(20);
        if (longLifeChance == 0)
            this.lifetime = 120;

        this.scale(partSize);
        this.partSize = quadSize;
    }

    @Override
    public void tick() {
        super.tick();
        updateColorAndGravity();
    }

    public void updateColorAndGravity() {
        float relativeAge = (float) ((this.lifetime - this.age)) / this.lifetime; //1.0 -> 0.0
        float adjustedAge;

        if (relativeAge < 0.5f) {
            adjustedAge = (float) Math.pow(relativeAge / 0.5f, 2);
            this.alpha = Mth.lerp(adjustedAge, 0.25f, 1);
        }
    }

    @Override //Performance Reasons
    protected int getLightCoords(float pPartialTick) {
        return 0xF00080;
    }

    private static TextureAtlasSprite resolveSprite(ClientLevel world, ItemStack stack, RandomSource random) {
        Minecraft mc = Minecraft.getInstance();
        ItemStack target = stack.isEmpty() ? new ItemStack(Blocks.COBBLESTONE) : stack;
        ItemStackRenderState state = new ItemStackRenderState();
        mc.getItemModelResolver().updateForTopItem(state, target, ItemDisplayContext.GROUND, world, null, 0);
        Material.Baked material = state.pickParticleMaterial(random);
        if (material != null) return material.sprite();
        return mc.getAtlasManager().getAtlasOrThrow(AtlasIds.ITEMS).missingSprite();
    }

    public static ParticleProvider<GooExplodeParticleData> FACTORY =
            (data, world, x, y, z, xAux, yAux, zAux, random) -> {
                TextureAtlasSprite sprite = resolveSprite(world, data.getItemStack(), random);
                return new GooExplodeParticle(world, x, y, z, sprite);
            };
}
