package com.direwolf20.justdirethings.client.particles.gooexplodeparticle;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.Random;

public class GooExplodeParticle extends BreakingItemParticle {
    Random random = new Random();
    private float partSize;

    public GooExplodeParticle(ClientLevel world, double x, double y, double z, ItemStack itemStack) {
        super(world, x, y, z, itemStack);
        float minSize = 0.25f;
        float maxSize = 0.5f;
        this.partSize = minSize + random.nextFloat() * (maxSize - minSize);
        this.lifetime = 30;
        int longLifeChance = random.nextInt(20);
        if (longLifeChance == 0)
            this.lifetime = 120;

        this.scale(partSize);
        this.partSize = quadSize;
        if (this.sprite == null) {
            this.setSprite(Minecraft.getInstance().getItemRenderer().getModel(new ItemStack(Blocks.COBBLESTONE), world, (LivingEntity) null, 0).getParticleIcon());
        }
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
    protected int getLightColor(float pPartialTick) {
        return 0xF00080;
    }

    public static ParticleProvider<GooExplodeParticleData> FACTORY =
            (data, world, x, y, z, xSpeed, ySpeed, zSpeed) ->
                    new GooExplodeParticle(world, x, y, z, data.getItemStack());
}

