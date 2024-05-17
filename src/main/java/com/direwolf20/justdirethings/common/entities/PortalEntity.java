package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PortalEntity extends Entity {
    private PortalEntity linkedPortal;

    public PortalEntity(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    public PortalEntity(Level world) {
        this(Registration.PortalEntity.get(), world);
    }

    @Override
    public void tick() {
        super.tick();
        System.out.println("I'm here: " + this.tickCount);
        if (tickCount > 60)
            this.discard();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
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
