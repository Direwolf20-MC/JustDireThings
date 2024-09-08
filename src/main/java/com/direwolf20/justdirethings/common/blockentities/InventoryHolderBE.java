package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.capabilities.InventoryHolderItemHandler;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.ItemStackKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryHolderBE extends BaseMachineBE {
    public FilterBasicHandler filterBasicHandler = new FilterBasicHandler(41);
    public Map<ItemStackKey, List<Integer>> filteredCache = new HashMap<>();
    public boolean compareNBT = false;
    public boolean filtersOnly = false;
    public boolean automatedFiltersOnly = false;
    public boolean compareCounts = false;
    public boolean automatedCompareCounts = false;
    public InventoryHolderBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 41; //Hotbar, Inventory, Armor, and Offhand
    }

    public InventoryHolderBE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.InventoryHolderBE.get(), pPos, pBlockState);
    }

    public void addSavedItem(int slot) {
        ItemStack itemStack = getMachineHandler().getStackInSlot(slot).copy();
        filterBasicHandler.setStackInSlot(slot, itemStack);
        rebuildFilterCache();
        markDirtyClient();
    }

    public void saveSettings(boolean compareNBT, boolean filtersOnly, boolean compareCounts, boolean automatedFiltersOnly, boolean automatedCompareCounts) {
        this.compareNBT = compareNBT;
        this.filtersOnly = filtersOnly;
        this.compareCounts = compareCounts;
        this.automatedFiltersOnly = automatedFiltersOnly;
        this.automatedCompareCounts = automatedCompareCounts;
        markDirtyClient();
    }

    public void rebuildFilterCache() {
        filteredCache.clear();
        for (int i = 0; i < filterBasicHandler.getSlots(); i++) {
            ItemStack stack = filterBasicHandler.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            ItemStackKey key = new ItemStackKey(stack, compareNBT);
            List<Integer> slotList = filteredCache.getOrDefault(key, new ArrayList<>());
            slotList.add(i);
            filteredCache.put(key, slotList);
        }
    }

    public ItemStackHandler getInventoryHolderHandler() {
        return new InventoryHolderItemHandler(this, getMachineHandler());
    }

    public int allowedExtractAmount(int slot, int amount) {
        ItemStack stack = filterBasicHandler.getStackInSlot(slot);
        if (stack.isEmpty()) {
            return amount;
        }
        if (automatedCompareCounts) {
            int amountDesired = getSlotLimit(slot);
            int amountHad = getMachineHandler().getStackInSlot(slot).getCount();
            if (amountDesired > amountHad)
                return 0;
            return Math.min(amount, amountHad - amountDesired);
        }
        return 0;
    }

    public boolean isStackValidFilter(ItemStack testStack, int slot) {
        ItemStackKey key = new ItemStackKey(testStack, compareNBT);
        ItemStack stack = filterBasicHandler.getStackInSlot(slot);
        if (stack.isEmpty()) {
            return !automatedFiltersOnly;
        }
        return key.equals(new ItemStackKey(stack, compareNBT));
    }

    public int getSlotLimit(int slot) {
        if (!automatedCompareCounts)
            return -1;
        ItemStack stack = filterBasicHandler.getStackInSlot(slot);
        if (stack.isEmpty()) return -1;
        return stack.getCount();
    }

    @Override
    public boolean isDefaultSettings() {
        return true;
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putBoolean("compareNBT", compareNBT);
        tag.putBoolean("filtersOnly", filtersOnly);
        tag.putBoolean("compareCounts", compareCounts);
        tag.putBoolean("automatedFiltersOnly", automatedFiltersOnly);
        tag.putBoolean("automatedCompareCounts", automatedCompareCounts);
        // Create a new CompoundTag to hold all saved items
        tag.put("filteredItems", filterBasicHandler.serializeNBT(provider));
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        compareNBT = tag.getBoolean("compareNBT");
        filtersOnly = tag.getBoolean("filtersOnly");
        compareCounts = tag.getBoolean("compareCounts");
        automatedFiltersOnly = tag.getBoolean("automatedFiltersOnly");
        automatedCompareCounts = tag.getBoolean("automatedCompareCounts");
        if (tag.contains("filteredItems")) {
            CompoundTag filteredItems = tag.getCompound("filteredItems");
            filterBasicHandler.deserializeNBT(provider, filteredItems);
            rebuildFilterCache();
        }
    }
}
