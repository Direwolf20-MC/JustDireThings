package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.common.network.data.MomentumPayload;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;

public class PortalEntity extends Entity {
    private PortalEntity linkedPortal;
    private UUID portalGunUUID;
    private UUID linkedPortalUUID;
    private static final int TELEPORT_COOLDOWN = 10; // Cooldown period in ticks (1 second)
    public final Map<UUID, Integer> entityCooldowns = new HashMap<>();
    public final Map<UUID, Integer> entityVelocityCooldowns = new HashMap<>();
    public final Map<UUID, Vec3> entityVelocities = new HashMap<>();

    private static final EntityDataAccessor<Byte> DIRECTION = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> ALIGNMENT = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ISPRIMARY = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.BOOLEAN);

    public PortalEntity(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    public PortalEntity(Level world, Player player, Direction direction, Direction.Axis axis, UUID portalGunUUID, boolean isPrimary) {
        this(Registration.PortalEntity.get(), world);
        this.entityData.set(DIRECTION, (byte) direction.ordinal());
        this.portalGunUUID = portalGunUUID;
        this.entityData.set(ALIGNMENT, (byte) axis.ordinal());
        this.entityData.set(ISPRIMARY, isPrimary);
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
        tickCooldowns();
        if (level().isClientSide) {
            captureVelocity();
        }
        teleportCollidingEntities();
    }

    public void tickCooldowns() {
        // Update cooldowns
        if (level().isClientSide) {
            entityVelocityCooldowns.entrySet().removeIf(entry -> {
                if (entry.getValue() <= 0) {
                    entityVelocities.remove(entry.getKey());
                    return true;
                }
                entry.setValue(entry.getValue() - 1);
                return false;
            });
        } else {
            entityCooldowns.entrySet().removeIf(entry -> {
                if (entry.getValue() <= 0) {
                    return true;
                }
                entry.setValue(entry.getValue() - 1);
                return false;
            });
        }
    }

    public void captureVelocity() {
        AABB boundingBox = this.getBoundingBox().expandTowards(getDirection().getStepX() * 0.75, getDirection().getStepY() * 0.75, getDirection().getStepZ() * 0.75);
        List<Entity> entities = level().getEntities(this, boundingBox);
        for (Entity entity : entities) {
            if (entity != this && isValidEntity(entity) && !entityVelocities.containsKey(entity.getUUID())) {
                double threshold = 0.075;
                Vec3 previousPos = new Vec3(entity.xo, entity.yo, entity.zo);
                Vec3 currentPos = entity.position();
                if (previousPos.equals(currentPos)) continue;
                // Calculate velocity based on position change and assuming a tick length of 1/20th of a second
                Vec3 velocity = currentPos.subtract(previousPos);
                if (Math.abs(velocity.x) > threshold || Math.abs(velocity.y) > threshold || Math.abs(velocity.z) > threshold || velocity.y > 0) {
                    entityVelocities.put(entity.getUUID(), velocity);
                    entityVelocityCooldowns.put(entity.getUUID(), 10);
                }
            }
        }
    }

    public void teleportCollidingEntities() {
        AABB boundingBox = this.getBoundingBox();
        List<Entity> entities = level().getEntities(this, boundingBox);
        for (Entity entity : entities) {
            if (entity != this && isValidEntity(entity)) {
                if (!level().isClientSide) {
                    teleport(entity);
                } else {
                    if (entityVelocities.containsKey(entity.getUUID()) && !entityCooldowns.containsKey(entity.getUUID())) {
                        PacketDistributor.sendToServer(new MomentumPayload(entityVelocities.get(entity.getUUID()), getUUID(), entity.getUUID()));
                        entityVelocities.remove(entity.getUUID());
                    }
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

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DIRECTION, (byte) 0);
        builder.define(ALIGNMENT, (byte) Direction.Axis.Z.ordinal());
        builder.define(ISPRIMARY, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(DIRECTION, compound.getByte("direction"));
        this.entityData.set(ALIGNMENT, compound.getByte("alignment"));
        this.entityData.set(ISPRIMARY, compound.getBoolean("isPrimary"));
        if (compound.hasUUID("portalGunUUID"))
            portalGunUUID = compound.getUUID("portalGunUUID");
        if (compound.hasUUID("linkedPortalUUID"))
            linkedPortalUUID = compound.getUUID("linkedPortalUUID");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putByte("direction", this.entityData.get(DIRECTION));
        compound.putByte("alignment", this.entityData.get(ALIGNMENT));
        compound.putBoolean("isPrimary", getIsPrimary());
        if (this.getPortalGunUUID() != null) {
            compound.putUUID("portalGunUUID", this.getPortalGunUUID());
        }
        if (this.linkedPortalUUID != null) {
            compound.putUUID("linkedPortalUUID", this.linkedPortalUUID);
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
                return this.makeBoundingBox(this.getX() - 0.5f, this.getY(), this.getZ(), setWidth, setHeight, setDepth);
            } else {
                setWidth = width;
                setDepth = height;
                return this.makeBoundingBox(this.getX(), this.getY(), this.getZ() - 0.5f, setWidth, setHeight, setDepth);
            }
        } else if (direction.getAxis() == Direction.Axis.Z) {
            setWidth = width;
            setDepth = depth;
            return this.makeBoundingBox(this.getX(), this.getY() - .5f, this.getZ(), setWidth, setHeight, setDepth);
        } else if (direction.getAxis() == Direction.Axis.X) {
            setWidth = depth;
            setDepth = width;
            return this.makeBoundingBox(this.getX(), this.getY() - .5f, this.getZ(), setWidth, setHeight, setDepth);
        }
        return this.makeBoundingBox(this.getX(), this.getY(), this.getZ(), width, height, depth);
    }

    private AABB makeBoundingBox(double x, double y, double z, float width, float height, float depth) {
        float halfWidth = width / 2.0F;
        float halfDepth = depth / 2.0F;
        float halfHeight = height / 2.0F;
        return new AABB(x - halfWidth, y - halfHeight, z - halfDepth, x + halfWidth, y + halfHeight, z + halfDepth);
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
        if (matchingPortal.getDirection().getAxis() == Direction.Axis.Y) {
            if (matchingPortal.getAlignment() == Direction.Axis.X) {
                teleportTo = new Vec3(linkedPortal.getBoundingBox().minX + entityHeightFraction * linkedPortal.getBoundingBox().getXsize(), linkedPortal.getY(), linkedPortal.getZ()).relative(linkedPortal.getDirection(), 1f);
            } else {
                teleportTo = new Vec3(linkedPortal.getX(), linkedPortal.getY(), linkedPortal.getBoundingBox().minZ + entityHeightFraction * linkedPortal.getBoundingBox().getZsize()).relative(linkedPortal.getDirection(), 1f);
            }
        } else {
            teleportTo = new Vec3(linkedPortal.getX(), linkedPortal.getBoundingBox().minY + entityHeightFraction * linkedPortal.getBoundingBox().getYsize(), linkedPortal.getZ()).relative(linkedPortal.getDirection(), 1f);
        }

        if (linkedPortal.getDirection() == Direction.DOWN)
            teleportTo = teleportTo.relative(Direction.DOWN, 1f);
        return teleportTo;
    }

    public void teleport(Entity entity) {
        if (entity.level().isClientSide) return;
        PortalEntity matchingPortal = getLinkedPortal();
        if (matchingPortal != null) {
            Vec3 teleportTo = getTeleportTo(entity, matchingPortal);
            // Adjust the entity's rotation to match the exit portal's direction
            float newYaw = getYawFromDirection(linkedPortal.getDirection());
            float newPitch = entity.getXRot(); // Maintain the same pitch
            entity.resetFallDistance();

            // Teleport the entity to the new location and set its rotation
            boolean success = entity.teleportTo((ServerLevel) linkedPortal.level(), teleportTo.x(), teleportTo.y(), teleportTo.z(), new HashSet<>(), newYaw, newPitch);

            if (success) {
                entity.resetFallDistance();
                entity.hasImpulse = true;
                if (entity instanceof Player player)
                    ((ServerPlayer) player).connection.send(new ClientboundSetEntityMotionPacket(player));
                else
                    ((ServerLevel) entity.level()).getChunkSource().broadcast(entity, new ClientboundSetEntityMotionPacket(entity));
                linkedPortal.entityCooldowns.put(entity.getUUID(), TELEPORT_COOLDOWN); //Ensure it doesn't get teleported back!
            }
        }
    }

    public boolean isValidEntity(Entity entity) {
        if (entityCooldowns.containsKey(entity.getUUID())) {
            return false; // Skip entities with active cooldown
        }
        if (entity.isMultipartEntity())
            return false;
        if (entity instanceof PartEntity<?>)
            return false;
        if (!entity.canChangeDimensions() && !isSameLevel())
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

    // Helper method to get the yaw from a direction
    private float getYawFromDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> 180.0F;
            case SOUTH -> 0.0F;
            case WEST -> 90.0F;
            case EAST -> -90.0F;
            default -> 0.0F;
        };
    }
}
