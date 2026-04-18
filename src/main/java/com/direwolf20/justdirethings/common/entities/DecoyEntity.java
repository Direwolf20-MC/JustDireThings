package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.common.items.interfaces.AbilityMethods;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DecoyEntity extends LivingEntity {
    // Stored as the UUID's string form, so it can ride the standard STRING entity-data serializer.
    // "" means no owner.
    private static final EntityDataAccessor<String> PLAYER_UUID = SynchedEntityData.defineId(DecoyEntity.class, EntityDataSerializers.STRING);

    public DecoyEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    public DecoyEntity(Level level) {
        this(JDTRegistration.DecoyEntity.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0F)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.ARMOR, 0.0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            if (tickCount % 10 == 0)
                aggroMobs();
            if (tickCount >= 200)
                this.remove(RemovalReason.DISCARDED);
        }
    }

    public void aggroMobs() {
        int radius = 10;
        AABB aabb = new AABB(getX() - radius, getY() - radius, getZ() - radius,
                getX() + radius, getY() + radius, getZ() + radius);

        List<Mob> aggroList = new ArrayList<>(level().getEntitiesOfClass(Mob.class, aabb, AbilityMethods::isValidStompEntity));

        for (Mob mob : aggroList) {
            mob.setTarget(this);
        }
    }

    public void setSummonerName(String playerName) {
        this.setCustomName(Component.literal(playerName + "_").append(Component.translatable("justdirethings.decoy")));
    }

    public Optional<UUID> getOwnerUUID() {
        String raw = this.entityData.get(PLAYER_UUID);
        if (raw.isEmpty()) return Optional.empty();
        try {
            return Optional.of(UUID.fromString(raw));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public void setOwnerUUID(UUID uuid) {
        this.entityData.set(PLAYER_UUID, uuid.toString());
    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    @Override
    public boolean isInvulnerableTo(ServerLevel level, DamageSource damageSource) {
        return !damageSource.is(DamageTypes.GENERIC_KILL);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PLAYER_UUID, "");
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.entityData.set(PLAYER_UUID, input.read("player_uuid", UUIDUtil.CODEC).map(UUID::toString).orElse(""));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        getOwnerUUID().ifPresent(u -> output.store("player_uuid", UUIDUtil.CODEC, u));
    }
}
