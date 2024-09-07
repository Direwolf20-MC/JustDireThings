package com.direwolf20.justdirethings.common.blockentities;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.setup.Registration;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class InventoryHolderBE extends BaseMachineBE {
    public final Map<Integer, ItemStack> savedItemStacks = new Int2ObjectOpenHashMap<>();
    public InventoryHolderBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        MACHINE_SLOTS = 41; //Hotbar, Inventory, Armor, and Offhand
    }

    public InventoryHolderBE(BlockPos pPos, BlockState pBlockState) {
        this(Registration.InventoryHolderBE.get(), pPos, pBlockState);
    }

    public void addSavedItem(int slot) {
        ItemStack itemStack = getMachineHandler().getStackInSlot(slot).copy();
        if (itemStack.isEmpty())
            savedItemStacks.remove(slot);
        else
            savedItemStacks.put(slot, itemStack);
        markDirtyClient();
    }

    @Override
    public boolean isDefaultSettings() {
        return true;
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        // Create a new CompoundTag to hold all saved items
        CompoundTag savedItemsTag = new CompoundTag();

        // Loop through savedItemStacks and save each item
        for (Map.Entry<Integer, ItemStack> entry : savedItemStacks.entrySet()) {
            if (entry.getValue().isEmpty()) continue;
            CompoundTag itemTag = new CompoundTag();
            savedItemsTag.put(String.valueOf(entry.getKey()), entry.getValue().save(provider, itemTag)); // Save it under the slot index as the key
        }

        // Add this CompoundTag to the main tag
        tag.put("SavedItemStacks", savedItemsTag);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        // Clear the savedItemStacks map to avoid duplicates
        savedItemStacks.clear();

        // Check if the tag contains saved items
        if (tag.contains("SavedItemStacks", 10)) { // 10 is the ID for CompoundTag
            CompoundTag savedItemsTag = tag.getCompound("SavedItemStacks");

            // Loop through each key (slot index) in the saved items tag
            for (String key : savedItemsTag.getAllKeys()) {
                int slotIndex = Integer.parseInt(key);
                CompoundTag itemTag = savedItemsTag.getCompound(key);
                ItemStack stack = ItemStack.parse(provider, itemTag).orElse(ItemStack.EMPTY); // Load the ItemStack from the tag
                if (!stack.isEmpty())
                    savedItemStacks.put(slotIndex, stack); // Put it back into the map
            }
        }
    }
}
