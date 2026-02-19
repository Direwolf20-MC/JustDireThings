package com.direwolf20.justdirethings.common.blockentities.basebe;

import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.common.entities.CreatureCatcherEntity;
import com.direwolf20.justdirethings.common.items.CreatureCatcher;
import com.direwolf20.justdirethings.util.ItemStackKey;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface FilterableBE {
    FilterBasicHandler getFilterHandler();

    FilterData getFilterData();

    default void setFilterData(FilterData filterData) {
        FilterData existingData = getFilterData();
        existingData = filterData;
    }

    BlockEntity getBlockEntity();


    default void saveFilterSettings(CompoundTag tag) {
        tag.putBoolean("allowlist", getFilterData().allowlist);
        tag.putBoolean("compareNBT", getFilterData().compareNBT);
        tag.putInt("blockitemfilter", getFilterData().blockItemFilter);
    }

    default void loadFilterSettings(CompoundTag tag) {
        getFilterData().allowlist = tag.getBoolean("allowlist");
        getFilterData().compareNBT = tag.getBoolean("compareNBT");
        int blockItemFilter = tag.getInt("blockitemfilter");
        if (blockItemFilter != -1 && getFilterData().blockItemFilter != -1)
            getFilterData().blockItemFilter = blockItemFilter;
    }

    default void setFilterSettings(FilterData filterData) {
        getFilterData().allowlist = filterData.allowlist;
        getFilterData().compareNBT = filterData.compareNBT;
        getFilterData().blockItemFilter = filterData.blockItemFilter;
        if (getBlockEntity() instanceof BaseMachineBE baseMachineBE)
            baseMachineBE.markDirtyClient();
    }

    default boolean isStackValidFilter(ItemStack testStack) {
        ItemStackKey key = new ItemStackKey(testStack, getFilterData().compareNBT);
        if (getFilterData().filterCache.containsKey(key)) return getFilterData().filterCache.get(key);

        FilterBasicHandler filteredItems = getFilterHandler();
        for (int i = 0; i < filteredItems.getSlots(); i++) {
            ItemStack stack = filteredItems.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (key.equals(new ItemStackKey(stack, getFilterData().compareNBT))) {
                getFilterData().filterCache.put(key, getFilterData().allowlist);
                return getFilterData().allowlist;
            }
        }
        getFilterData().filterCache.put(key, !getFilterData().allowlist);
        return !getFilterData().allowlist;
    }

    default boolean isEntityValidFilter(Entity entity, Level level) {
        if (getFilterData().entityCache.containsKey(entity)) return getFilterData().entityCache.get(entity);

        FilterBasicHandler filteredItems = getFilterHandler();
        for (int i = 0; i < filteredItems.getSlots(); i++) {
            ItemStack stack = filteredItems.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof SpawnEggItem) {
                Item entityEgg = SpawnEggItem.byId(entity.getType());
                if (entityEgg == null) continue;
                if (stack.is(entityEgg)) {
                    getFilterData().entityCache.put(entity, getFilterData().allowlist);
                    return getFilterData().allowlist;
                }
            } else if (stack.getItem() instanceof CreatureCatcher creatureCatcher) {
                Mob mob = CreatureCatcherEntity.getEntityFromItemStack(stack, level);
                if (mob == null)
                    continue;
                if (entity.getType().equals(mob.getType())) {
                    if (getFilterData().compareNBT) {
                        CompoundTag filterTag = getNormalizedTag(mob);
                        CompoundTag targetTag = getNormalizedTag(entity);
                        if (filterTag.equals(targetTag)) {
                            getFilterData().entityCache.put(entity, getFilterData().allowlist);
                            return getFilterData().allowlist;
                        }
                    } else {
                        getFilterData().entityCache.put(entity, getFilterData().allowlist);
                        return getFilterData().allowlist;
                    }
                }
            }
        }

        if (entity instanceof ItemEntity itemEntity) {
            getFilterData().entityCache.put(entity, isStackValidFilter(itemEntity.getItem()));
            return getFilterData().entityCache.get(entity);
        }

        getFilterData().entityCache.put(entity, !getFilterData().allowlist);
        return !getFilterData().allowlist;
    }

    default CompoundTag getNormalizedTag(Entity entity) {
        CompoundTag tag = new CompoundTag();
        entity.save(tag);
        // Remove generic entity tags
        tag.remove("AbsorptionAmount");
        tag.remove("Age");
        tag.remove("Air");
        tag.remove("ArmorDropChances");
        tag.remove("ArmorItems");
        tag.remove("Brain");
        tag.remove("CanPickUpLoot");
        tag.remove("DeathTime");
        tag.remove("FallDistance");
        tag.remove("FallFlying");
        tag.remove("Fire");
        tag.remove("ForcedAge");
        tag.remove("HandDropChances");
        tag.remove("HandItems");
        tag.remove("HurtByTimestamp");
        tag.remove("HurtTime");
        tag.remove("InLove");
        tag.remove("Invulnerable");
        tag.remove("LeftHanded");
        tag.remove("Motion");
        tag.remove("OnGround");
        tag.remove("PersistenceRequired");
        tag.remove("PortalCooldown");
        tag.remove("Pos");
        tag.remove("Rotation");
        tag.remove("UUID");
        tag.remove("attributes");  // This may contain some relevant attributes, but is generally generic
        tag.remove("id");  // If you're not comparing the entity type

        // Additional generic tags
        tag.remove("NoAI");
        tag.remove("Silent");
        tag.remove("Glowing");
        tag.remove("Tags");
        tag.remove("Passengers");
        tag.remove("Leashed");
        tag.remove("Leash");
        tag.remove("CustomName");
        tag.remove("FireTicks");
        tag.remove("Dimension");
        tag.remove("HasVisualFire");
        tag.remove("ActiveEffects");

        //Special Cases
        tag.remove("FromBucket"); //Axolotls

        // If there are any additional mod-specific tags that don't affect sheep directly, you can remove them too:
        tag.remove("neoforge:spawn_type");  // Example of a mod-specific tag that might not be relevant


        return tag;
    }

    default boolean isStackValidFilter(LiquidBlock liquidBlock) {
        ItemStack testStack = new ItemStack(liquidBlock.fluid.getBucket());
        ItemStackKey key = new ItemStackKey(testStack, getFilterData().compareNBT);
        if (getFilterData().filterCache.containsKey(key)) return getFilterData().filterCache.get(key);

        FilterBasicHandler filteredItems = getFilterHandler();
        for (int i = 0; i < filteredItems.getSlots(); i++) {
            ItemStack stack = filteredItems.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (key.equals(new ItemStackKey(stack, getFilterData().compareNBT))) {
                getFilterData().filterCache.put(key, getFilterData().allowlist);
                return getFilterData().allowlist;
            }
        }
        getFilterData().filterCache.put(key, !getFilterData().allowlist);
        return !getFilterData().allowlist;
    }
}
