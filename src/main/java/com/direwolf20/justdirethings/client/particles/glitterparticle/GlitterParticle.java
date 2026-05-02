package com.direwolf20.justdirethings.client.particles.glitterparticle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class GlitterParticle extends SingleQuadParticle {
    private double sourceX;
    private double sourceY;
    private double sourceZ;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int speedModifier;
    private String particleType;
    private Random rand = new Random();
    private int particlePicker;
    protected final SpriteSet spriteSet;

    public GlitterParticle(ClientLevel world, double sourceX, double sourceY, double sourceZ, double targetX, double targetY, double targetZ, double xSpeed, double ySpeed, double zSpeed,
                           float size, float red, float green, float blue, float alpha, boolean collide, float maxAge, String particleType, SpriteSet sprite) {
        super(world, sourceX, sourceY, sourceZ, sprite.get(new Random().nextInt(3) + 1, 4));
        xd = xSpeed;
        yd = ySpeed;
        zd = zSpeed;
        rCol = red;
        gCol = green;
        bCol = blue;
        this.alpha = alpha;
        gravity = 0;
        this.lifetime = Math.round(maxAge);

        setSize(0.001F, 0.001F);

        xo = x;
        yo = y;
        zo = z;
        this.quadSize = size;
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceZ = sourceZ;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.hasPhysics = collide;
        this.particleType = particleType;
        this.setGravity(0f);
        this.spriteSet = sprite;
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }

    // [VanillaCopy] of super, without drag when onGround is true
    @Override
    public void tick() {
        double moveX;
        double moveY;
        double moveZ;

        if (this.age++ >= this.lifetime) {
            this.remove();
        }

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        Vec3 sourcePos = new Vec3(sourceX, sourceY, sourceZ);
        Vec3 targetPos = new Vec3(targetX, targetY, targetZ);

        Vec3 partPos = new Vec3(this.x, this.y, this.z);
        Vec3 targetDirection = new Vec3(targetPos.x() - this.x, targetPos.y() - this.y, targetPos.z() - this.z);

        double totalDistance = targetPos.distanceTo(partPos);
        if (totalDistance < 0.1)
            this.remove();

        double speedAdjust = 20;

        moveX = (targetX - this.x) / speedAdjust;
        moveY = (targetY - this.y) / speedAdjust;
        moveZ = (targetZ - this.z) / speedAdjust;

        BlockPos nextPos = new BlockPos((int) this.x + (int) moveX, (int) this.y + (int) moveY, (int) this.z + (int) moveZ);

        if (age > 40)
            this.hasPhysics = false;
        this.move(moveX, moveY, moveZ);
    }

    public void setGravity(float value) {
        gravity = value;
    }

    public void setSpeed(float mx, float my, float mz) {
        xd = mx;
        yd = my;
        zd = mz;
    }

}
