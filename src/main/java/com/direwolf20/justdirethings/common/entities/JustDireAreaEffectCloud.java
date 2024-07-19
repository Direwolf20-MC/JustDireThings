package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.setup.Registration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JustDireAreaEffectCloud extends Entity implements TraceableEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int TIME_BETWEEN_APPLICATIONS = 5;
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(JustDireAreaEffectCloud.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_WAITING = SynchedEntityData.defineId(JustDireAreaEffectCloud.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ParticleOptions> DATA_PARTICLE = SynchedEntityData.defineId(JustDireAreaEffectCloud.class, EntityDataSerializers.PARTICLE);
    private static final float MAX_RADIUS = 32.0F;
    private static final float MINIMAL_RADIUS = 0.5F;
    private static final float DEFAULT_RADIUS = 3.0F;
    public static final float DEFAULT_WIDTH = 6.0F;
    public static final float HEIGHT = 0.5F;
    private PotionContents potionContents = PotionContents.EMPTY;
    private final Map<Entity, Integer> victims = Maps.newHashMap();
    private int duration = 600;
    private int waitTime = 20;
    private int reapplicationDelay = 20;
    private int durationOnUse;
    private float radiusOnUse;
    private float radiusPerTick;
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;

    public JustDireAreaEffectCloud(EntityType<? extends JustDireAreaEffectCloud> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public JustDireAreaEffectCloud(Level level, double x, double y, double z) {
        this(Registration.JustDireAreaEffectCloud.get(), level);
        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder p_326332_) {
        p_326332_.define(DATA_RADIUS, 3.0F);
        p_326332_.define(DATA_WAITING, false);
        p_326332_.define(DATA_PARTICLE, ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, -1));
    }

    public void setRadius(float radius) {
        if (!this.level().isClientSide) {
            this.getEntityData().set(DATA_RADIUS, Mth.clamp(radius, 0.0F, 32.0F));
        }
    }

    @Override
    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    public float getRadius() {
        return this.getEntityData().get(DATA_RADIUS);
    }

    public void setPotionContents(PotionContents p_330869_) {
        this.potionContents = p_330869_;
        this.updateColor();
    }

    private void updateColor() {
        ParticleOptions particleoptions = this.entityData.get(DATA_PARTICLE);
        if (particleoptions instanceof ColorParticleOption colorparticleoption) {
            int i = this.potionContents.equals(PotionContents.EMPTY) ? 0 : this.potionContents.getColor();
            this.entityData.set(DATA_PARTICLE, ColorParticleOption.create(colorparticleoption.getType(), FastColor.ARGB32.opaque(i)));
        }
    }

    public void addEffect(MobEffectInstance effectInstance) {
        this.setPotionContents(this.potionContents.withEffectAdded(effectInstance));
    }

    public ParticleOptions getParticle() {
        return this.getEntityData().get(DATA_PARTICLE);
    }

    public void setParticle(ParticleOptions particleOption) {
        this.getEntityData().set(DATA_PARTICLE, particleOption);
    }

    /**
     * Sets if the cloud is waiting. While waiting, the radius is ignored and the cloud shows fewer particles in its area.
     */
    protected void setWaiting(boolean waiting) {
        this.getEntityData().set(DATA_WAITING, waiting);
    }

    public boolean isWaiting() {
        return this.getEntityData().get(DATA_WAITING);
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void tick() {
        super.tick();
        boolean flag = this.isWaiting();
        float f = this.getRadius();
        if (this.level().isClientSide) {
            if (flag && this.random.nextBoolean()) {
                return;
            }

            ParticleOptions particleoptions = this.getParticle();
            int i;
            float f1;
            if (flag) {
                i = 2;
                f1 = 0.2F;
            } else {
                i = Mth.ceil((float) Math.PI * f * f);
                f1 = f;
            }

            for (int j = 0; j < i; j++) {
                float f2 = this.random.nextFloat() * (float) (Math.PI * 2);
                float f3 = Mth.sqrt(this.random.nextFloat()) * f1;
                double d0 = this.getX() + (double) (Mth.cos(f2) * f3);
                double d2 = this.getY();
                double d4 = this.getZ() + (double) (Mth.sin(f2) * f3);
                if (particleoptions.getType() == ParticleTypes.ENTITY_EFFECT) {
                    if (flag && this.random.nextBoolean()) {
                        this.level().addAlwaysVisibleParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, -1), d0, d2, d4, 0.0, 0.0, 0.0);
                    } else {
                        this.level().addAlwaysVisibleParticle(particleoptions, d0, d2, d4, 0.0, 0.0, 0.0);
                    }
                } else if (flag) {
                    this.level().addAlwaysVisibleParticle(particleoptions, d0, d2, d4, 0.0, 0.0, 0.0);
                } else {
                    this.level()
                            .addAlwaysVisibleParticle(
                                    particleoptions, d0, d2, d4, (0.5 - this.random.nextDouble()) * 0.15, 0.01F, (0.5 - this.random.nextDouble()) * 0.15
                            );
                }
            }
        } else {
            if (this.tickCount >= this.waitTime + this.duration) {
                this.discard();
                return;
            }

            boolean flag1 = this.tickCount < this.waitTime;
            if (flag != flag1) {
                this.setWaiting(flag1);
            }

            if (flag1) {
                return;
            }

            if (this.radiusPerTick != 0.0F) {
                f += this.radiusPerTick;
                if (f < 0.5F) {
                    this.discard();
                    return;
                }

                this.setRadius(f);
            }

            if (this.tickCount % 5 == 0) {
                this.victims.entrySet().removeIf(p_287380_ -> this.tickCount >= p_287380_.getValue());
                if (!this.potionContents.hasEffects()) {
                    this.victims.clear();
                } else {
                    List<MobEffectInstance> list = Lists.newArrayList();
                    if (this.potionContents.potion().isPresent()) {
                        for (MobEffectInstance mobeffectinstance1 : this.potionContents.potion().get().value().getEffects()) {
                            list.add(
                                    new MobEffectInstance(
                                            mobeffectinstance1.getEffect(),
                                            mobeffectinstance1.mapDuration(p_267926_ -> p_267926_ / 4),
                                            mobeffectinstance1.getAmplifier(),
                                            mobeffectinstance1.isAmbient(),
                                            mobeffectinstance1.isVisible()
                                    )
                            );
                        }
                    }

                    list.addAll(this.potionContents.customEffects());
                    List<LivingEntity> list1 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
                    if (!list1.isEmpty()) {
                        for (LivingEntity livingentity : list1) {
                            if (!this.victims.containsKey(livingentity)
                                    && livingentity.isAffectedByPotions()
                                    && !list.stream().noneMatch(livingentity::canBeAffected)) {
                                double d5 = livingentity.getX() - this.getX();
                                double d1 = livingentity.getZ() - this.getZ();
                                double d3 = d5 * d5 + d1 * d1;
                                if (d3 <= (double) (f * f)) {
                                    this.victims.put(livingentity, this.tickCount + this.reapplicationDelay);
                                    boolean affectedAnything = false;
                                    for (MobEffectInstance mobeffectinstance : list) {
                                        if (mobeffectinstance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL && getOwner() != null && livingentity.is(getOwner()))
                                            continue;
                                        if (mobeffectinstance.getEffect().value().getCategory() == MobEffectCategory.BENEFICIAL && getOwner() != null && !livingentity.is(getOwner()))
                                            continue;
                                        if (mobeffectinstance.getEffect().value().isInstantenous()) {
                                            mobeffectinstance.getEffect()
                                                    .value()
                                                    .applyInstantenousEffect(this, this.getOwner(), livingentity, mobeffectinstance.getAmplifier(), 0.5);
                                        } else {
                                            livingentity.addEffect(new MobEffectInstance(mobeffectinstance), this);
                                        }
                                        affectedAnything = true;
                                    }

                                    if (this.radiusOnUse != 0.0F && affectedAnything) {
                                        f += this.radiusOnUse;
                                        if (f < 0.5F) {
                                            this.discard();
                                            return;
                                        }

                                        this.setRadius(f);
                                    }

                                    if (this.durationOnUse != 0) {
                                        this.duration = this.duration + this.durationOnUse;
                                        if (this.duration <= 0) {
                                            this.discard();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public float getRadiusOnUse() {
        return this.radiusOnUse;
    }

    public void setRadiusOnUse(float radiusOnUse) {
        this.radiusOnUse = radiusOnUse;
    }

    public float getRadiusPerTick() {
        return this.radiusPerTick;
    }

    public void setRadiusPerTick(float radiusPerTick) {
        this.radiusPerTick = radiusPerTick;
    }

    public int getDurationOnUse() {
        return this.durationOnUse;
    }

    public void setDurationOnUse(int durationOnUse) {
        this.durationOnUse = durationOnUse;
    }

    public int getWaitTime() {
        return this.waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUUID = owner == null ? null : owner.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.level()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
            }
        }

        return this.owner;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.tickCount = compound.getInt("Age");
        this.duration = compound.getInt("Duration");
        this.waitTime = compound.getInt("WaitTime");
        this.reapplicationDelay = compound.getInt("ReapplicationDelay");
        this.durationOnUse = compound.getInt("DurationOnUse");
        this.radiusOnUse = compound.getFloat("RadiusOnUse");
        this.radiusPerTick = compound.getFloat("RadiusPerTick");
        this.setRadius(compound.getFloat("Radius"));
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
        }

        RegistryOps<Tag> registryops = this.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        if (compound.contains("Particle", 10)) {
            ParticleTypes.CODEC
                    .parse(registryops, compound.get("Particle"))
                    .resultOrPartial(p_329991_ -> LOGGER.warn("Failed to parse area effect cloud particle options: '{}'", p_329991_))
                    .ifPresent(this::setParticle);
        }

        if (compound.contains("potion_contents")) {
            PotionContents.CODEC
                    .parse(registryops, compound.get("potion_contents"))
                    .resultOrPartial(p_340707_ -> LOGGER.warn("Failed to parse area effect cloud potions: '{}'", p_340707_))
                    .ifPresent(this::setPotionContents);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Age", this.tickCount);
        compound.putInt("Duration", this.duration);
        compound.putInt("WaitTime", this.waitTime);
        compound.putInt("ReapplicationDelay", this.reapplicationDelay);
        compound.putInt("DurationOnUse", this.durationOnUse);
        compound.putFloat("RadiusOnUse", this.radiusOnUse);
        compound.putFloat("RadiusPerTick", this.radiusPerTick);
        compound.putFloat("Radius", this.getRadius());
        RegistryOps<Tag> registryops = this.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        compound.put("Particle", ParticleTypes.CODEC.encodeStart(registryops, this.getParticle()).getOrThrow());
        if (this.ownerUUID != null) {
            compound.putUUID("Owner", this.ownerUUID);
        }

        if (!this.potionContents.equals(PotionContents.EMPTY)) {
            Tag tag = PotionContents.CODEC.encodeStart(registryops, this.potionContents).getOrThrow();
            compound.put("potion_contents", tag);
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_RADIUS.equals(key)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(key);
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 0.5F);
    }
}
