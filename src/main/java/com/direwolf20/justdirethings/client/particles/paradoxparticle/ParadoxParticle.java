package com.direwolf20.justdirethings.client.particles.paradoxparticle;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;

public class ParadoxParticle extends BreakingItemParticle {

    private double targetX, targetY, targetZ;
    Random random = new Random();
    private double initialRadius;
    private double angularVelocity;
    private double inwardSpeed;
    private double currentAngle;
    private double currentRadius;
    private double gravitationalPull;  // Controls the speed of movement
    private UUID paradox_uuid;
    private boolean dying = false;

    public ParadoxParticle(ClientLevel world, double x, double y, double z, double targetX, double targetY, double targetZ, ItemStack itemStack, int gravitationalPull, UUID paradox_uuid) {
        super(world, x, y, z, itemStack);

        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.gravitationalPull = gravitationalPull;  // Properly assign gravitationalPull
        this.paradox_uuid = paradox_uuid;

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
            this.setSprite(Minecraft.getInstance().getItemRenderer().getModel(new ItemStack(Blocks.COBBLESTONE), world, null, 0).getParticleIcon());
        }

        this.scale(0.5f);
        this.lifetime = 600;
    }

    public ParadoxParticle(ClientLevel world, double x, double y, double z, ItemStack itemStack) {
        super(world, x, y, z, itemStack);
    }

    @Nullable
    private Entity findEntityByUUID(ClientLevel world, UUID uuid) {
        for (Entity entity : world.entitiesForRendering()) { // Iterating through all entities
            if (entity.getUUID().equals(uuid)) {
                return entity;
            }
        }
        return null; // Return null if no matching entity is found
    }

    public void deathMovement() {
        if (!this.onGround) {
            this.xd *= 0.5; // Reduce horizontal speed to simulate "falling"
            this.zd *= 0.5;
            this.yd -= this.gravity;
            // Update the position with the new physics-applied motion
            this.move(this.xd, this.yd, this.zd);
        }
        if (this.age++ >= this.lifetime)
            this.remove();
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        Entity entity = findEntityByUUID(level, paradox_uuid);
        if (entity == null || dying) {
            if (!dying) {
                this.gravity = 0.04f; // Adjust gravity as needed
                this.hasPhysics = true;
                // Randomize the remaining lifetime after it hits the ground
                this.lifetime = this.age + 20 + random.nextInt(41); // 20-60 ticks
                this.dying = true;
            }
            deathMovement();
            return;
        }

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            // Calculate current distance to the black hole in all three dimensions
            double dx = this.x - targetX;
            double dy = this.y - targetY;
            double dz = this.z - targetZ;
            double distanceToCenter = Math.sqrt(dx * dx + dy * dy + dz * dz);

            // Stop movement when the particle gets very close to the center
            double stopThreshold = 0.01;  // Distance threshold to stop the particle
            double minDistance = 0.005;   // Minimum allowed distance from the center to prevent overshooting
            double fixedTransitionDistance = 2.0; // Fixed distance from the black hole to start orbiting

            if (distanceToCenter > stopThreshold) {
                double baseGravitationalPull;

                if (distanceToCenter > fixedTransitionDistance) {
                    // Increase the gravitational pull significantly to bring the particle closer quickly
                    baseGravitationalPull = 30 * distanceToCenter; // Faster pull for close particles
                } else {
                    // Reduce the gravitational pull for more stable orbiting
                    baseGravitationalPull = 12;
                }

                // Gravitational pull increases as distance decreases, with a damping factor
                double proximityFactor = 1 / (distanceToCenter + 2.5);  // Adjusted for stronger pull at closer distances
                double adjustedGravitationalPull = gravitationalPull * proximityFactor * baseGravitationalPull;

                // Cap the gravitational pull to prevent it from becoming too extreme
                /*double maxGravitationalPull = 20; // Higher cap to allow stronger pull
                if (adjustedGravitationalPull > maxGravitationalPull) {
                    adjustedGravitationalPull = maxGravitationalPull;
                }*/

                // Decrease the radius over time, but not beyond the minimum distance
                currentRadius -= adjustedGravitationalPull * 0.04; // Increase inward speed for closer distances
                if (currentRadius < minDistance) {
                    currentRadius = minDistance;  // Prevent getting too close to the center
                }

                // Angular velocity increases with gravitational pull and proximity, but apply a damping factor
                angularVelocity += adjustedGravitationalPull * 0.0004; // Faster angular velocity change

                // Cap the angular velocity to prevent it from getting too high
                double maxAngularVelocity = 0.04;
                if (angularVelocity > maxAngularVelocity) {
                    angularVelocity = maxAngularVelocity;
                }

                // Update the current angle for this tick
                currentAngle += angularVelocity;

                // Normalize the direction vector to get the unit direction
                double directionX = dx / distanceToCenter;
                double directionY = dy / distanceToCenter;
                double directionZ = dz / distanceToCenter;

                // Calculate the new positions for X, Y, and Z based on current radius, angle, and direction
                this.x = targetX + currentRadius * (Math.cos(currentAngle) * directionX - Math.sin(currentAngle) * directionZ);
                this.z = targetZ + currentRadius * (Math.sin(currentAngle) * directionX + Math.cos(currentAngle) * directionZ);

                // Adjust the Y position proportionally to the gravitational pull and direction
                this.y = targetY + currentRadius * directionY * 0.975;  // Keep Y movement aligned with radial distance

                // Apply the updated position
                this.setPos(this.x, this.y, this.z);
            } else {
                // Stop movement when the particle is within the stopThreshold
                this.remove();
            }
        }
    }


    public static ParticleProvider<ParadoxParticleData> FACTORY =
            (data, world, x, y, z, xSpeed, ySpeed, zSpeed) ->
                    new ParadoxParticle(world, x, y, z, data.targetX, data.targetY, data.targetZ, data.getItemStack(), data.ticksPerBlock, data.paradox_uuid);
}

