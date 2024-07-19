package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.world.entity.projectile.ThrownPotion.WATER_SENSITIVE_OR_ON_FIRE;

public class JustDireArrow extends AbstractArrow {
    private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR = SynchedEntityData.defineId(JustDireArrow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_POTIONARROW = SynchedEntityData.defineId(JustDireArrow.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SPLASH = SynchedEntityData.defineId(JustDireArrow.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_LINGERING = SynchedEntityData.defineId(JustDireArrow.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_HOMING = SynchedEntityData.defineId(JustDireArrow.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ARROW_STATE = SynchedEntityData.defineId(JustDireArrow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> STATE_TICK_COUNTER = SynchedEntityData.defineId(JustDireArrow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> ORIGINAL_VELOCITY = SynchedEntityData.defineId(JustDireArrow.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> IS_EPIC_ARROW = SynchedEntityData.defineId(JustDireArrow.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_PHASE = SynchedEntityData.defineId(JustDireArrow.class, EntityDataSerializers.BOOLEAN);

    private enum ArrowState {
        NORMAL,
        SLOWING_DOWN,
        STOPPED_AND_ROTATING,
        RESUMING_FLIGHT
    }

    private static final int SLOW_DOWN_DURATION = 4; // 1 second at 20 ticks per second
    private static final int STOP_DURATION = 10; // 1 second stop duration
    private static final int MAX_TARGETS = 10;

    private static final byte EVENT_POTION_PUFF = 0;

    private boolean canHitMobs = true;

    private LivingEntity targetEntity;

    public JustDireArrow(EntityType<? extends AbstractArrow> p_36858_, Level p_36859_) {
        super(p_36858_, p_36859_);
    }

    public JustDireArrow(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack itemStack) {
        super(Registration.JustDireArrow.get(), x, y, z, level, pickupItemStack, itemStack);
        this.updateColor();
    }

    public JustDireArrow(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack itemStack) {
        super(Registration.JustDireArrow.get(), owner, level, pickupItemStack, itemStack);
        this.updateColor();
    }

    public PotionContents getPotionContents() {
        return this.getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
    }

    public void setPotionContents(PotionContents p_331534_) {
        this.getPickupItemStackOrigin().set(DataComponents.POTION_CONTENTS, p_331534_);
        this.updateColor();
    }

    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    protected void setPickupItemStack(ItemStack p_331667_) {
        super.setPickupItemStack(p_331667_);
        this.updateColor();
    }

    private void updateColor() {
        PotionContents potioncontents = this.getPotionContents();
        this.entityData.set(ID_EFFECT_COLOR, potioncontents.equals(PotionContents.EMPTY) ? -1 : potioncontents.getColor());
    }

    public void addEffect(MobEffectInstance effectInstance) {
        this.setPotionContents(this.getPotionContents().withEffectAdded(effectInstance));
    }

    public void setPotionArrow(boolean potionArrow) {
        this.entityData.set(IS_POTIONARROW, potionArrow);
    }

    public void setSplash(boolean splash) {
        this.entityData.set(IS_SPLASH, splash);
    }

    public void setLingering(boolean lingering) {
        this.entityData.set(IS_LINGERING, lingering);
    }

    public void setHoming(boolean homing) {
        this.entityData.set(IS_HOMING, homing);
    }

    public void setPhase(boolean phase) {
        this.entityData.set(IS_PHASE, phase);
    }

    public boolean isPhase() {
        return this.entityData.get(IS_PHASE);
    }

    public float getOriginalVelocity() {
        return this.entityData.get(ORIGINAL_VELOCITY);
    }

    public void setEpicArrow(boolean isEpicArrow) {
        this.entityData.set(IS_EPIC_ARROW, isEpicArrow);
        this.setPierceLevel((byte) 5);
    }

    public boolean isEpic() {
        return this.entityData.get(IS_EPIC_ARROW);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder p_326324_) {
        super.defineSynchedData(p_326324_);
        p_326324_.define(ID_EFFECT_COLOR, -1);
        p_326324_.define(IS_POTIONARROW, false);
        p_326324_.define(IS_SPLASH, false);
        p_326324_.define(IS_LINGERING, false);
        p_326324_.define(IS_HOMING, false);
        p_326324_.define(ARROW_STATE, ArrowState.NORMAL.ordinal());
        p_326324_.define(STATE_TICK_COUNTER, 0);
        p_326324_.define(ORIGINAL_VELOCITY, 0f);
        p_326324_.define(IS_EPIC_ARROW, false);
        p_326324_.define(IS_PHASE, false);
    }

    public void setData(EntityDataAccessor<Integer> entityDataAccessor, int value) {
        if (!this.level().isClientSide)
            this.entityData.set(entityDataAccessor, value);
    }

    public void setData(EntityDataAccessor<Boolean> entityDataAccessor, boolean value) {
        if (!this.level().isClientSide)
            this.entityData.set(entityDataAccessor, value);
    }

    public double searchRadius() {
        return isEpic() ? 20 : 10;
    }

    @Override
    public void setDeltaMovement(Vec3 deltaMovement) {
        super.setDeltaMovement(deltaMovement);
    }

    @Override
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        if (!canHitMobs)
            return null;
        return super.findHitEntity(startVec, endVec);
    }

    @Override
    public void tick() {
        if (isPhase()) {
            this.noPhysics = true;
        } else {
            this.noPhysics = false;
        }
        if (this.isPhase() && !level().isClientSide) {
            if (tickCount >= 200) {
                this.discard();
                return;
            }
            canHitMobs = true;
            // Handle entity collisions manually
            Vec3 vec32 = this.position();
            Vec3 vec33 = vec32.add(getDeltaMovement());
            EntityHitResult entityhitresult = this.findHitEntity(vec32, vec33);
            if (entityhitresult != null) {
                this.onHit(entityhitresult);
            }
            canHitMobs = false;
        }
        super.tick();

        if (isEpic() && targetEntity != null && wasAlreadyHit(targetEntity)) {
            targetEntity = this.findNearestEntity();
        }
        if (!level().isClientSide && getOriginalVelocity() == 0f)
            this.entityData.set(ORIGINAL_VELOCITY, (float) this.getDeltaMovement().length());
        if (this.targetEntity != null && !this.targetEntity.isAlive()) {
            if (!isEpic()) {
                this.discard();
            } else {
                targetEntity = this.findNearestEntity();
                if (targetEntity == null || !targetEntity.isAlive()) {
                    this.discard();
                }
            }
        }

        if (this.entityData.get(IS_HOMING) && !this.inGround) {
            ArrowState currentState = ArrowState.values()[this.entityData.get(ARROW_STATE)];
            if (currentState != ArrowState.NORMAL && targetEntity == null && tickCount > 5) {
                this.discard();
                return;
            }
            int stateTickCounter = this.entityData.get(STATE_TICK_COUNTER);

            switch (currentState) {
                case NORMAL:
                    handleNormalState(stateTickCounter);
                    break;
                case SLOWING_DOWN:
                    handleSlowingDownState(stateTickCounter);
                    break;
                case STOPPED_AND_ROTATING:
                    handleStoppedAndRotatingState(stateTickCounter);
                    break;
                case RESUMING_FLIGHT:
                    handleResumingFlightState(stateTickCounter);
                    break;
            }

            stateTickCounter++;
            setData(STATE_TICK_COUNTER, stateTickCounter);
        }
        if (this.level().isClientSide) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.makeParticle(1);
                }
            } else {
                this.makeParticle(2);
            }
        } else {
            if (this.inGround && this.inGroundTime != 0 && !this.getPotionContents().equals(PotionContents.EMPTY) && this.inGroundTime >= 600) {
                this.level().broadcastEntityEvent(this, (byte) 0);
                this.setPickupItemStack(new ItemStack(Items.ARROW));
            }
        }
    }

    private double calculateDotProduct(Vec3 vec1, Vec3 vec2) {
        return vec1.normalize().dot(vec2.normalize());
    }

    private void handleNormalState(int stateTickCounter) {
        if (targetEntity == null || !targetEntity.isAlive() || targetEntity.distanceTo(this) > 20.0) {
            targetEntity = this.findNearestEntity();
        }

        if (targetEntity != null) {
            Vec3 arrowPosition = this.position();
            Vec3 targetPosition = targetEntity.getBoundingBox().getCenter();
            Vec3 directionToTarget = targetPosition.subtract(arrowPosition).normalize();
            Vec3 arrowDirection = this.getDeltaMovement().normalize();

            double dotProduct = calculateDotProduct(arrowDirection, directionToTarget);
            double distanceToTarget = this.position().distanceTo(targetPosition);

            if (dotProduct >= 0.85 || distanceToTarget < 1.0) {
                // If the arrow is already on a relatively close course or very close to the target, adjust course immediately
                this.adjustCourseTowards(targetEntity);
            } else {
                // Perform the "stop and turn" action
                setData(ARROW_STATE, ArrowState.SLOWING_DOWN.ordinal());
                setData(STATE_TICK_COUNTER, 0);
            }
        }
    }

    private void handleSlowingDownState(int stateTickCounter) {
        if (stateTickCounter < SLOW_DOWN_DURATION) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5)); // Slow down gradually
        } else {
            setData(ARROW_STATE, ArrowState.STOPPED_AND_ROTATING.ordinal());
            setData(STATE_TICK_COUNTER, 0);
        }
    }

    private void handleStoppedAndRotatingState(int stateTickCounter) {
        this.setDeltaMovement(Vec3.ZERO); // Stop the arrow

        if (targetEntity != null && stateTickCounter != 0) {
            Vec3 arrowPosition = this.position();
            Vec3 targetCenterPosition = targetEntity.getBoundingBox().getCenter();
            Vec3 direction = targetCenterPosition.subtract(arrowPosition).normalize();
            // Gradually rotate towards the target
            double dx = direction.x;
            double dy = direction.y;
            double dz = direction.z;
            double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
            float targetYaw = (float) (Math.atan2(dx, dz) * (180D / Math.PI));
            float targetPitch = (float) (Math.atan2(dy, horizontalDistance) * (180D / Math.PI));

            float currentYaw = this.getYRot();
            float currentPitch = this.getXRot();

            float yawDifference = wrapDegrees(targetYaw - currentYaw);
            float newYaw = currentYaw + yawDifference * 0.3f; // Smooth rotation factor

            float pitchDifference = wrapDegrees(targetPitch - currentPitch);
            float newPitch = currentPitch + pitchDifference * 0.3f; // Smooth rotation factor

            this.yRotO = currentYaw;
            this.xRotO = currentPitch;

            this.setYRot(newYaw); // Smooth rotation
            this.setXRot(newPitch);
        }

        if (stateTickCounter >= STOP_DURATION) {
            setData(ARROW_STATE, ArrowState.RESUMING_FLIGHT.ordinal());
            setData(STATE_TICK_COUNTER, 0);
            if (targetEntity != null) {
                this.adjustCourseTowards(targetEntity);
                if (this.getOwner() instanceof Player player)
                    this.level().playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.ENDER_DRAGON_FLAP, SoundSource.PLAYERS, 1.0F, 0.5F);
            }
            this.setDeltaMovement(this.getDeltaMovement().scale(0.25));
        }
    }

    private void handleResumingFlightState(int stateTickCounter) {
        if (this.getDeltaMovement().length() < getOriginalVelocity()) { //Cap the speed to the original velocity
            this.setDeltaMovement(this.getDeltaMovement().scale(1.5)); // Gradually speed up
        }
        if (this.getDeltaMovement().length() > getOriginalVelocity()) { //Cap the speed to the original velocity
            this.setDeltaMovement(this.getDeltaMovement().normalize().scale(getOriginalVelocity()));
        }
        if (targetEntity != null) {
            this.adjustCourseTowards(targetEntity);
        }
    }

    @Override
    public void setYRot(float yRot) {
        if (yRot == 0f && getDeltaMovement().equals(Vec3.ZERO)) return;
        if (isPhase()) {
            Vec3 delta = getDeltaMovement();
            if (yRot == (float) (Mth.atan2(-delta.x, -delta.z) * 180.0F / (float) Math.PI))
                return;
        }
        super.setYRot(yRot);
    }

    @Override
    public void setXRot(float xRot) {
        if (xRot == 0f && getDeltaMovement().equals(Vec3.ZERO)) return;
        super.setXRot(xRot);
    }

    private float wrapDegrees(float degrees) {
        degrees = degrees % 360.0F;
        if (degrees >= 180.0F) {
            degrees -= 360.0F;
        }
        if (degrees < -180.0F) {
            degrees += 360.0F;
        }
        return degrees;
    }

    @Nullable
    private LivingEntity findNearestEntity() {
        double radius = searchRadius();
        AABB searchArea = this.getBoundingBox().inflate(radius, radius / 2, radius);
        List<Mob> entities = this.level().getEntitiesOfClass(Mob.class, searchArea);
        LivingEntity nearestEntity = null;
        double nearestDistance = Double.MAX_VALUE;

        for (Mob entity : entities) {
            if (entity == this.getOwner() || !entity.isAlive() || wasAlreadyHit(entity)) {
                continue;
            }
            double distance = this.distanceToSqr(entity);
            if (distance < nearestDistance) {
                nearestEntity = entity;
                nearestDistance = distance;
            }
        }

        return nearestEntity;
    }

    private boolean wasAlreadyHit(LivingEntity target) {
        if (!isEpic())
            return false;
        if (this.piercingIgnoreEntityIds == null)
            return false;
        return this.piercingIgnoreEntityIds.contains(target.getId());
    }

    private void adjustCourseTowards(LivingEntity target) {
        Vec3 arrowPosition = this.position();
        Vec3 targetCenterPosition = target.getBoundingBox().getCenter();
        Vec3 direction = targetCenterPosition.subtract(arrowPosition).normalize();

        // Update the arrow's motion
        if (this.getDeltaMovement().equals(Vec3.ZERO))
            this.setDeltaMovement(direction.scale(0.1f));
        else
            this.setDeltaMovement(direction.scale(this.getDeltaMovement().length()));

        // Update the arrow's rotation to point towards the target
        double dx = direction.x;
        double dy = direction.y;
        double dz = direction.z;
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        this.setYRot((float) (Math.atan2(dx, dz) * (180D / Math.PI)));
        this.setXRot((float) (Math.atan2(dy, horizontalDistance) * (180D / Math.PI)));

        // Ensure the previous rotation values are updated to avoid visual glitches
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    private void makeParticle(int particleAmount) {
        int i = this.getColor();
        if (i != -1 && particleAmount > 0) {
            for (int j = 0; j < particleAmount; j++) {
                this.level()
                        .addParticle(
                                ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, i),
                                this.getRandomX(0.5),
                                this.getRandomY(),
                                this.getRandomZ(0.5),
                                0.0,
                                0.0,
                                0.0
                        );
            }
        }
    }

    public int getColor() {
        return this.entityData.get(ID_EFFECT_COLOR);
    }

    @Override
    protected void onHit(HitResult result) {
        HitResult.Type hitresult$type = result.getType();
        if (isPhase() && hitresult$type == HitResult.Type.BLOCK)
            return;
        super.onHit(result);
        PotionContents potioncontents = getPotionContents();
        if (potioncontents.is(Potions.WATER)) {
            this.applyWater();
        } else if (potioncontents.hasEffects()) {
            if (this.entityData.get(IS_LINGERING)) {
                this.makeAreaOfEffectCloud(potioncontents);
            }
            if (this.entityData.get(IS_SPLASH)) {
                this.applySplash(
                        potioncontents.getAllEffects(), result.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) result).getEntity() : null
                );
                int i = potioncontents.potion().isPresent() && potioncontents.potion().get().value().hasInstantEffects() ? 2007 : 2002;
                this.level().levelEvent(i, this.blockPosition(), potioncontents.getColor());
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
    }

    public boolean isCurrentlyGlowing() {
        return isPhase();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (isEpic()) {
            setData(ARROW_STATE, ArrowState.STOPPED_AND_ROTATING.ordinal());
            setData(STATE_TICK_COUNTER, 0);
        }
    }

    private void applyWater() {
        AABB aabb = this.getBoundingBox().inflate(4.0, 2.0, 4.0);

        for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, aabb, WATER_SENSITIVE_OR_ON_FIRE)) {
            double d0 = this.distanceToSqr(livingentity);
            if (d0 < 16.0) {
                if (livingentity.isSensitiveToWater()) {
                    livingentity.hurt(this.damageSources().indirectMagic(this, this.getOwner()), 1.0F);
                }

                if (livingentity.isOnFire() && livingentity.isAlive()) {
                    livingentity.extinguishFire();
                }
            }
        }

        for (Axolotl axolotl : this.level().getEntitiesOfClass(Axolotl.class, aabb)) {
            axolotl.rehydrate();
        }
    }

    private void applySplash(Iterable<MobEffectInstance> p_330815_, @Nullable Entity p_37549_) {
        AABB aabb = this.getBoundingBox().inflate(4.0, 2.0, 4.0);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, aabb);
        if (!list.isEmpty()) {
            Entity entity = this.getEffectSource();

            for (LivingEntity livingentity : list) {
                if (livingentity.isAffectedByPotions()) {
                    double d0 = this.distanceToSqr(livingentity);
                    if (d0 < 16.0) {
                        double d1;
                        if (livingentity == p_37549_) {
                            d1 = 1.0;
                        } else {
                            d1 = 1.0 - Math.sqrt(d0) / 4.0;
                        }

                        for (MobEffectInstance mobeffectinstance : p_330815_) {
                            Holder<MobEffect> holder = mobeffectinstance.getEffect();
                            if (holder.value().getCategory() == MobEffectCategory.HARMFUL && getOwner() != null && livingentity.is(getOwner()))
                                continue;
                            if (holder.value().getCategory() == MobEffectCategory.BENEFICIAL && getOwner() != null && !livingentity.is(getOwner()))
                                continue;
                            if (holder.value().isInstantenous()) {
                                holder.value().applyInstantenousEffect(this, this.getOwner(), livingentity, mobeffectinstance.getAmplifier(), d1);
                            } else {
                                int i = mobeffectinstance.mapDuration(p_267930_ -> (int) (d1 * (double) p_267930_ + 0.5));
                                MobEffectInstance mobeffectinstance1 = new MobEffectInstance(
                                        holder, i, mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()
                                );
                                if (!mobeffectinstance1.endsWithin(20)) {
                                    livingentity.addEffect(mobeffectinstance1, entity);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void makeAreaOfEffectCloud(PotionContents p_332124_) {
        JustDireAreaEffectCloud areaeffectcloud = new JustDireAreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
        if (this.getOwner() instanceof LivingEntity livingentity) {
            areaeffectcloud.setOwner(livingentity);
        }

        areaeffectcloud.setRadius(3.0F);
        areaeffectcloud.setRadiusOnUse(-0.1F);
        areaeffectcloud.setWaitTime(10);
        areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());
        areaeffectcloud.setPotionContents(p_332124_);
        this.level().addFreshEntity(areaeffectcloud);
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        super.doPostHurtEffects(living);
        if (!this.entityData.get(IS_POTIONARROW))
            return;
        Entity entity = this.getEffectSource();
        PotionContents potioncontents = this.getPotionContents();
        if (potioncontents.potion().isPresent()) {
            for (MobEffectInstance mobeffectinstance : potioncontents.potion().get().value().getEffects()) {
                if (mobeffectinstance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL && getOwner() != null && living.is(getOwner()))
                    continue;
                if (mobeffectinstance.getEffect().value().getCategory() == MobEffectCategory.BENEFICIAL && getOwner() != null && !living.is(getOwner()))
                    continue;
                living.addEffect(
                        new MobEffectInstance(
                                mobeffectinstance.getEffect(),
                                Math.max(mobeffectinstance.mapDuration(p_268168_ -> p_268168_ / 2), 1),
                                mobeffectinstance.getAmplifier(),
                                mobeffectinstance.isAmbient(),
                                mobeffectinstance.isVisible()
                        ),
                        entity
                );
            }
        }

        for (MobEffectInstance mobeffectinstance1 : potioncontents.customEffects()) {
            living.addEffect(mobeffectinstance1, entity);
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.ARROW);
    }

    /**
     * Handles an entity event received from a {@link net.minecraft.network.protocol.game.ClientboundEntityEventPacket}.
     */
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 0) {
            int i = this.getColor();
            if (i != -1) {
                float f = (float) (i >> 16 & 0xFF) / 255.0F;
                float f1 = (float) (i >> 8 & 0xFF) / 255.0F;
                float f2 = (float) (i >> 0 & 0xFF) / 255.0F;

                for (int j = 0; j < 20; j++) {
                    this.level()
                            .addParticle(
                                    ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, f, f1, f2),
                                    this.getRandomX(0.5),
                                    this.getRandomY(),
                                    this.getRandomZ(0.5),
                                    0.0,
                                    0.0,
                                    0.0
                            );
                }
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("is_potionarrow", this.entityData.get(IS_POTIONARROW));
        pCompound.putBoolean("is_splash", this.entityData.get(IS_SPLASH));
        pCompound.putBoolean("is_lingering", this.entityData.get(IS_LINGERING));
        pCompound.putBoolean("is_homing", this.entityData.get(IS_HOMING));
        pCompound.putInt("arrow_state", this.entityData.get(ARROW_STATE));
        pCompound.putInt("state_tick_counter", this.entityData.get(STATE_TICK_COUNTER));
        pCompound.putFloat("original_velocity", this.entityData.get(ORIGINAL_VELOCITY));
        pCompound.putBoolean("is_epic_arrow", this.entityData.get(IS_EPIC_ARROW));
        pCompound.putBoolean("is_phase", this.entityData.get(IS_PHASE));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.entityData.set(IS_POTIONARROW, pCompound.getBoolean("is_potionarrow"));
        this.entityData.set(IS_SPLASH, pCompound.getBoolean("is_splash"));
        this.entityData.set(IS_LINGERING, pCompound.getBoolean("is_lingering"));
        this.entityData.set(IS_HOMING, pCompound.getBoolean("is_homing"));
        this.entityData.set(ARROW_STATE, pCompound.getInt("arrow_state"));
        this.entityData.set(STATE_TICK_COUNTER, pCompound.getInt("state_tick_counter"));
        this.entityData.set(ORIGINAL_VELOCITY, pCompound.getFloat("original_velocity"));
        this.entityData.set(IS_EPIC_ARROW, pCompound.getBoolean("is_epic_arrow"));
        this.entityData.set(IS_PHASE, pCompound.getBoolean("is_phase"));
    }
}
