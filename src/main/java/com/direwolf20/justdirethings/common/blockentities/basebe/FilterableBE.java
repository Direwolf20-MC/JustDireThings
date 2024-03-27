package com.direwolf20.justdirethings.common.blockentities.basebe;

import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.util.ItemStackKey;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
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
        getFilterData().blockItemFilter = tag.getInt("blockitemfilter");
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
}
