package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.entity.PartEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.*;

public class PortalEntity extends Entity {
    private PortalEntity linkedPortal;
    private UUID portalGunUUID;
    private UUID linkedPortalUUID;
    private boolean isAdvanced;
    private static final int TELEPORT_COOLDOWN = 10; // Cooldown period in ticks (1 second)
    public static int ANIMATION_COOLDOWN = 5;
    public final Map<UUID, Integer> entityCooldowns = new HashMap<>();
    public final Map<UUID, Integer> entityVelocityCooldowns = new HashMap<>();
    public final Map<UUID, Vec3> entityLastPosition = new HashMap<>();
    public final Map<UUID, Vec3> entityLastLastPosition = new HashMap<>();
    public int expirationTime = -99;
    public int deathCounter = 0;
    @Nullable
    private UUID ownerUUID;

    private static final EntityDataAccessor<Byte> DIRECTION = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> ALIGNMENT = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ISPRIMARY = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ISDYING = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.BOOLEAN);

    public PortalEntity(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    public PortalEntity(Level world, Direction direction, Direction.Axis axis, UUID portalGunUUID, boolean isPrimary, boolean isAdvanced, UUID owner) {
        this(Registration.PortalEntity.get(), world);
        this.entityData.set(DIRECTION, (byte) direction.ordinal());
        this.portalGunUUID = portalGunUUID;
        this.entityData.set(ALIGNMENT, (byte) axis.ordinal());
        this.entityData.set(ISPRIMARY, isPrimary);
        this.isAdvanced = isAdvanced;
        if (isAdvanced)
            expirationTime = 100;
        setOwner(owner);
    }

    public void setOwner(UUID owner) {
        if (owner != null) {
            this.ownerUUID = owner;
        }
    }

    public UUID getOwner() {
        return ownerUUID;
    }

    public UUID getPortalGunUUID() {
        return portalGunUUID;
    }

    @Override
    public void playerTouch(Player pPlayer) {
        pPlayer.resetFallDistance();
    }

    @Override
    public void tick() {
        super.tick();
        refreshDimensions();
        if (!level().isClientSide) {
            tickCooldowns();
            if (getLinkedPortal() != null) {
                teleportCollidingEntities();
                captureVelocity();
            }
        }
        tickDying();
    }

    public void tickCooldowns() {
        // Update cooldowns
        entityVelocityCooldowns.entrySet().removeIf(entry -> {
            if (entry.getValue() <= 0) {
                entityLastPosition.remove(entry.getKey());
                entityLastLastPosition.remove(entry.getKey());
                return true;
            }
            entry.setValue(entry.getValue() - 1);
            return false;
        });
        entityCooldowns.entrySet().removeIf(entry -> {
            if (entry.getValue() <= 0) {
                return true;
            }
            entry.setValue(entry.getValue() - 1);
            return false;
        });
        if (isAdvanced && expirationTime > 0) {
            expirationTime = expirationTime - 1;
            if (expirationTime == 0) {
                if (getLinkedPortal() != null)
                    getLinkedPortal().setDying();
                setDying();
            }
        }

    }

    public void tickDying() {
        if (isDying()) {
            deathCounter++;
            if (deathCounter > ANIMATION_COOLDOWN)
                this.remove(RemovalReason.DISCARDED);
        }
    }

    public void captureVelocity() {
        AABB boundingBox = getVelocityBoundingBox();
        List<Entity> entities = level().getEntities(this, boundingBox);
        for (Entity entity : entities) {
            UUID entityUUID = entity.getUUID();
            if (entity != this && isValidEntity(entity)) {
                Vec3 currentPos = entity.position();
                if (entityLastPosition.containsKey(entityUUID))
                    entityLastLastPosition.put(entityUUID, entityLastPosition.get(entityUUID));
                entityLastPosition.put(entityUUID, currentPos);
                entityVelocityCooldowns.put(entityUUID, 10);
            }
        }
    }

    public AABB getVelocityBoundingBox() {
        return this.getBoundingBox().expandTowards(getDirection().getStepX() * 2.5, getDirection().getStepY() * 2.5, getDirection().getStepZ() * 2.5);
    }

    public void teleportCollidingEntities() {
        AABB boundingBox = this.getBoundingBox();
        List<Entity> entities = level().getEntities(this, boundingBox);
        for (Entity entity : entities) {
            if (entity != this && isValidEntity(entity)) {
                if (!level().isClientSide) {
                    teleport(entity);
                }
            }
        }
    }

    public Direction getDirection() {
        return Direction.values()[this.entityData.get(DIRECTION)];
    }

    public Direction.Axis getAlignment() {
        return Direction.Axis.values()[this.entityData.get(ALIGNMENT)];
    }

    public boolean getIsPrimary() {
        return this.entityData.get(ISPRIMARY);
    }

    public int getDeathCounter() {
        return deathCounter;
    }

    public boolean isDying() {
        return this.entityData.get(ISDYING);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DIRECTION, (byte) 0);
        builder.define(ALIGNMENT, (byte) Direction.Axis.Z.ordinal());
        builder.define(ISPRIMARY, false);
        builder.define(ISDYING, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(DIRECTION, compound.getByte("direction"));
        this.entityData.set(ALIGNMENT, compound.getByte("alignment"));
        this.entityData.set(ISPRIMARY, compound.getBoolean("isPrimary"));
        this.entityData.set(ISDYING, compound.getBoolean("isDying"));
        deathCounter = compound.getInt("deathCounter");
        if (compound.hasUUID("portalGunUUID"))
            portalGunUUID = compound.getUUID("portalGunUUID");
        if (compound.hasUUID("linkedPortalUUID"))
            linkedPortalUUID = compound.getUUID("linkedPortalUUID");
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putByte("direction", this.entityData.get(DIRECTION));
        compound.putByte("alignment", this.entityData.get(ALIGNMENT));
        compound.putBoolean("isPrimary", getIsPrimary());
        compound.putBoolean("isDying", isDying());
        compound.putInt("deathCounter", deathCounter);
        if (this.getPortalGunUUID() != null) {
            compound.putUUID("portalGunUUID", this.getPortalGunUUID());
        }
        if (this.linkedPortalUUID != null) {
            compound.putUUID("linkedPortalUUID", this.linkedPortalUUID);
        }
        if (this.ownerUUID != null) {
            compound.putUUID("Owner", this.ownerUUID);
        }
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
        if (!level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            ChunkPos chunkPos = new ChunkPos(this.blockPosition());
            Registration.TICKET_CONTROLLER.forceChunk(serverLevel, this, chunkPos.x, chunkPos.z, true, false);

            level().playSound(
                    null,
                    getX(),
                    getY(),
                    getZ(),
                    Registration.PORTAL_GUN_OPEN.get(),
                    SoundSource.NEUTRAL,
                    0.75F,
                    0.4F
            );
        }
    }

    public void setDying() {
        this.entityData.set(ISDYING, true);
        level().playSound(
                null,
                getX(),
                getY(),
                getZ(),
                Registration.PORTAL_GUN_CLOSE.get(),
                SoundSource.NEUTRAL,
                0.5F,
                0.2F
        );
    }

    @Override
    public void remove(Entity.RemovalReason pReason) {
        super.remove(pReason);
        if (!level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            ChunkPos chunkPos = new ChunkPos(this.blockPosition());
            Registration.TICKET_CONTROLLER.forceChunk(serverLevel, this, chunkPos.x, chunkPos.z, false, false);
        }
    }

    @Override
    public void refreshDimensions() {
        this.setBoundingBox(makeBoundingBox());
    }

    @Override
    protected AABB makeBoundingBox() {
        float width = 1f;
        float height = 2f;
        float depth = 0.2F; // Default depth

        float setWidth = width;
        float setHeight = height;
        float setDepth = depth;

        Direction direction = this.getDirection();
        Direction.Axis alignment = this.getAlignment();

        if (direction == Direction.UP || direction == Direction.DOWN) {
            setHeight = depth;
            if (alignment == Direction.Axis.X) {
                setWidth = height;
                setDepth = width;
                return this.makeBoundingBox(this.getX() + 1, this.getY(), this.getZ(), setWidth, setHeight, setDepth);
            } else {
                setWidth = width;
                setDepth = height;
                return this.makeBoundingBox(this.getX(), this.getY(), this.getZ() + 1, setWidth, setHeight, setDepth);
            }
        } else if (direction.getAxis() == Direction.Axis.Z) {
            setWidth = width;
            setDepth = depth;
            return this.makeBoundingBox(this.getX(), this.getY(), this.getZ(), setWidth, setHeight, setDepth);
        } else if (direction.getAxis() == Direction.Axis.X) {
            setWidth = depth;
            setDepth = width;
            return this.makeBoundingBox(this.getX(), this.getY(), this.getZ(), setWidth, setHeight, setDepth);
        }
        return this.makeBoundingBox(this.getX(), this.getY(), this.getZ(), width, height, depth);
    }

    private AABB makeBoundingBox(double x, double y, double z, float width, float height, float depth) {
        float halfWidth = width / 2.0F;
        float halfDepth = depth / 2.0F;
        return new AABB(x - halfWidth, y, z - halfDepth, x + halfWidth, y + height, z + halfDepth);
    }

    public PortalEntity findPartnerPortal(MinecraftServer server) {
        for (ServerLevel serverLevel : server.getAllLevels()) {
            List<? extends PortalEntity> customEntities = serverLevel.getEntities(Registration.PortalEntity.get(), k -> k.getUUID().equals(this.linkedPortalUUID));
            if (!customEntities.isEmpty())
                return customEntities.getFirst();
        }
        return null;
    }

    public PortalEntity getLinkedPortal() {
        if (level().isClientSide) return null;
        if (linkedPortal == null && linkedPortalUUID != null) {
            linkedPortal = findPartnerPortal(level().getServer());
        }
        return linkedPortal;
    }

    public void setLinkedPortal(PortalEntity linkedPortal) {
        this.linkedPortalUUID = linkedPortal.getUUID();
        this.linkedPortal = null;
    }

    public Vec3 getTeleportTo(Entity entity, PortalEntity matchingPortal) {
        Vec3 teleportTo;
        double entityHeightFraction;
        AABB entityBB = entity.getBoundingBox();
        AABB portalBB = this.getBoundingBox();
        if (getDirection().getAxis() == Direction.Axis.Y) {
            if (getAlignment() == Direction.Axis.X) {
                entityHeightFraction = Math.abs((((entityBB.maxX + entityBB.minX) / 2) - portalBB.minX) / portalBB.getXsize());
            } else {
                entityHeightFraction = Math.abs((((entityBB.maxZ + entityBB.minZ) / 2) - portalBB.minZ) / portalBB.getZsize());
            }
        } else {
            entityHeightFraction = (entityBB.minY - portalBB.minY) / portalBB.getYsize();
        }

        // Clamp fraction to [0, 1] to avoid out-of-bounds positioning
        entityHeightFraction = Math.max(0.0, Math.min(1.0, entityHeightFraction));

        if (matchingPortal.getDirection().getAxis() == Direction.Axis.Y) {
            if (matchingPortal.getAlignment() == Direction.Axis.X) {
                double offset = entityHeightFraction * linkedPortal.getBoundingBox().getXsize();
                double buffer = entityBB.getXsize() / 2 + Shapes.EPSILON;
                // Don't collide in wall next to top/bottom of floor/ceiling portal
                offset = Mth.clamp(offset, buffer, linkedPortal.getBoundingBox().getXsize() - buffer);
                teleportTo = new Vec3(linkedPortal.getBoundingBox().minX + offset, linkedPortal.getY(), linkedPortal.getZ());
            } else {
                double offset = entityHeightFraction * linkedPortal.getBoundingBox().getZsize();
                double buffer = entityBB.getZsize() / 2 + Shapes.EPSILON;
                // Don't collide in wall next to top/bottom of floor/ceiling portal
                offset = Mth.clamp(offset, buffer, linkedPortal.getBoundingBox().getZsize() - buffer);
                teleportTo = new Vec3(linkedPortal.getX(), linkedPortal.getY(), linkedPortal.getBoundingBox().minZ + offset);
            }
        } else {
            teleportTo = new Vec3(linkedPortal.getX(), linkedPortal.getBoundingBox().minY + entityHeightFraction * linkedPortal.getBoundingBox().getYsize(), linkedPortal.getZ());
        }

        // Move to 'side' of portal (except for portals facing up, where it would give us extra velocity)
        if (linkedPortal.getDirection() == Direction.DOWN) {
            teleportTo = teleportTo.relative(Direction.DOWN, entityBB.getYsize());
        } else if (linkedPortal.getDirection() != Direction.UP) {
            if (linkedPortal.getDirection().getAxis() == Direction.Axis.X) {
                teleportTo = teleportTo.relative(linkedPortal.getDirection(), linkedPortal.getBoundingBox().getXsize() / 2 + entityBB.getXsize() / 2);
            } else if (linkedPortal.getDirection().getAxis() == Direction.Axis.Z) {
                teleportTo = teleportTo.relative(linkedPortal.getDirection(), linkedPortal.getBoundingBox().getZsize() / 2 + entityBB.getZsize() / 2);
            }
        }

        // Don't immediately collide again
        teleportTo = teleportTo.relative(linkedPortal.getDirection(), Shapes.EPSILON);

        return teleportTo;
    }

    public Vec3 calculateVelocity(Entity entity) {
        Vec3 newMotion = Vec3.ZERO;
        UUID entityUUID = entity.getUUID();
        if (entityLastPosition.containsKey(entityUUID)) {
            double threshold = 0.2;
            Vec3 previousPos = entityLastPosition.get(entityUUID);
            Vec3 currentPos = entity.position();
            // Calculate velocity based on position change and assuming a tick length of 1/20th of a second
            Vec3 thisVelocity = currentPos.subtract(previousPos);
            Vec3 lastVelocity = Vec3.ZERO;
            if (entityLastLastPosition.containsKey(entityUUID)) {
                Vec3 lastLastPos = entityLastLastPosition.get(entityUUID);
                Vec3 lastPos = entityLastPosition.get(entityUUID);
                lastVelocity = lastPos.subtract(lastLastPos);
            }
            Vec3 velocity = lastVelocity.equals(Vec3.ZERO) ? thisVelocity : lastVelocity;
            if (Math.abs(velocity.x) > threshold || Math.abs(velocity.y) > threshold || Math.abs(velocity.z) > threshold || velocity.y > 0) {
                newMotion = transformMotion(velocity, getDirection(), linkedPortal.getDirection().getOpposite());
            }
            entityLastPosition.remove(entityUUID);
            entityLastLastPosition.remove(entityUUID);
        }
        return newMotion;
    }

    public void teleport(Entity entity) {
        if (entity.level().isClientSide) return;
        if (getLinkedPortal() != null) {
            Vec3 teleportTo = getTeleportTo(entity, linkedPortal);
            // Adjust the entity's rotation to match the exit portal's direction
            Vec2 newLookAngle = transformLookAngle(entity, linkedPortal);
            entity.resetFallDistance();

            Vec3 newMotion = calculateVelocity(entity);

            // Teleport the entity to the new location and set its rotation
            boolean success = entity.teleportTo((ServerLevel) linkedPortal.level(), teleportTo.x(), teleportTo.y(), teleportTo.z(), new HashSet<>(), newLookAngle.y, newLookAngle.x);

            if (success) {
                entity.resetFallDistance();
                if (!newMotion.equals(Vec3.ZERO)) {
                    entity.setDeltaMovement(newMotion);
                    entity.hasImpulse = true;
                    if (entity instanceof Player player)
                        ((ServerPlayer) player).connection.send(new ClientboundSetEntityMotionPacket(player));
                    else
                        ((ServerLevel) entity.level()).getChunkSource().broadcast(entity, new ClientboundSetEntityMotionPacket(entity));
                }
                linkedPortal.entityCooldowns.put(entity.getUUID(), TELEPORT_COOLDOWN); //Ensure it doesn't get teleported back!
            }
        }
    }

    public boolean isValidEntity(Entity entity) {
        if (entity.getType().equals(Registration.PortalEntity.get()))
            return false;
        if (entityCooldowns.containsKey(entity.getUUID()))
            return false; // Skip entities with active cooldown
        if (entity.isMultipartEntity())
            return false;
        if (entity instanceof PartEntity<?>)
            return false;
        if (!entity.canChangeDimensions(level(), getLinkedPortal().level()) && !isSameLevel())
            return false;
        if (entity.getType().is(Tags.EntityTypes.TELEPORTING_NOT_SUPPORTED))
            return false;
        return true;
    }

    public boolean isSameLevel() {
        return level().equals(getLinkedPortal().level());
    }

    // Helper method to transform the motion vector based on entry and exit directions
    public static Vec3 transformMotion(Vec3 motion, Direction from, Direction to) {
        // Get the rotation quaternions for the from and to directions
        Quaternionf fromRotation = from.getRotation();
        Quaternionf toRotation = to.getRotation();

        // Invert the fromRotation quaternion
        fromRotation.invert();

        // Transform the motion vector
        Vector3f motionVec = new Vector3f((float) motion.x, (float) motion.y, (float) motion.z);

        // Apply the rotations
        fromRotation.transform(motionVec);
        toRotation.transform(motionVec);

        return new Vec3(motionVec.x(), motionVec.y(), motionVec.z());
    }

    /**
     * Adjust the entity's look angle (pitch and yaw) for seamless transitions between portals.
     *
     * @param entity      entity
     * @param destination destination portal
     * @return adjusted (xrot, yrot)
     */
    private Vec2 transformLookAngle(Entity entity, PortalEntity destination) {
        final Vec3 newLook = transformMotion(entity.getLookAngle(), getDirection(), destination.getDirection().getOpposite());
        return toDegrees(newLook);
    }

    private static Vec2 toDegrees(Vec3 vector) {
        double x = vector.x;
        double y = vector.y;
        double z = vector.z;
        double hyp = Math.sqrt(x * x + z * z);
        float xrot = Mth.wrapDegrees((float)(-(Mth.atan2(y, hyp) * 180.0F / (float)Math.PI)));
        float yrot = Mth.wrapDegrees((float)(Mth.atan2(z, x) * 180.0F / (float)Math.PI) - 90.0F);
        return new Vec2(xrot, yrot);
    }
}
