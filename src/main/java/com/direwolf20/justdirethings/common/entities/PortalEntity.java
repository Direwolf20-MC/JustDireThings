package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.common.worlddata.PortalRegistryData;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
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
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
        this(JDTRegistration.PortalEntity.get(), world);
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
        if (!level().isClientSide()) {
            tickCooldowns();
            // We no longer gate on a cached partner — the partner's chunk may be unloaded.
            // teleportCollidingEntities loads it on demand when an entity is actually colliding;
            // captureVelocity just records positions and doesn't need the partner.
            if (linkedPortalUUID != null) {
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
                if (!level().isClientSide()) {
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
    protected void readAdditionalSaveData(ValueInput input) {
        this.entityData.set(DIRECTION, input.getByteOr("direction", (byte) 0));
        this.entityData.set(ALIGNMENT, input.getByteOr("alignment", (byte) Direction.Axis.Z.ordinal()));
        this.entityData.set(ISPRIMARY, input.getBooleanOr("isPrimary", false));
        this.entityData.set(ISDYING, input.getBooleanOr("isDying", false));
        deathCounter = input.getIntOr("deathCounter", 0);
        portalGunUUID = input.read("portalGunUUID", UUIDUtil.CODEC).orElse(null);
        linkedPortalUUID = input.read("linkedPortalUUID", UUIDUtil.CODEC).orElse(null);
        this.ownerUUID = input.read("Owner", UUIDUtil.CODEC).orElse(null);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putByte("direction", this.entityData.get(DIRECTION));
        output.putByte("alignment", this.entityData.get(ALIGNMENT));
        output.putBoolean("isPrimary", getIsPrimary());
        output.putBoolean("isDying", isDying());
        output.putInt("deathCounter", deathCounter);
        output.storeNullable("portalGunUUID", UUIDUtil.CODEC, this.getPortalGunUUID());
        output.storeNullable("linkedPortalUUID", UUIDUtil.CODEC, this.linkedPortalUUID);
        output.storeNullable("Owner", UUIDUtil.CODEC, this.ownerUUID);
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
        if (!level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            if (isAdvanced) {
                // Advanced portals must keep ticking so their expirationTime runs down.
                ChunkPos chunkPos = ChunkPos.containing(this.blockPosition());
                JDTRegistration.TICKET_CONTROLLER.forceChunk(serverLevel, this, chunkPos.x(), chunkPos.z(), true, false);
            }

            MinecraftServer server = serverLevel.getServer();
            if (server != null) {
                PortalRegistryData.get(server).addOrUpdate(
                        this.getUUID(), serverLevel.dimension(), ChunkPos.containing(this.blockPosition()),
                        this.linkedPortalUUID, this.portalGunUUID, this.ownerUUID, getIsPrimary());
            }

            level().playSound(
                    null,
                    getX(),
                    getY(),
                    getZ(),
                    JDTRegistration.PORTAL_GUN_OPEN.get(),
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
                JDTRegistration.PORTAL_GUN_CLOSE.get(),
                SoundSource.NEUTRAL,
                0.5F,
                0.2F
        );
    }

    @Override
    public void remove(Entity.RemovalReason pReason) {
        super.remove(pReason);
        if (!level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            if (isAdvanced) {
                ChunkPos chunkPos = ChunkPos.containing(this.blockPosition());
                JDTRegistration.TICKET_CONTROLLER.forceChunk(serverLevel, this, chunkPos.x(), chunkPos.z(), false, false);
            }

            MinecraftServer server = serverLevel.getServer();
            if (server != null) {
                PortalRegistryData reg = PortalRegistryData.get(server);
                // Use the registry's current partner, not this.linkedPortalUUID — if the partner
                // was re-linked to another portal after we saved our field, the registry has been
                // updated and our field is stale. Cascading based on the stale field would kill
                // a portal that is no longer ours.
                PortalRegistryData.Entry selfEntry = reg.getEntry(this.getUUID());
                UUID partnerUuid = selfEntry != null ? selfEntry.partnerUuid() : null;
                reg.removePortal(this.getUUID());
                if (partnerUuid != null) {
                    PortalRegistryData.Entry partnerEntry = reg.getEntry(partnerUuid);
                    if (partnerEntry != null) {
                        PortalEntity partner = PortalRegistryData.resolveEntityForceLoad(server, partnerEntry);
                        if (partner != null && !partner.isDying()) partner.setDying();
                    }
                }
            }
        }
    }

    @Override
    public void refreshDimensions() {
        this.setBoundingBox(makeBoundingBox());
    }

    @Override
    protected AABB makeBoundingBox(Vec3 position) {
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
                return this.buildBox(position.x + 1, position.y, position.z, setWidth, setHeight, setDepth);
            } else {
                setWidth = width;
                setDepth = height;
                return this.buildBox(position.x, position.y, position.z + 1, setWidth, setHeight, setDepth);
            }
        } else if (direction.getAxis() == Direction.Axis.Z) {
            setWidth = width;
            setDepth = depth;
            return this.buildBox(position.x, position.y, position.z, setWidth, setHeight, setDepth);
        } else if (direction.getAxis() == Direction.Axis.X) {
            setWidth = depth;
            setDepth = width;
            return this.buildBox(position.x, position.y, position.z, setWidth, setHeight, setDepth);
        }
        return this.buildBox(position.x, position.y, position.z, width, height, depth);
    }

    private AABB buildBox(double x, double y, double z, float width, float height, float depth) {
        float halfWidth = width / 2.0F;
        float halfDepth = depth / 2.0F;
        return new AABB(x - halfWidth, y, z - halfDepth, x + halfWidth, y + height, z + halfDepth);
    }

    public PortalEntity getLinkedPortal() {
        return resolveLinkedPortal(false);
    }

    /**
     * Resolve partner, blocking-loading its chunk if necessary. Use only when a teleport is imminent.
     */
    public PortalEntity getOrLoadLinkedPortal() {
        return resolveLinkedPortal(true);
    }

    private PortalEntity resolveLinkedPortal(boolean loadIfNeeded) {
        if (level().isClientSide()) return null;
        if (linkedPortalUUID == null) return null;
        MinecraftServer server = level().getServer();
        if (server == null) return null;

        if (linkedPortal != null && !linkedPortal.isRemoved() && linkedPortal.getUUID().equals(linkedPortalUUID))
            return linkedPortal;

        PortalRegistryData reg = PortalRegistryData.get(server);
        PortalRegistryData.Entry partnerEntry = reg.getEntry(linkedPortalUUID);
        if (partnerEntry != null) {
            PortalEntity resolved = loadIfNeeded
                    ? PortalRegistryData.resolveEntityForceLoad(server, partnerEntry)
                    : PortalRegistryData.resolveEntity(server, partnerEntry);
            if (resolved != null) {
                linkedPortal = resolved;
                return resolved;
            }
        }
        linkedPortal = null;
        return null;
    }

    public void setLinkedPortal(PortalEntity other) {
        this.linkedPortalUUID = other.getUUID();
        this.linkedPortal = null;
        if (!level().isClientSide()) {
            MinecraftServer server = level().getServer();
            if (server != null) {
                PortalRegistryData.get(server).linkPair(
                        this.getUUID(), level().dimension(), ChunkPos.containing(this.blockPosition()),
                        this.portalGunUUID, this.ownerUUID, getIsPrimary(),
                        other.getUUID(), other.level().dimension(), ChunkPos.containing(other.blockPosition()),
                        other.getPortalGunUUID(), other.getOwner(), other.getIsPrimary());
            }
        }
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
        if (entity.level().isClientSide()) return;
        if (getOrLoadLinkedPortal() != null) {
            Vec3 teleportTo = getTeleportTo(entity, linkedPortal);
            // Adjust the entity's rotation to match the exit portal's direction
            Vec2 newLookAngle = transformLookAngle(entity, linkedPortal);
            entity.resetFallDistance();

            Vec3 newMotion = calculateVelocity(entity);

            // Teleport the entity to the new location and set its rotation
            boolean success = entity.teleportTo((ServerLevel) linkedPortal.level(), teleportTo.x(), teleportTo.y(), teleportTo.z(), EnumSet.noneOf(Relative.class), newLookAngle.y, newLookAngle.x, true);

            if (success) {
                entity.resetFallDistance();
                if (!newMotion.equals(Vec3.ZERO)) {
                    entity.setDeltaMovement(newMotion);
                    if (entity instanceof Player player) {
                        ((ServerPlayer) player).connection.send(new ClientboundSetEntityMotionPacket(player));
                    } else {
                        ServerLevel srv = (ServerLevel) entity.level();
                        ClientboundSetEntityMotionPacket packet = new ClientboundSetEntityMotionPacket(entity);
                        srv.getServer().getPlayerList().broadcast(null, entity.getX(), entity.getY(), entity.getZ(), 128.0, srv.dimension(), packet);
                    }
                }
                linkedPortal.entityCooldowns.put(entity.getUUID(), TELEPORT_COOLDOWN); //Ensure it doesn't get teleported back!
            }
        }
    }

    public boolean isValidEntity(Entity entity) {
        if (entity.getType().equals(JDTRegistration.PortalEntity.get()))
            return false;
        if (entityCooldowns.containsKey(entity.getUUID()))
            return false; // Skip entities with active cooldown
        if (entity.isMultipartEntity())
            return false;
        if (entity instanceof PartEntity<?>)
            return false;
        if (!entity.canUsePortal(false) && !isSameLevel())
            return false;
        if (entity.getType().builtInRegistryHolder().is(Tags.EntityTypes.TELEPORTING_NOT_SUPPORTED))
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

    @Override
    public boolean hurtServer(ServerLevel level, net.minecraft.world.damagesource.DamageSource source, float damage) {
        return false;
    }
}
