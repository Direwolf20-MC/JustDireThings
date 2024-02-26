package com.direwolf20.justdirethings.client.particles.itemparticle;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.Random;

public class ItemFlowParticle extends BreakingItemParticle {

    private double targetX, targetY, targetZ;
    Random random = new Random();
    private float partSize;
    private boolean doGravity;
    private boolean shrinking;

    public ItemFlowParticle(ClientLevel world, double x, double y, double z, ItemStack itemStack, boolean gravity, boolean shrinking) {
        super(world, x, y, z, itemStack);
        this.doGravity = gravity;
        this.shrinking = shrinking;
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
        //this.xd = 0;
        //this.yd = 0;
        //this.zd = 0;
        /*if (shrinking) {
            this.targetX = x + 1.75f;
            this.targetY = y + 1.75f;
            this.targetZ = z + 1.75f;
        } else {
            if (!gravity) {
                double randomX = random.nextFloat();
                double randomY = random.nextFloat();
                double randomZ = random.nextFloat();
                this.xo = x + randomX;
                this.yo = y + randomY;
                this.zo = z + randomZ;
                this.setPos(xo, yo, zo);
                this.targetX = x;
                this.targetY = y;
                this.targetZ = z;
            }
        }*/

        /*Vec3 target = new Vec3(targetX, targetY, targetZ);
        Vec3 source = new Vec3(this.x, this.y, this.z);
        Vec3 path = target.subtract(source).normalize().multiply(1, 1, 1);
        float speedModifier = (1f - 0.5f) * (partSize - minSize) / (maxSize - minSize) + 0.25f;
        int ticksPerBlock = 15;
        float speedAdjust = ticksPerBlock * (1 / speedModifier);
        this.xd += path.x / speedAdjust;
        this.yd += path.y / speedAdjust;
        this.zd += path.z / speedAdjust;

        if (gravity) {
            this.xd = 0;
            this.yd = 0;
            this.zd = 0;
            this.gravity = 0.0625f;
            this.hasPhysics = true;
            this.age = this.lifetime / 2;
            this.scale(2f);
            this.partSize = quadSize;
            updateColorAndGravity();
        } else {
            this.gravity = 0.0f;
            this.hasPhysics = false;
        }

        if (!shrinking)
            updateColorAndGravity();*/
    }

    @Override
    public void tick() {
        super.tick();
        /*this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.yd -= 0.04D * (double) this.gravity;
            this.move(this.xd, this.yd, this.zd);
        }
        if (!shrinking && this.y <= targetY)
            this.remove();*/
        updateColorAndGravity();
    }

    public void updateColorAndGravity() {
        float relativeAge = (float) ((this.lifetime - this.age)) / this.lifetime; //1.0 -> 0.0
        //float shrink = Mth.lerp(relativeAge, 0.1f, 1);
        //this.quadSize = partSize * shrink;

        float adjustedAge = (float) Math.pow(relativeAge, 2);
        /*float darkness;
        if (shrinking)
            darkness = Mth.lerp(adjustedAge, 0, 1);
        else
            darkness = Mth.lerp(adjustedAge, 1, 0.15f);

        this.rCol = darkness;
        this.gCol = darkness;
        this.bCol = darkness;*/

        if (relativeAge < 0.5f) {
            adjustedAge = (float) Math.pow(relativeAge / 0.5f, 2);
            //if (shrinking)
            this.alpha = Mth.lerp(adjustedAge, 0.25f, 1);
            //else
            //    this.alpha = Mth.lerp(adjustedAge, 1f, 0.2f);
        }
        /*if (!doGravity) {
            int gravityChance = random.nextInt(2);
            if (relativeAge < 0.75f && gravityChance == 0) {
                this.gravity = 0.05f;
            }
        }*/
    }

    @Override //Performance Reasons
    protected int getLightColor(float pPartialTick) {
        return 0xF00080;
    }

    public static ParticleProvider<ItemFlowParticleData> FACTORY =
            (data, world, x, y, z, xSpeed, ySpeed, zSpeed) ->
                    new ItemFlowParticle(world, x, y, z, data.getItemStack(), data.doGravity, data.shrinking);
}

