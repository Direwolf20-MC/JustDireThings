package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.common.items.CreatureCatcher;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.direwolf20.justdirethings.util.ModTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.entity.PartEntity;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import static com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents.ENTITIYTYPE;

public class CreatureCatcherEntity extends ThrowableItemProjectile {
    private static final EntityDataAccessor<Boolean> HAS_HIT = SynchedEntityData.defineId(CreatureCatcherEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CAPTURING = SynchedEntityData.defineId(CreatureCatcherEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SHRINKING_TIME = SynchedEntityData.defineId(CreatureCatcherEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<ItemStack> RETURN_ITEM_STACK = SynchedEntityData.defineId(CreatureCatcherEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Vector3fc> ENTITY_POSITION = SynchedEntityData.defineId(CreatureCatcherEntity.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Float> ENTITY_BODY_ROT = SynchedEntityData.defineId(CreatureCatcherEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ENTITY_HEAD_ROT = SynchedEntityData.defineId(CreatureCatcherEntity.class, EntityDataSerializers.FLOAT);

    private Mob hitEntity;
    public int renderTick;

    public CreatureCatcherEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HAS_HIT, false);
        builder.define(CAPTURING, false);
        builder.define(SHRINKING_TIME, 0);
        builder.define(RETURN_ITEM_STACK, ItemStack.EMPTY);
        builder.define(ENTITY_POSITION, new Vector3f(0, 0, 0));
        builder.define(ENTITY_BODY_ROT, 0f);
        builder.define(ENTITY_HEAD_ROT, 0f);

    }

    public CreatureCatcherEntity(Level pLevel, LivingEntity pShooter, ItemStack pItemStack) {
        super(JDTRegistration.CreatureCatcherEntity.get(), pShooter, pLevel, pItemStack);
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

    public Vector3fc getMobPosition() {
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
        return JDTRegistration.CreatureCatcher.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (!hasHitEntity()) return;
        if (!level().isClientSide()) {
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
        if (level().isClientSide() || CreatureCatcher.hasEntity(itemStack)) return;
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
        if (level().isClientSide()) return;
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
        TagValueOutput entityOutput = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, entity.registryAccess());
        entity.save(entityOutput);
        ItemStack itemStack = new ItemStack(getDefaultItem());
        itemStack.set(ENTITIYTYPE, EntityType.getKey(entity.getType()).toString());
        itemStack.set(DataComponents.ENTITY_DATA, TypedEntityData.of(entity.getType(), entityOutput.buildResult()));
        return itemStack;
    }

    protected void releaseEntity(ItemStack itemStack) {
        Entity entity = getEntityFromItemStack(itemStack, this.level());
        Vec3 location = getPosition(0);
        entity.snapTo(location);
        level().addFreshEntity(entity);
    }

    public static Mob getEntityFromItemStack(ItemStack itemStack, Level level) {
        if (!itemStack.has(ENTITIYTYPE)) return null;
        EntityType<?> type = EntityType.byString(itemStack.get(ENTITIYTYPE)).orElse(null);
        if (type == null) return null;
        Entity entity = type.create(level, net.minecraft.world.entity.EntitySpawnReason.LOAD);
        if (!(entity instanceof Mob)) return null;
        net.minecraft.nbt.CompoundTag tag = itemStack.has(DataComponents.ENTITY_DATA)
                ? itemStack.get(DataComponents.ENTITY_DATA).copyTagWithoutId()
                : new net.minecraft.nbt.CompoundTag();
        ValueInput input = TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), tag);
        entity.load(input);
        return (Mob) entity;
    }

    protected boolean canCapture(Entity entity) {
        if (!entity.isAlive())
            return false;
        if (entity.isMultipartEntity())
            return false;
        if (entity instanceof PartEntity<?>)
            return false;
        if (entity.getType().builtInRegistryHolder().is(Tags.EntityTypes.CAPTURING_NOT_SUPPORTED))
            return false;
        if (entity.getType().builtInRegistryHolder().is(ModTags.Entities.CREATURE_CATCHER_DENY))
            return false;
        if (!entity.save(TagValueOutput.createWithContext(ProblemReporter.DISCARDING, entity.registryAccess())))
            return false;
        return true;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("HasHitEntity", this.entityData.get(HAS_HIT));
        output.putBoolean("Capturing", this.entityData.get(CAPTURING));
        output.putInt("ShrinkingTime", this.entityData.get(SHRINKING_TIME));
        output.store("ReturnItemStack", ItemStack.CODEC, this.entityData.get(RETURN_ITEM_STACK));
        output.putFloat("EntityPosX", this.entityData.get(ENTITY_POSITION).x());
        output.putFloat("EntityPosY", this.entityData.get(ENTITY_POSITION).y());
        output.putFloat("EntityPosZ", this.entityData.get(ENTITY_POSITION).z());
        output.putFloat("EntityBodyRot", this.entityData.get(ENTITY_BODY_ROT));
        output.putFloat("EntityHeadRot", this.entityData.get(ENTITY_HEAD_ROT));
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.entityData.set(HAS_HIT, input.getBooleanOr("HasHitEntity", false));
        this.entityData.set(CAPTURING, input.getBooleanOr("Capturing", false));
        this.entityData.set(SHRINKING_TIME, input.getIntOr("ShrinkingTime", 0));
        ItemStack stack = input.read("ReturnItemStack", ItemStack.CODEC).orElseGet(() -> new ItemStack(this.getDefaultItem()));
        this.entityData.set(RETURN_ITEM_STACK, stack);
        Vector3f position = new Vector3f(
                input.getFloatOr("EntityPosX", 0.0F),
                input.getFloatOr("EntityPosY", 0.0F),
                input.getFloatOr("EntityPosZ", 0.0F)
        );
        this.entityData.set(ENTITY_POSITION, position);
        this.entityData.set(ENTITY_BODY_ROT, input.getFloatOr("EntityBodyRot", 0.0F));
        this.entityData.set(ENTITY_HEAD_ROT, input.getFloatOr("EntityHeadRot", 0.0F));
    }
}
