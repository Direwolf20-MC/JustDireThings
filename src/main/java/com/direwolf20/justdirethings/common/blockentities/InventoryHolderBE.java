package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.capabilities.InventoryHolderItemHandler;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.ItemStackKey;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

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
    public int renderedSlot = 27;
    public boolean renderPlayer = true;
    public InventoryHolderBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 41; //Hotbar, Inventory, Armor, and Offhand
    }

    public InventoryHolderBE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.InventoryHolderBE.get(), pPos, pBlockState);
    }

    public void addSavedItem(int slot) {
        ItemStacksResourceHandler machine = getMachineHandler();
        ItemResource resource = machine.getResource(slot);
        int count = machine.getAmountAsInt(slot);
        if (resource.isEmpty() || count == 0) {
            filterBasicHandler.set(slot, ItemResource.EMPTY, 0);
        } else {
            filterBasicHandler.set(slot, resource, count);
        }
        rebuildFilterCache();
        markDirtyClient();
    }

    public void saveSettings(boolean compareNBT, boolean filtersOnly, boolean compareCounts, boolean automatedFiltersOnly, boolean automatedCompareCounts, boolean renderPlayer, int renderedSlot) {
        this.compareNBT = compareNBT;
        this.filtersOnly = filtersOnly;
        this.compareCounts = compareCounts;
        this.automatedFiltersOnly = automatedFiltersOnly;
        this.automatedCompareCounts = automatedCompareCounts;
        this.renderPlayer = renderPlayer;
        this.renderedSlot = renderedSlot;
        markDirtyClient();
    }

    public void rebuildFilterCache() {
        filteredCache.clear();
        for (int i = 0; i < filterBasicHandler.size(); i++) {
            ItemStack stack = filterBasicHandler.getResource(i).toStack(filterBasicHandler.getAmountAsInt(i));
            if (stack.isEmpty()) continue;
            ItemStackKey key = new ItemStackKey(stack, compareNBT);
            List<Integer> slotList = filteredCache.getOrDefault(key, new ArrayList<>());
            slotList.add(i);
            filteredCache.put(key, slotList);
        }
    }

    public ResourceHandler<ItemResource> getInventoryHolderHandler() {
        return new InventoryHolderItemHandler(this, getMachineHandler());
    }

    public int allowedExtractAmount(int slot, int amount) {
        ItemStack stack = filterBasicHandler.getResource(slot).toStack(filterBasicHandler.getAmountAsInt(slot));
        if (stack.isEmpty()) {
            return amount;
        }
        if (automatedCompareCounts) {
            int amountDesired = getSlotLimit(slot);
            int amountHad = getMachineHandler().getAmountAsInt(slot);
            if (amountDesired > amountHad)
                return 0;
            return Math.min(amount, amountHad - amountDesired);
        }
        return 0;
    }

    public boolean isStackValidFilter(ItemStack testStack, int slot) {
        ItemStackKey key = new ItemStackKey(testStack, compareNBT);
        ItemStack stack = filterBasicHandler.getResource(slot).toStack(filterBasicHandler.getAmountAsInt(slot));
        if (stack.isEmpty()) {
            return !automatedFiltersOnly;
        }
        return key.equals(new ItemStackKey(stack, compareNBT));
    }

    public int getSlotLimit(int slot) {
        if (!automatedCompareCounts)
            return -1;
        ItemStack stack = filterBasicHandler.getResource(slot).toStack(filterBasicHandler.getAmountAsInt(slot));
        if (stack.isEmpty()) return -1;
        return stack.getCount();
    }

    @Override
    public boolean isDefaultSettings() {
        if (compareNBT)
            return false;
        if (filtersOnly)
            return false;
        if (automatedFiltersOnly)
            return false;
        if (compareCounts)
            return false;
        if (automatedCompareCounts)
            return false;
        if (renderedSlot != 27)
            return false;
        for (int i = 0; i < filterBasicHandler.size(); i++) {
            if (!filterBasicHandler.getResource(i).isEmpty())
                return false;
        }
        ItemStacksResourceHandler machine = getMachineHandler();
        for (int i = 0; i < machine.size(); i++) {
            if (!machine.getResource(i).isEmpty())
                return false;
        }
        return true;
    }

    public void saveInventory(ValueOutput output) {
        getMachineHandler().serialize(output.child("storedItems"));
    }

    public void loadInventory(ValueInput input) {
        input.child("storedItems").ifPresent(child -> {
            getMachineHandler().deserialize(child);
            rebuildFilterCache();
        });
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putBoolean("compareNBT", compareNBT);
        output.putBoolean("filtersOnly", filtersOnly);
        output.putBoolean("compareCounts", compareCounts);
        output.putBoolean("automatedFiltersOnly", automatedFiltersOnly);
        output.putBoolean("automatedCompareCounts", automatedCompareCounts);
        output.putBoolean("renderPlayer", renderPlayer);
        output.putInt("renderedSlot", renderedSlot);
        filterBasicHandler.serialize(output.child("filteredItems"));
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        compareNBT = input.getBooleanOr("compareNBT", compareNBT);
        filtersOnly = input.getBooleanOr("filtersOnly", filtersOnly);
        compareCounts = input.getBooleanOr("compareCounts", compareCounts);
        automatedFiltersOnly = input.getBooleanOr("automatedFiltersOnly", automatedFiltersOnly);
        automatedCompareCounts = input.getBooleanOr("automatedCompareCounts", automatedCompareCounts);
        renderPlayer = input.getBooleanOr("renderPlayer", renderPlayer);
        renderedSlot = input.getIntOr("renderedSlot", renderedSlot);
        input.child("filteredItems").ifPresent(child -> {
            filterBasicHandler.deserialize(child);
            rebuildFilterCache();
        });
    }
}
