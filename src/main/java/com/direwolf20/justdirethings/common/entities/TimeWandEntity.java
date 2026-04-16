package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.MiscTools;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class TimeWandEntity extends Entity {
    private static final EntityDataAccessor<Integer> TICKSPEED = SynchedEntityData.defineId(TimeWandEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> REMAINING_TIME = SynchedEntityData.defineId(TimeWandEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TOTAL_TIME = SynchedEntityData.defineId(TimeWandEntity.class, EntityDataSerializers.INT);
    private BlockPos blockPos;

    public TimeWandEntity(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
    }

    public TimeWandEntity(Level level, BlockPos blockPos) {
        this(Registration.TimeWandEntity.get(), level);
        this.blockPos = blockPos;
        this.snapTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            if (!canSurvive())
                this.remove(RemovalReason.DISCARDED);
            doExtraTicks();
            tickLife();
        }
    }

    public boolean canSurvive() {
        if (blockPos == null)
            return false;
        if (getRemainingTime() < 0)
            return false;
        return true;
    }

    public void doExtraTicks() {
        if (level() instanceof ServerLevel serverLevel) { //Should always be true - mostly just getting serverlevel!
            MiscTools.doExtraTicks(serverLevel, blockPos, getAccelerationRate());
        }
    }

    public void tickLife() {
        setRemainingTime(getRemainingTime() - 1);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(TICKSPEED, 1);
        builder.define(REMAINING_TIME, 600); //30 Seconds Default
        builder.define(TOTAL_TIME, 600); //30 Seconds Default
    }

    public int getTickSpeed() {
        return this.entityData.get(TICKSPEED);
    }

    public int getTotalTime() {
        return this.entityData.get(TOTAL_TIME);
    }

    public float getAccelerationRate() {
        return calculateAccelRate(getTickSpeed());
    }

    public static float calculateAccelRate(int speed) {
        return (float) Math.pow(2, speed);
    }

    public void setTickSpeed(int tickSpeed) {
        this.entityData.set(TICKSPEED, tickSpeed);
    }

    public void setTotalTime(int totalTime) {
        this.entityData.set(TOTAL_TIME, totalTime);
    }

    public int getRemainingTime() {
        return this.entityData.get(REMAINING_TIME);
    }

    public void setRemainingTime(int remainingTime) {
        this.entityData.set(REMAINING_TIME, remainingTime);
    }

    public void addTime(int moreTime) {
        this.entityData.set(REMAINING_TIME, getRemainingTime() + moreTime);
        //this.entityData.set(TOTAL_TIME, getTotalTime() + moreTime);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        input.getInt("tickSpeed").ifPresent(v -> this.entityData.set(TICKSPEED, v));
        input.getInt("remainingTime").ifPresent(v -> this.entityData.set(REMAINING_TIME, v));
        input.getInt("totalTime").ifPresent(v -> this.entityData.set(TOTAL_TIME, v));
        this.blockPos = input.read("blockpos", BlockPos.CODEC).orElse(null);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putInt("tickSpeed", getTickSpeed());
        output.putInt("remainingTime", getRemainingTime());
        output.storeNullable("blockpos", BlockPos.CODEC, blockPos);
        output.putInt("totalTime", getTotalTime());
    }

    @Override
    public boolean hurtServer(ServerLevel level, net.minecraft.world.damagesource.DamageSource source, float damage) {
        return false;
    }
}
