package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.common.items.CreatureCatcher;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class CreatureCatcherEntity extends ThrowableItemProjectile {
    public CreatureCatcherEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public CreatureCatcherEntity(Level pLevel, LivingEntity pShooter) {
        super(Registration.CreatureCatcherEntity.get(), pShooter, pLevel);
    }

    @Override
    protected Item getDefaultItem() {
        return Registration.CreatureCatcher.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        ItemStack itemStack = getItem();
        if (level().isClientSide || CreatureCatcher.hasEntity(itemStack)) return;
        Entity entity = pResult.getEntity();
        if (entity instanceof Mob mob) {
            ItemEntity itemEntity = createItemEntity(mob);
            this.level().addFreshEntity(itemEntity);
            if (CreatureCatcher.hasEntity(itemEntity.getItem())) {
                entity.remove(RemovalReason.DISCARDED);
            }
            this.remove(RemovalReason.DISCARDED);
        }
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if (level().isClientSide) return;
        super.onHitBlock(pResult);
        ItemStack itemStack = getItem();
        if (CreatureCatcher.hasEntity(itemStack)) {
            Entity entity = releaseEntity(itemStack);
            Vec3 location = Vec3.atCenterOf(pResult.getBlockPos().relative(pResult.getDirection()));
            entity.moveTo(location);
            itemStack.setTag(new CompoundTag());
            level().addFreshEntity(entity);
        }
        ItemEntity itemEntity = createItemEntity();
        this.level().addFreshEntity(itemEntity);
        this.remove(RemovalReason.DISCARDED);
    }

    protected ItemEntity createItemEntity() {
        ItemEntity itemEntity = new ItemEntity(this.level(),
                this.getX(),
                this.getY(),
                this.getZ(),
                new ItemStack(getDefaultItem()));
        itemEntity.setPickUpDelay(40);
        return itemEntity;
    }

    protected ItemEntity createItemEntity(Mob entity) {
        ItemEntity itemEntity = createItemEntity();
        if (!entity.canChangeDimensions() || !entity.isAlive() || !canCapture(entity)) return itemEntity;
        itemEntity.setPos(entity.position());
        CompoundTag tag = new CompoundTag();
        tag.putString("entityType", EntityType.getKey(entity.getType()).toString());
        CompoundTag entityData = new CompoundTag();
        entity.saveWithoutId(entityData);
        tag.put("entityData", entityData);
        itemEntity.getItem().setTag(tag);
        return itemEntity;
    }

    protected Entity releaseEntity(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (tag.isEmpty() || !tag.contains("entityType")) return null;
        EntityType<?> type = EntityType.byString(tag.getString("entityType")).orElse(null);
        if (type == null) return null;
        Entity entity = type.create(this.level());
        if (entity == null) return null;
        entity.load(tag.getCompound("entityData"));
        return entity;
    }

    protected boolean canCapture(Mob entity) {
        //Todo wither/dragons/tags?
        return true;
    }
}
