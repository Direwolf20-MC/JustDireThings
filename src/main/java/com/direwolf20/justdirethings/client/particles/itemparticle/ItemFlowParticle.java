package com.direwolf20.justdirethings.client.particles.itemparticle;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class ItemFlowParticle extends BreakingItemParticle {

    private double targetX, targetY, targetZ;
    Random random = new Random();

    public ItemFlowParticle(ClientLevel world, double x, double y, double z, double targetX, double targetY, double targetZ, ItemStack itemStack, int ticksPerBlock) {
        this(world, x, y, z, itemStack);
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
        //System.out.println(source +":"+target);
        this.hasPhysics = false;
        float minSize = 0.1f;
        float maxSize = 0.2f;
        float partSize = minSize + random.nextFloat() * (maxSize - minSize);
        float speedModifier = (1f - 0.5f) * (partSize - minSize) / (maxSize - minSize) + 0.25f;
        //float speedModifier = (0.5f - 1f) * (partSize - maxSize) / (minSize - maxSize) + 1f;
        float speedAdjust = ticksPerBlock * (1 / speedModifier);
        this.xd += path.x / speedAdjust;
        this.yd += path.y / speedAdjust;
        this.zd += path.z / speedAdjust;
        this.lifetime = (int) (distance * speedAdjust);
        this.scale(partSize);
        if (this.sprite == null) {
            this.setSprite(Minecraft.getInstance().getItemRenderer().getModel(new ItemStack(Blocks.COBBLESTONE), world, (LivingEntity) null, 0).getParticleIcon());
        }

    }

    public ItemFlowParticle(ClientLevel world, double x, double y, double z, ItemStack itemStack) {
        super(world, x, y, z, itemStack);
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

    public static ParticleProvider<ItemFlowParticleData> FACTORY =
            (data, world, x, y, z, xSpeed, ySpeed, zSpeed) ->
                    new ItemFlowParticle(world, x, y, z, data.targetX, data.targetY, data.targetZ, data.getItemStack(), data.ticksPerBlock);
}

