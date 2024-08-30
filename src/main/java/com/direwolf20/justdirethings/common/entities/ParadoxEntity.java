package com.direwolf20.justdirethings.common.entities;

import com.direwolf20.justdirethings.client.particles.paradoxparticle.ParadoxParticleData;
import com.direwolf20.justdirethings.datagen.JustDireBlockTags;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ParadoxEntity extends Entity {
    private static final EntityDataAccessor<Integer> REQUIRED_CONSUMPTION = SynchedEntityData.defineId(ParadoxEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CONSUMPTION = SynchedEntityData.defineId(ParadoxEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> RADIUS = SynchedEntityData.defineId(ParadoxEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> SHRINK_SCALE = SynchedEntityData.defineId(ParadoxEntity.class, EntityDataSerializers.FLOAT);

    private final Map<BlockPos, Integer> blocksToAbsorb = new HashMap<>();
    private int maxRadius = 5;
    private double itemSuckSpeed = 0.25;
    private boolean collapsing = false;

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
        this.maxRadius = 4;
        int currentRadius = getRadius();
        if (!level().isClientSide) {
            if (collapsing) {
                float scale = getShrinkScale() - 0.02f; // Decrease the scale over time
                setShrinkScale(Math.max(scale, 0.0f)); // Ensure scale doesn't go below 0
                if (getShrinkScale() <= 0.01f) {
                    this.discard(); // Remove the entity once fully shrunk
                }
                return;
            }

            handleBlockAbsorption(currentRadius);
            handleItemAbsorption(currentRadius);
        }
    }

    private void handleBlockAbsorption(int currentRadius) {
        if (this.tickCount % 20 == 0 && currentRadius < maxRadius) {
            currentRadius++;
            setRadius(currentRadius);
        }

        for (BlockPos pos : BlockPos.betweenClosed(getOnPos().offset(-currentRadius, -currentRadius, -currentRadius), getOnPos().offset(currentRadius, currentRadius, currentRadius))) {
            float rand = random.nextFloat();
            if (isBlockValid(pos) && rand < 0.0125f) {
                // Add block with a countdown between 40 and 80 ticks
                blocksToAbsorb.put(new BlockPos(pos), 40 + random.nextInt(41));
            }
        }

        Iterator<Map.Entry<BlockPos, Integer>> iterator = blocksToAbsorb.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, Integer> entry = iterator.next();
            BlockPos pos = entry.getKey();
            if (level().isEmptyBlock(pos)) {
                iterator.remove();
                continue;
            }
            int timeLeft = entry.getValue() - 1;

            Vec3 targetVec = position().add(0, 0.5, 0);
            ItemStack blockStack = new ItemStack(level().getBlockState(pos).getBlock());
            if (blockStack.equals(ItemStack.EMPTY) || blockStack.getItem().equals(Items.AIR))
                blockStack = new ItemStack(Items.STONE);
            ParadoxParticleData data = new ParadoxParticleData(blockStack, targetVec.x, targetVec.y, targetVec.z, 1, this.getUUID());

            ((ServerLevel) level()).sendParticles(data, pos.getX(), pos.getY(), pos.getZ(), 1, random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5, 0.1);

            // Update the countdown
            if (timeLeft <= 0) {
                for (int i = 0; i < 5; i++) {
                    ((ServerLevel) level()).sendParticles(data, pos.getX(), pos.getY(), pos.getZ(), 10, random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5, 0.1);
                }

                // Set the block to air and safely remove from the map
                level().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                iterator.remove();
            } else {
                entry.setValue(timeLeft);
            }
        }
    }

    private void handleItemAbsorption(int currentRadius) {
        List<ItemEntity> items = level().getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(currentRadius + 0.25f));

        for (ItemEntity item : items) {
            Vec3 itemPosition = item.position();
            Vec3 direction = position().subtract(itemPosition).normalize().scale(itemSuckSpeed);
            item.setNoGravity(true);
            // Apply the calculated velocity to the item
            item.setDeltaMovement(direction);

            // Check if the item is close enough to be voided
            if (position().closerThan(item.position(), 0.125)) {
                ItemStack itemStack = item.getItem();
                if (itemStack.is(Registration.TimeCrystal.get()))
                    collapse();
                item.discard(); // Remove the item from the world
            }
        }
    }

    private void collapse() {
        collapsing = true;
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.WITHER_AMBIENT, SoundSource.HOSTILE, 1.0f, 0.25f);
    }

    public boolean isBlockValid(BlockPos blockPos) {
        BlockState blockState = level().getBlockState(blockPos);
        if (blockState.isAir())
            return false;
        if (blockState.is(JustDireBlockTags.PARADOX_ABSORB_DENY))
            return false;
        if (blocksToAbsorb.containsKey(blockPos))
            return false;
        if (blockState.getDestroySpeed(level(), blockPos) < 0)
            return false;
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(REQUIRED_CONSUMPTION, 100);
        builder.define(CONSUMPTION, 0);
        builder.define(RADIUS, 0);
        builder.define(SHRINK_SCALE, 1.0f);
    }

    public int getRadius() {
        return this.entityData.get(RADIUS);
    }

    public void setRadius(int radius) {
        this.entityData.set(RADIUS, radius);
    }

    public float getShrinkScale() {
        return this.entityData.get(SHRINK_SCALE);
    }

    public void setShrinkScale(float scale) {
        this.entityData.set(SHRINK_SCALE, scale);
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
