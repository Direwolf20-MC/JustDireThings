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

import java.util.UUID;

public class PortalEntity extends Entity {
    private PortalEntity linkedPortal;
    private UUID ownerUUID;

    private static final EntityDataAccessor<Byte> DIRECTION = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.BYTE);

    public PortalEntity(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    public PortalEntity(Level world, Player player, Direction direction) {
        this(Registration.PortalEntity.get(), world);
        this.entityData.set(DIRECTION, (byte) direction.ordinal());
        this.ownerUUID = player.getUUID();
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    @Override
    public void tick() {
        super.tick();
        //System.out.println("I'm here: " + this.tickCount);
        //if (tickCount > 60)
        //    this.discard();
    }

    public Direction getDirection() {
        return Direction.values()[this.entityData.get(DIRECTION)];
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DIRECTION, (byte) 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(DIRECTION, compound.getByte("direction"));
        if (compound.hasUUID("Owner"))
            ownerUUID = compound.getUUID("Owner");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putByte("direction", this.entityData.get(DIRECTION));
        if (this.getOwnerUUID() != null) {
            compound.putUUID("Owner", this.getOwnerUUID());
        }
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
