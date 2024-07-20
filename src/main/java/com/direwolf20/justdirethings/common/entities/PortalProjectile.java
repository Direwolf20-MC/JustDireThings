package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.NBTHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.direwolf20.justdirethings.util.MiscHelpers.getPrimaryDirection;

public class PortalProjectile extends Projectile {
    private UUID portalGunUUID;
    private boolean isPrimaryType;
    private boolean isAdvanced;
    private NBTHelpers.PortalDestination portalDestination;
    private boolean hasSpawnedPortal = false;
    public PortalProjectile(EntityType<? extends Projectile> entityType, Level world) {
        super(entityType, world);
    }

    public PortalProjectile(Level world, Player player, UUID portalGunUUID, boolean isPrimaryType, boolean isAdvanced) {
        this(Registration.PortalProjectile.get(), world);
        setOwner(player);
        setPos(player.getEyePosition());
        this.portalGunUUID = portalGunUUID;
        this.isPrimaryType = isPrimaryType;
        this.isAdvanced = isAdvanced;
    }

    public PortalProjectile(Level world, Player player, UUID portalGunUUID, boolean isPrimaryType, boolean isAdvanced, NBTHelpers.PortalDestination portalDestination) {
        this(Registration.PortalProjectile.get(), world);
        setOwner(player);
        setPos(player.getEyePosition());
        this.portalGunUUID = portalGunUUID;
        this.isPrimaryType = isPrimaryType;
        this.isAdvanced = isAdvanced;
        this.portalDestination = portalDestination;
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
        if (isAdvanced) {
            if (tickCount > 5) {
                BlockPos blockPos = this.blockPosition();
                if (!level().getBlockState(blockPos).isAir() || !level().getBlockState(blockPos.below()).isAir())
                    blockPos = blockPos.relative(getPrimaryDirection(vec3).getOpposite());
                Vec3 hitPos = Vec3.atCenterOf(blockPos); // Slightly offset to avoid z-fighting
                spawnAdvancedPortal(hitPos.x(), hitPos.y(), hitPos.z(), getPrimaryDirection(vec3).getOpposite(), blockPos);
            }
        } else {
            if (tickCount > 200)
                this.discard();
        }
    }


    @Override
    protected void onHitBlock(BlockHitResult result) {
        Vec3 hitPos = Vec3.atCenterOf(result.getBlockPos()).relative(result.getDirection(), 0.501); // Slightly offset to avoid z-fighting
        if (isAdvanced)
            spawnAdvancedPortal(hitPos.x(), hitPos.y(), hitPos.z(), result.getDirection(), result.getBlockPos());
        else
            spawnPortal(hitPos.x(), hitPos.y(), hitPos.z(), result.getDirection(), result.getBlockPos());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    protected List<? extends PortalEntity> findMatchingPortal(MinecraftServer server, boolean isPrimaryType) {
        List<PortalEntity> returnList = new ArrayList<>();
        for (ServerLevel serverLevel : server.getAllLevels()) {
            List<? extends PortalEntity> customEntities = serverLevel.getEntities(Registration.PortalEntity.get(), k -> k.getPortalGunUUID().equals(portalGunUUID) && k.getIsPrimary() == isPrimaryType);
            if (!customEntities.isEmpty())
                returnList.addAll(customEntities);
        }
        return returnList;
    }

    public void closeMyPortals(MinecraftServer server) {
        for (ServerLevel serverLevel : server.getAllLevels()) {
            List<? extends PortalEntity> customEntities = serverLevel.getEntities(Registration.PortalEntity.get(), k -> k.getPortalGunUUID().equals(portalGunUUID));

            for (PortalEntity entity : customEntities) {
                entity.setDying();
            }
        }
    }

    protected void clearMatchingPortal(MinecraftServer server) {
        List<? extends PortalEntity> matchingPortals = findMatchingPortal(server, isPrimaryType);
        for (PortalEntity matchingPortal : matchingPortals) {
            if (matchingPortal != null)
                matchingPortal.setDying();
        }
    }

    protected void linkPortals(MinecraftServer server, PortalEntity portal) {
        List<? extends PortalEntity> matchingPortals = findMatchingPortal(server, !isPrimaryType);
        if (!matchingPortals.isEmpty()) {
            PortalEntity matchingPortal = matchingPortals.getFirst();
            if (matchingPortal != null) {
                portal.setLinkedPortal(matchingPortal);
                matchingPortal.setLinkedPortal(portal);
            }
        }
    }

    protected void linkPortals(PortalEntity source, PortalEntity destination) {
        source.setLinkedPortal(destination);
        destination.setLinkedPortal(source);
    }

    protected void spawnAdvancedPortal(double x, double y, double z, Direction direction, BlockPos hitPos) {
        if (hasSpawnedPortal) return;
        Level level = this.level();
        MinecraftServer server = level.getServer();
        if (server == null) return;
        if (getOwner() == null) return;
        if (!level.isClientSide) {
            PortalEntity source = new PortalEntity(level, direction, getPortalAlignment(getDeltaMovement()), portalGunUUID, isPrimaryType, true, getOwner().getUUID());
            if (direction.getAxis() != Direction.Axis.Y) {
                y = y - 1.5;
                BlockState belowState = level.getBlockState(hitPos.relative(direction).below());
                if (!belowState.isAir()) {
                    y = y + 1;
                    BlockState aboveState = level.getBlockState(hitPos.relative(direction).above());
                    if (!aboveState.isAir()) {
                        this.discard();
                        return;
                    }
                }
            } else {
                if (getPortalAlignment(getDeltaMovement()) == Direction.Axis.X)
                    x = x - 0.5;
                else
                    z = z - 0.5;
            }
            source.setPos(x, y, z);
            source.refreshDimensions();
            AABB newBoundingBox = source.getBoundingBox();
            List<PortalEntity> existingPortals = level.getEntitiesOfClass(PortalEntity.class, newBoundingBox.inflate(-0.1));

            ServerLevel boundLevel = server.getLevel(portalDestination.globalVec3().dimension());
            if (boundLevel == null) return;
            PortalEntity destination = new PortalEntity(boundLevel, portalDestination.direction(), portalDestination.direction().getAxis(), portalGunUUID, !isPrimaryType, true, getOwner().getUUID());
            destination.setPos(portalDestination.globalVec3().position());
            destination.refreshDimensions();
            AABB destinationBoundingBox = destination.getBoundingBox();
            List<PortalEntity> existingPortals2 = boundLevel.getEntitiesOfClass(PortalEntity.class, destinationBoundingBox.inflate(-0.1));

            boolean overlaps = existingPortals.stream().anyMatch(existingPortal -> existingPortal.getBoundingBox().intersects(newBoundingBox) && !existingPortal.getPortalGunUUID().equals(source.getPortalGunUUID())) ||
                    existingPortals2.stream().anyMatch(existingPortal2 -> existingPortal2.getBoundingBox().intersects(destinationBoundingBox) && !existingPortal2.getPortalGunUUID().equals(destination.getPortalGunUUID()));
            if (!overlaps) {
                closeMyPortals(server);
                level.addFreshEntity(source);
                boundLevel.addFreshEntity(destination);
                linkPortals(source, destination);
                hasSpawnedPortal = true;
            }
            this.discard();
        }
    }

    protected void spawnPortal(double x, double y, double z, Direction direction, BlockPos hitPos) {
        Level level = this.level();
        MinecraftServer server = level.getServer();
        if (server == null) return;
        if (getOwner() == null) return;
        if (!level.isClientSide) {
            PortalEntity portal = new PortalEntity(level, direction, getPortalAlignment(getDeltaMovement()), portalGunUUID, isPrimaryType, false, getOwner().getUUID());
            if (direction.getAxis() != Direction.Axis.Y) {
                y = y - 1.5;
                BlockState belowState = level.getBlockState(hitPos.relative(direction).below());
                if (!belowState.isAir()) {
                    y = y + 1;
                    BlockState aboveState = level.getBlockState(hitPos.relative(direction).above());
                    if (!aboveState.isAir()) {
                        this.discard();
                        return;
                    }
                }
            } else {
                if (getPortalAlignment(getDeltaMovement()) == Direction.Axis.X)
                    x = x - 0.5;
                else
                    z = z - 0.5;
            }
            portal.setPos(x, y, z);
            portal.refreshDimensions();
            AABB newBoundingBox = portal.getBoundingBox();
            List<PortalEntity> existingPortals = level.getEntitiesOfClass(PortalEntity.class, newBoundingBox.inflate(-0.1));

            boolean overlaps = existingPortals.stream().anyMatch(existingPortal -> existingPortal.getBoundingBox().intersects(newBoundingBox));
            if (!overlaps) {
                clearMatchingPortal(server);
                level.addFreshEntity(portal);
                linkPortals(server, portal);
            }
            this.discard();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("portalGunUUID"))
            portalGunUUID = compound.getUUID("portalGunUUID");
        if (compound.contains("portalDestination"))
            portalDestination = NBTHelpers.PortalDestination.fromNBT(compound.getCompound("portalDestination"));
        isPrimaryType = compound.getBoolean("isPrimaryType");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putBoolean("isPrimaryType", isPrimaryType);
        if (portalGunUUID != null)
            compound.putUUID("portalGunUUID", portalGunUUID);
        if (portalDestination != null)
            compound.put("portalDestination", NBTHelpers.PortalDestination.toNBT(portalDestination));
    }

    private static Direction.Axis getPortalAlignment(Vec3 velocity) {
        double absX = Math.abs(velocity.x);
        double absZ = Math.abs(velocity.z);
        return absX > absZ ? Direction.Axis.X : Direction.Axis.Z;
    }
}
