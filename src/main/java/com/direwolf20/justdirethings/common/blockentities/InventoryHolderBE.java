package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.common.containers.handlers.FilterBasicHandler;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class InventoryHolderBE extends BaseMachineBE {
    public FilterBasicHandler filterBasicHandler = new FilterBasicHandler(41);
    public boolean compareNBT = false;
    public boolean filtersOnly = false;
    public boolean compareCounts = false;
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
        markDirtyClient();
    }

    public void saveSettings(boolean compareNBT, boolean filtersOnly, boolean compareCounts) {
        this.compareNBT = compareNBT;
        this.filtersOnly = filtersOnly;
        this.compareCounts = compareCounts;
        markDirtyClient();
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
        // Create a new CompoundTag to hold all saved items
        tag.put("filteredItems", filterBasicHandler.serializeNBT(provider));
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        compareNBT = tag.getBoolean("compareNBT");
        filtersOnly = tag.getBoolean("filtersOnly");
        compareCounts = tag.getBoolean("compareCounts");
        if (tag.contains("filteredItems")) {
            CompoundTag filteredItems = tag.getCompound("filteredItems");
            filterBasicHandler.deserializeNBT(provider, filteredItems);
        }
    }
}
