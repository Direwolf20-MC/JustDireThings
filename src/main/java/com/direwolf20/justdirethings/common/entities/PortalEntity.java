package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.UUID;

public class PortalEntity extends Entity {
    private PortalEntity linkedPortal;
    private UUID ownerUUID;

    private static final EntityDataAccessor<Byte> DIRECTION = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> ALIGNMENT = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.BYTE);

    public PortalEntity(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    public PortalEntity(Level world, Player player, Direction direction, Direction.Axis axis) {
        this(Registration.PortalEntity.get(), world);
        this.entityData.set(DIRECTION, (byte) direction.ordinal());
        this.ownerUUID = player.getUUID();
        this.entityData.set(ALIGNMENT, (byte) axis.ordinal());
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    @Override
    public void tick() {
        super.tick();
        refreshDimensions();
        //System.out.println("I'm here: " + this.tickCount);
        //if (tickCount > 60)
        //    this.discard();
    }

    public Direction getDirection() {
        return Direction.values()[this.entityData.get(DIRECTION)];
    }

    public Direction.Axis getAlignment() {
        return Direction.Axis.values()[this.entityData.get(ALIGNMENT)];
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DIRECTION, (byte) 0);
        builder.define(ALIGNMENT, (byte) Direction.Axis.Z.ordinal());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(DIRECTION, compound.getByte("direction"));
        this.entityData.set(ALIGNMENT, compound.getByte("alignment"));
        if (compound.hasUUID("Owner"))
            ownerUUID = compound.getUUID("Owner");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putByte("direction", this.entityData.get(DIRECTION));
        compound.putByte("alignment", this.entityData.get(ALIGNMENT));
        if (this.getOwnerUUID() != null) {
            compound.putUUID("Owner", this.getOwnerUUID());
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
        float depth = 0.1F; // Default depth

        Direction direction = this.getDirection();
        Direction.Axis alignment = this.getAlignment();

        if (direction == Direction.UP || direction == Direction.DOWN) {
            height = 0.1f;
            if (alignment == Direction.Axis.X) {
                width = 2.0F;
                depth = 1.0F;
                return this.makeBoundingBox(this.getX() - 0.5f, this.getY(), this.getZ(), width, height, depth);
            } else {
                width = 1.0F;
                depth = 2.0F;
                return this.makeBoundingBox(this.getX(), this.getY(), this.getZ() - 0.5f, width, height, depth);
            }
        } else if (direction.getAxis() == Direction.Axis.Z) {
            width = 1.0F;
            depth = 0.1f;
            return this.makeBoundingBox(this.getX(), this.getY() - .5f, this.getZ(), width, height, depth);
        } else if (direction.getAxis() == Direction.Axis.X) {
            width = 0.1f;
            depth = 1.0F;
            return this.makeBoundingBox(this.getX(), this.getY() - .5f, this.getZ(), width, height, depth);
        }
        return this.makeBoundingBox(this.getX(), this.getY(), this.getZ(), width, height, depth);
    }

    private AABB makeBoundingBox(double x, double y, double z, float width, float height, float depth) {
        float halfWidth = width / 2.0F;
        float halfDepth = depth / 2.0F;
        float halfHeight = height / 2.0F;
        return new AABB(x - halfWidth, y - halfHeight, z - halfDepth, x + halfWidth, y + halfHeight, z + halfDepth);
    }

    public void setLinkedPortal(PortalEntity linkedPortal) {
        this.linkedPortal = linkedPortal;
    }

    public PortalEntity getLinkedPortal() {
        return linkedPortal;
    }

    public void teleport(Player player) {
        if (linkedPortal != null) {
            player.teleportTo(linkedPortal.getX(), linkedPortal.getY(), linkedPortal.getZ());
        }
    }
}
