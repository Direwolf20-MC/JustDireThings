package com.direwolf20.justdirethings.client.particles.itemparticle;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.AtlasIds;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class ItemFlowParticle extends BreakingItemParticle {

    private double targetX, targetY, targetZ;
    Random random = new Random();

    public ItemFlowParticle(ClientLevel world, double x, double y, double z, double targetX, double targetY, double targetZ, TextureAtlasSprite sprite, int ticksPerBlock) {
        this(world, x, y, z, sprite);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        Vec3 target = new Vec3(targetX, targetY, targetZ);
        Vec3 source = new Vec3(this.x, this.y, this.z);
        Vec3 path = target.subtract(source).normalize().multiply(1, 1, 1);
        this.gravity = 0.0f;
        double distance = target.distanceTo(source);
        this.hasPhysics = false;
        float minSize = 0.1f;
        float maxSize = 0.2f;
        float partSize = minSize + random.nextFloat() * (maxSize - minSize);
        float speedModifier = (1f - 0.5f) * (partSize - minSize) / (maxSize - minSize) + 0.25f;
        float speedAdjust = ticksPerBlock * (1 / speedModifier);
        this.xd += path.x / speedAdjust;
        this.yd += path.y / speedAdjust;
        this.zd += path.z / speedAdjust;
        this.lifetime = (int) (distance * speedAdjust);
        this.scale(partSize);
    }

    public ItemFlowParticle(ClientLevel world, double x, double y, double z, TextureAtlasSprite sprite) {
        super(world, x, y, z, sprite);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.yd -= 0.04D * (double) this.gravity;
            this.move(this.xd, this.yd, this.zd);
        }
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

    public static ParticleProvider<ItemFlowParticleData> FACTORY =
            (data, world, x, y, z, xAux, yAux, zAux, random) -> {
                TextureAtlasSprite sprite = resolveSprite(world, data.getItemStack(), random);
                return new ItemFlowParticle(world, x, y, z, data.targetX, data.targetY, data.targetZ, sprite, data.ticksPerBlock);
            };
}
