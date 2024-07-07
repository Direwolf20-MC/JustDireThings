package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.common.items.interfaces.AbilityMethods;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DecoyEntity extends LivingEntity {
    private static final EntityDataAccessor<Optional<UUID>> PLAYER_UUID = SynchedEntityData.defineId(DecoyEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    public DecoyEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    public DecoyEntity(Level level) {
        this(Registration.DecoyEntity.get(), level);
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
        if (!level().isClientSide) {
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
        return this.entityData.get(PLAYER_UUID);
    }

    public void setOwnerUUID(UUID uuid) {
        this.entityData.set(PLAYER_UUID, Optional.of(uuid));
    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return !damageSource.is(DamageTypes.GENERIC_KILL);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PLAYER_UUID, Optional.empty());
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return NonNullList.withSize(4, ItemStack.EMPTY); //TODO Validate
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        return;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("player_uuid"))
            this.entityData.set(PLAYER_UUID, Optional.of(compound.getUUID("player_uuid")));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.entityData.get(PLAYER_UUID).isPresent())
            compound.putUUID("player_uuid", this.entityData.get(PLAYER_UUID).get());
    }
}
