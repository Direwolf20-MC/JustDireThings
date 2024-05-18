package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PortalProjectile extends Projectile {
    public PortalProjectile(EntityType<? extends Projectile> entityType, Level world) {
        super(entityType, world);
    }

    public PortalProjectile(Level world, Player player) {
        this(Registration.PortalProjectile.get(), world);
        setOwner(player);
        setPos(player.getEyePosition());
    }

    @Override
    public void tick() {
        super.tick();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity, ClipContext.Block.COLLIDER);
        if (hitresult.getType() != HitResult.Type.MISS && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult)) {
            this.hitTargetOrDeflectSelf(hitresult);
        }
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.setPos(d0, d1, d2);
        //System.out.println("I'm here: " + this.tickCount);
        if (tickCount > 60)
            spawnPortal(this.getX(), this.getY(), this.getZ(), getPrimaryDirection(vec3));
    }


    @Override
    protected void onHitBlock(BlockHitResult result) {
        Vec3 hitPos = Vec3.atCenterOf(result.getBlockPos()).relative(result.getDirection(), 0.501); // Slightly offset to avoid z-fighting
        spawnPortal(hitPos.x(), hitPos.y(), hitPos.z(), result.getDirection());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    protected void spawnPortal(double x, double y, double z, Direction direction) {
        Level level = this.level();
        if (!level.isClientSide) {
            PortalEntity portal = new PortalEntity(level, (Player) getOwner(), direction, getPortalAlignment(getDeltaMovement()));
            portal.setPos(x, y, z);
            portal.refreshDimensions();
            AABB newBoundingBox = portal.getBoundingBox();
            List<PortalEntity> existingPortals = level.getEntitiesOfClass(PortalEntity.class, newBoundingBox.inflate(-0.1));

            boolean overlaps = existingPortals.stream().anyMatch(existingPortal -> existingPortal.getBoundingBox().intersects(newBoundingBox));
            if (!overlaps) {
                level.addFreshEntity(portal);
            }
            this.discard();
        }
    }

    public static Direction getPrimaryDirection(Vec3 vec) {
        double absX = Math.abs(vec.x);
        double absY = Math.abs(vec.y);
        double absZ = Math.abs(vec.z);

        // Determine the largest magnitude component
        if (absX > absY && absX > absZ) {
            return vec.x > 0 ? Direction.EAST : Direction.WEST;
        } else if (absY > absX && absY > absZ) {
            return vec.y > 0 ? Direction.UP : Direction.DOWN;
        } else {
            return vec.z > 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }

    private static Direction.Axis getPortalAlignment(Vec3 velocity) {
        double absX = Math.abs(velocity.x);
        double absZ = Math.abs(velocity.z);
        return absX > absZ ? Direction.Axis.X : Direction.Axis.Z;
    }
}
