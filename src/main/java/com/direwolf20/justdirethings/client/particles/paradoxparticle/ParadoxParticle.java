package com.direwolf20.justdirethings.client.particles.paradoxparticle;


import com.direwolf20.justdirethings.common.entities.ParadoxEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.data.AtlasIds;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;

public class ParadoxParticle extends BreakingItemParticle {

    private double targetX, targetY, targetZ;
    private static Random random = new Random();
    private double initialRadius;
    private double angularVelocity;
    private double currentAngle;
    private double currentRadius;
    private double gravitationalPull;  // Controls the speed of movement
    private UUID paradox_uuid;
    private boolean dying = false;
    private final BlockPos sourcePos;

    public ParadoxParticle(ClientLevel world, double x, double y, double z, double targetX, double targetY, double targetZ, TextureAtlasSprite sprite, int gravitationalPull, UUID paradox_uuid) {
        super(world, x, y, z, sprite);

        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.gravitationalPull = gravitationalPull;
        this.paradox_uuid = paradox_uuid;

        this.initialRadius = Math.sqrt(Math.pow(targetX - x, 2) + Math.pow(targetY - y, 2) + Math.pow(targetZ - z, 2));
        this.currentRadius = initialRadius;

        this.angularVelocity = 0;
        this.currentAngle = 0;

        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.gravity = 0.0f;
        this.hasPhysics = false;

        this.lifetime = (int) (initialRadius * 20 / gravitationalPull);

        this.sourcePos = new BlockPos((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
        BlockState blockState = level.getBlockState(sourcePos);
        if (blockState.getBlock() instanceof LiquidBlock liquidBlock) {
            FluidState fluidState = liquidBlock.fluid.defaultFluidState();
            FluidModel fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidState);
            TextureAtlasSprite fluidSprite = fluidModel.stillMaterial().sprite();
            this.setSprite(fluidSprite);
            FluidTintSource tintSource = fluidModel.fluidTintSource();
            int tint = tintSource != null
                    ? tintSource.colorInWorld(fluidState, blockState, (net.minecraft.client.renderer.block.BlockAndTintGetter) level, sourcePos)
                    : -1;
            this.rCol *= (float) (tint >> 16 & 255) / 255.0F;
            this.gCol *= (float) (tint >> 8 & 255) / 255.0F;
            this.bCol *= (float) (tint & 255) / 255.0F;
        }

        this.scale(0.5f);
        this.lifetime = 600;
    }

    public ParadoxParticle(ClientLevel world, double x, double y, double z, TextureAtlasSprite sprite) {
        super(world, x, y, z, sprite);
        this.sourcePos = new BlockPos((int) x, (int) y, (int) z);
    }

    @Nullable
    private ParadoxEntity findEntityByUUID(ClientLevel world, UUID uuid) {
        for (Entity entity : world.entitiesForRendering()) {
            if (entity.getUUID().equals(uuid) && entity instanceof ParadoxEntity paradoxEntity) {
                return paradoxEntity;
            }
        }
        return null;
    }

    public void deathMovement() {
        if (!this.onGround) {
            this.xd *= 0.5;
            this.zd *= 0.5;
            this.yd -= this.gravity;
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

        ParadoxEntity entity = findEntityByUUID(level, paradox_uuid);
        if (entity == null || entity.getShrinkScale() < 1 || !entity.isBlockWithinRadius(sourcePos) || dying) {
            if (!dying) {
                this.gravity = 0.04f;
                this.hasPhysics = true;
                this.lifetime = this.age + 20 + random.nextInt(41);
                this.dying = true;
            }
            deathMovement();
            return;
        }

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            double dx = this.x - targetX;
            double dy = this.y - targetY;
            double dz = this.z - targetZ;
            double distanceToCenter = Math.sqrt(dx * dx + dy * dy + dz * dz);

            double stopThreshold = 0.01;
            double minDistance = 0.005;
            double fixedTransitionDistance = 2.0;

            if (distanceToCenter > stopThreshold) {
                double baseGravitationalPull;

                if (distanceToCenter > fixedTransitionDistance) {
                    baseGravitationalPull = 30 * distanceToCenter;
                } else {
                    baseGravitationalPull = 12;
                }

                double proximityFactor = 1 / (distanceToCenter + 2.5);
                double adjustedGravitationalPull = gravitationalPull * proximityFactor * baseGravitationalPull;

                currentRadius -= adjustedGravitationalPull * 0.04;
                if (currentRadius < minDistance) {
                    currentRadius = minDistance;
                }

                angularVelocity += adjustedGravitationalPull * 0.0004;

                double maxAngularVelocity = 0.04;
                if (angularVelocity > maxAngularVelocity) {
                    angularVelocity = maxAngularVelocity;
                }

                currentAngle += angularVelocity;

                double directionX = dx / distanceToCenter;
                double directionY = dy / distanceToCenter;
                double directionZ = dz / distanceToCenter;

                this.x = targetX + currentRadius * (Math.cos(currentAngle) * directionX - Math.sin(currentAngle) * directionZ);
                this.z = targetZ + currentRadius * (Math.sin(currentAngle) * directionX + Math.cos(currentAngle) * directionZ);

                this.y = targetY + currentRadius * directionY * 0.975;

                this.setPos(this.x, this.y, this.z);
            } else {
                this.remove();
            }
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

    public static ParticleProvider<ParadoxParticleData> FACTORY =
            (data, world, x, y, z, xAux, yAux, zAux, random) -> {
                TextureAtlasSprite sprite = resolveSprite(world, data.getItemStack(), random);
                return new ParadoxParticle(world, x, y, z, data.targetX, data.targetY, data.targetZ, sprite, data.ticksPerBlock, data.paradox_uuid);
            };
}
