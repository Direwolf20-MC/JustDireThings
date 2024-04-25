package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.common.items.CreatureCatcher;
import com.direwolf20.justdirethings.datagen.JustDireEntityTags;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.neoforged.neoforge.entity.PartEntity;
import org.joml.Vector3f;

public class CreatureCatcherEntity extends ThrowableItemProjectile {
    private static final EntityDataAccessor<Boolean> HAS_HIT = SynchedEntityData.defineId(CreatureCatcherEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CAPTURING = SynchedEntityData.defineId(CreatureCatcherEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SHRINKING_TIME = SynchedEntityData.defineId(CreatureCatcherEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<ItemStack> RETURN_ITEM_STACK = SynchedEntityData.defineId(CreatureCatcherEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Vector3f> ENTITY_POSITION = SynchedEntityData.defineId(CreatureCatcherEntity.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Float> ENTITY_BODY_ROT = SynchedEntityData.defineId(CreatureCatcherEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ENTITY_HEAD_ROT = SynchedEntityData.defineId(CreatureCatcherEntity.class, EntityDataSerializers.FLOAT);

    private Mob hitEntity;
    public int renderTick;

    public CreatureCatcherEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(HAS_HIT, false);
        this.getEntityData().define(CAPTURING, false);
        this.getEntityData().define(SHRINKING_TIME, 0);
        this.getEntityData().define(RETURN_ITEM_STACK, ItemStack.EMPTY);
        this.getEntityData().define(ENTITY_POSITION, new Vector3f(0, 0, 0));
        this.getEntityData().define(ENTITY_BODY_ROT, 0f);
        this.getEntityData().define(ENTITY_HEAD_ROT, 0f);

    }

    public CreatureCatcherEntity(Level pLevel, LivingEntity pShooter) {
        super(Registration.CreatureCatcherEntity.get(), pShooter, pLevel);
    }

    public boolean hasHitEntity() {
        return this.entityData.get(HAS_HIT);
    }

    public boolean isCapturing() {
        return this.entityData.get(CAPTURING);
    }

    public ItemStack getReturnStack() {
        return this.entityData.get(RETURN_ITEM_STACK);
    }

    public int shrinkingTime() {
        return this.entityData.get(SHRINKING_TIME);
    }

    public void incrementShrinkTime() {
        this.entityData.set(SHRINKING_TIME, this.entityData.get(SHRINKING_TIME) + 1);
    }

    public Vector3f getMobPosition() {
        return this.entityData.get(ENTITY_POSITION);
    }

    public float getBodyRot() {
        return this.entityData.get(ENTITY_BODY_ROT);
    }

    public int getAnimationTicks() {
        return 20;
    }

    public float getHeadRot() {
        return this.entityData.get(ENTITY_HEAD_ROT);
    }

    @Override
    protected Item getDefaultItem() {
        return Registration.CreatureCatcher.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (!hasHitEntity()) return;
        if (!level().isClientSide) {
            if (hitEntity != null && !hitEntity.isDeadOrDying() && shrinkingTime() > 1) {
                if (CreatureCatcher.hasEntity(getReturnStack())) {
                    hitEntity.remove(RemovalReason.DISCARDED);
                }
            }
            incrementShrinkTime();
            if (shrinkingTime() > getAnimationTicks() + 1) {
                if (isCapturing()) { //Capturing
                    this.level().addFreshEntity(createItemEntity(getReturnStack()));
                    this.remove(RemovalReason.DISCARDED);
                } else { //Releasing
                    releaseEntity(getItem());
                    spawnNewItemEntity();
                    this.remove(RemovalReason.DISCARDED);
                }
            }
        } else {
            renderTick++;
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        ItemStack itemStack = getItem();
        if (level().isClientSide || CreatureCatcher.hasEntity(itemStack)) return;
        Entity entity = pResult.getEntity();
        if (entity instanceof Mob mob && canCapture(mob)) {
            this.entityData.set(HAS_HIT, true);
            this.entityData.set(CAPTURING, true);
            this.entityData.set(ENTITY_POSITION, new Vector3f((float) mob.position().x, (float) mob.position().y, (float) mob.position().z));
            this.entityData.set(ENTITY_BODY_ROT, mob.yBodyRot);
            this.entityData.set(ENTITY_HEAD_ROT, mob.yHeadRot);
            setDeltaMovement(new Vec3(0, 0, 0));
            setNoGravity(true);
            captureMob(mob);
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
            this.entityData.set(HAS_HIT, true);
            setDeltaMovement(new Vec3(0, 0, 0));
            setNoGravity(true);
        } else {
            spawnNewItemEntity();
        }
    }

    protected void captureMob(Mob hitEntity) {
        if (hitEntity == null) return;
        this.entityData.set(RETURN_ITEM_STACK, createItemStack(hitEntity));
        this.hitEntity = hitEntity;
    }

    protected void spawnNewItemEntity() {
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
        itemEntity.setPickUpDelay(10);
        return itemEntity;
    }

    protected ItemEntity createItemEntity(ItemStack itemStack) {
        ItemEntity itemEntity = createItemEntity();
        itemEntity.setItem(itemStack);
        return itemEntity;
    }

    protected ItemStack createItemStack(Mob entity) {
        CompoundTag tag = new CompoundTag();
        tag.putString("entityType", EntityType.getKey(entity.getType()).toString());
        CompoundTag entityData = new CompoundTag();
        entity.saveWithoutId(entityData);
        tag.put("entityData", entityData);
        ItemStack itemStack = new ItemStack(getDefaultItem());
        itemStack.setTag(tag);
        return itemStack;
    }

    protected void releaseEntity(ItemStack itemStack) {
        Entity entity = getEntityFromItemStack(itemStack, this.level());
        Vec3 location = getPosition(0);
        entity.moveTo(location);
        itemStack.setTag(new CompoundTag());
        level().addFreshEntity(entity);
    }

    public static Mob getEntityFromItemStack(ItemStack itemStack, Level level) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (tag.isEmpty() || !tag.contains("entityType")) return null;
        EntityType<?> type = EntityType.byString(tag.getString("entityType")).orElse(null);
        if (type == null) return null;
        Entity entity = type.create(level);
        if (!(entity instanceof Mob)) return null;
        entity.load(tag.getCompound("entityData"));
        return (Mob) entity;
    }

    protected boolean canCapture(Entity entity) {
        if (!entity.isAlive())
            return false;
        if (entity.isMultipartEntity())
            return false;
        if (entity instanceof PartEntity<?>)
            return false;
        if (entity.getType().is(JustDireEntityTags.CREATURE_CATCHER_DENY))
            return false;
        return true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("HasHitEntity", this.entityData.get(HAS_HIT));
        pCompound.putBoolean("Capturing", this.entityData.get(CAPTURING));
        pCompound.putInt("ShrinkingTime", this.entityData.get(SHRINKING_TIME));
        pCompound.put("ReturnItemStack", this.entityData.get(RETURN_ITEM_STACK).save(new CompoundTag()));
        pCompound.putFloat("EntityPosX", this.entityData.get(ENTITY_POSITION).x());
        pCompound.putFloat("EntityPosY", this.entityData.get(ENTITY_POSITION).y());
        pCompound.putFloat("EntityPosZ", this.entityData.get(ENTITY_POSITION).z());
        pCompound.putFloat("EntityBodyRot", this.entityData.get(ENTITY_BODY_ROT));
        pCompound.putFloat("EntityHeadRot", this.entityData.get(ENTITY_HEAD_ROT));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.entityData.set(HAS_HIT, pCompound.getBoolean("HasHitEntity"));
        this.entityData.set(CAPTURING, pCompound.getBoolean("Capturing"));
        this.entityData.set(SHRINKING_TIME, pCompound.getInt("ShrinkingTime"));
        ItemStack stack = ItemStack.of(pCompound.getCompound("ReturnItemStack"));
        this.entityData.set(RETURN_ITEM_STACK, stack);
        Vector3f position = new Vector3f(
                pCompound.getFloat("EntityPosX"),
                pCompound.getFloat("EntityPosY"),
                pCompound.getFloat("EntityPosZ")
        );
        this.entityData.set(ENTITY_POSITION, position);
        this.entityData.set(ENTITY_BODY_ROT, pCompound.getFloat("EntityBodyRot"));
        this.entityData.set(ENTITY_HEAD_ROT, pCompound.getFloat("EntityHeadRot"));
    }
}
