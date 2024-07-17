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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.world.entity.projectile.ThrownPotion.WATER_SENSITIVE_OR_ON_FIRE;

public class JustDireArrow extends AbstractArrow {
    private static final int EXPOSED_POTION_DECAY_TIME = 600;
    private static final int NO_EFFECT_COLOR = -1;
    private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR = SynchedEntityData.defineId(JustDireArrow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_POTIONARROW = SynchedEntityData.defineId(JustDireArrow.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SPLASH = SynchedEntityData.defineId(JustDireArrow.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_LINGERING = SynchedEntityData.defineId(JustDireArrow.class, EntityDataSerializers.BOOLEAN);
    private static final byte EVENT_POTION_PUFF = 0;

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

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder p_326324_) {
        super.defineSynchedData(p_326324_);
        p_326324_.define(ID_EFFECT_COLOR, -1);
        p_326324_.define(IS_POTIONARROW, false);
        p_326324_.define(IS_SPLASH, false);
        p_326324_.define(IS_LINGERING, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.makeParticle(1);
                }
            } else {
                this.makeParticle(2);
            }
        } else if (this.inGround && this.inGroundTime != 0 && !this.getPotionContents().equals(PotionContents.EMPTY) && this.inGroundTime >= 600) {
            this.level().broadcastEntityEvent(this, (byte) 0);
            this.setPickupItemStack(new ItemStack(Items.ARROW));
        }
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

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
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
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.entityData.set(IS_POTIONARROW, pCompound.getBoolean("is_potionarrow"));
        this.entityData.set(IS_SPLASH, pCompound.getBoolean("is_splash"));
        this.entityData.set(IS_LINGERING, pCompound.getBoolean("is_lingering"));
    }
}
