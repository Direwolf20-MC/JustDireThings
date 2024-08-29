package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.client.particles.paradoxparticle.ParadoxParticleData;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ParadoxEntity extends Entity {
    private static final EntityDataAccessor<Integer> REQUIRED_CONSUMPTION = SynchedEntityData.defineId(ParadoxEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CONSUMPTION = SynchedEntityData.defineId(ParadoxEntity.class, EntityDataSerializers.INT);

    public ParadoxEntity(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
    }

    public ParadoxEntity(Level level, BlockPos blockPos) {
        this(Registration.ParadoxEntity.get(), level);
        this.moveTo(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            float val = random.nextFloat();
            //if (val < 0.99) return;
            Vec3 targetVec = position().add(0, 0.5, 0);
            ParadoxParticleData data = new ParadoxParticleData(new ItemStack(Items.COBBLESTONE), targetVec.x, targetVec.y, targetVec.z, 1);
            double d0 = targetVec.x() - 2;
            double d1 = targetVec.y() + 1.5;
            double d2 = targetVec.z() + 2;
            ((ServerLevel) level()).sendParticles(data, d0, d1, d2, 1, 0, 0, 0, 0);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(REQUIRED_CONSUMPTION, 100);
        builder.define(CONSUMPTION, 0);
    }

    public int getRequiredConsumption() {
        return this.entityData.get(REQUIRED_CONSUMPTION);
    }

    public int getConsumed() {
        return this.entityData.get(CONSUMPTION);
    }

    public void setRequiredConsumption(int totalRequired) {
        this.entityData.set(REQUIRED_CONSUMPTION, totalRequired);
    }

    public void setConsumption(int consumed) {
        this.entityData.set(CONSUMPTION, consumed);
    }

    public void consume(int amt) {
        this.entityData.set(CONSUMPTION, getConsumed() + amt);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("requiredConsumption"))
            this.entityData.set(REQUIRED_CONSUMPTION, compound.getInt("requiredConsumption"));
        if (compound.contains("consumed"))
            this.entityData.set(CONSUMPTION, compound.getInt("consumed"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("requiredConsumption", getRequiredConsumption());
        compound.putInt("consumed", getConsumed());
    }
}
