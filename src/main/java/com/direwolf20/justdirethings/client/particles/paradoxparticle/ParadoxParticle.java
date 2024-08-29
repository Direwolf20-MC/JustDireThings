package com.direwolf20.justdirethings.client.particles.paradoxparticle;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.Random;

public class ParadoxParticle extends BreakingItemParticle {

    private double targetX, targetY, targetZ;
    Random random = new Random();
    private double initialRadius;
    private double angularVelocity;
    private double inwardSpeed;
    private double currentAngle;
    private double currentRadius;
    private double gravitationalPull;  // Controls the speed of movement

    public ParadoxParticle(ClientLevel world, double x, double y, double z, double targetX, double targetY, double targetZ, ItemStack itemStack, int gravitationalPull) {
        super(world, x, y, z, itemStack);

        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.gravitationalPull = gravitationalPull;  // Properly assign gravitationalPull

        // Calculate initial radius
        this.initialRadius = Math.sqrt(Math.pow(targetX - x, 2) + Math.pow(targetY - y, 2) + Math.pow(targetZ - z, 2));
        this.currentRadius = initialRadius;

        // Set the initial angular velocity and angle
        this.angularVelocity = 0;
        this.currentAngle = 0;

        // Particle settings
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.gravity = 0.0f;
        this.hasPhysics = false;

        // Determine lifetime based on gravitational pull (lower pull = longer lifetime)
        this.lifetime = (int) (initialRadius * 20 / gravitationalPull); // Adjust lifetime based on distance and pull

        if (this.sprite == null) {
            this.setSprite(Minecraft.getInstance().getItemRenderer().getModel(new ItemStack(Blocks.COBBLESTONE), world, (LivingEntity) null, 0).getParticleIcon());
        }

        this.scale(0.2f);
        this.lifetime = 600;
    }

    public ParadoxParticle(ClientLevel world, double x, double y, double z, ItemStack itemStack) {
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
            // Stop movement when the particle gets very close to the center
            double stopThreshold = 0.01;  // Distance threshold to stop the particle
            if (currentRadius > stopThreshold) {
                // Decrease the radius over time
                currentRadius -= gravitationalPull * 0.015;  // Adjust the rate of inward movement based on gravitational pull

                // Increase angular velocity as it spirals inward
                angularVelocity += gravitationalPull * 0.00005;  // Adjust the rate of rotation increase

                // Cap the angular velocity to prevent it from getting too high
                double maxAngularVelocity = 0.00075;  // Define a maximum angular velocity
                if (angularVelocity > maxAngularVelocity) {
                    angularVelocity = maxAngularVelocity;
                }

                // Update the current angle for this tick
                currentAngle += angularVelocity;

                // Calculate the current direction vector from the particle to the center of the black hole
                double dx = this.x - targetX;
                double dz = this.z - targetZ;

                // Calculate the current distance to the center of the black hole
                double distanceToCenter = Math.sqrt(dx * dx + dz * dz);

                // Normalize the direction vector to get the unit direction
                double directionX = dx / distanceToCenter;
                double directionZ = dz / distanceToCenter;

                // Calculate the new X and Z positions based on the current radius and angle
                this.x = targetX + currentRadius * (Math.cos(currentAngle) * directionX - Math.sin(currentAngle) * directionZ);
                this.z = targetZ + currentRadius * (Math.sin(currentAngle) * directionX + Math.cos(currentAngle) * directionZ);

                // Adjust the Y position gradually towards the target
                double deltaY = (targetY - this.y);
                double smoothingFactor = 0.008;
                double minYd = 0.0075;  // Minimum allowed yd value to prevent it from getting too small

                if (Math.abs(deltaY) > 0.01) {
                    this.yd = deltaY * smoothingFactor;
                    if (Math.abs(this.yd) < minYd) {
                        this.yd = Math.copySign(minYd, this.yd);  // Ensure yd doesn't get too small while maintaining direction
                    }
                    this.y += this.yd;
                } else {
                    this.y = targetY;  // Snap to the target Y-level once close enough
                }

                // Apply the updated position
                this.setPos(this.x, this.y, this.z);
            } else {
                // Stop movement when the particle is within the stopThreshold
                this.xd = 0;
                this.yd = 0;
                this.zd = 0;
                this.angularVelocity = 0;  // Stop any further rotation
            }
        }
    }


    public static ParticleProvider<ParadoxParticleData> FACTORY =
            (data, world, x, y, z, xSpeed, ySpeed, zSpeed) ->
                    new ParadoxParticle(world, x, y, z, data.targetX, data.targetY, data.targetZ, data.getItemStack(), data.ticksPerBlock);
}

