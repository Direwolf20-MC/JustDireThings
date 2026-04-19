package com.direwolf20.justdirethings.common.containers.handlers;

import com.direwolf20.justdirethings.common.items.FuelCanister;
import com.direwolf20.justdirethings.util.ModTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class FuelCanisterHandler extends ItemStacksResourceHandler {
    public ItemStack stack;
    private final FuelValues fuelValues;
    private boolean consuming;

    public FuelCanisterHandler(int size, ItemStack itemStack, FuelValues fuelValues) {
        super(size);
        this.stack = itemStack;
        this.fuelValues = fuelValues;
    }

    @Override
    protected void onContentsChanged(int slot, ItemStack previousContents) {
        if (consuming) return;
        ItemResource resource = getResource(slot);
        int amount = getAmountAsInt(slot);
        if (stack.isEmpty() || resource.isEmpty() || amount <= 0) return;
        ItemStack fuelStack = resource.toStack(amount);
        int consumed = FuelCanister.incrementFuel(stack, fuelStack, fuelValues);
        if (consumed <= 0) return;
        int remaining = amount - consumed;
        consuming = true;
        try {
            if (remaining <= 0) {
                set(slot, ItemResource.EMPTY, 0);
            } else {
                set(slot, resource, remaining);
            }
        } finally {
            consuming = false;
        }
    }

    @Override
    public boolean isValid(int slot, ItemResource resource) {
        if (resource.isEmpty()) return true;
        if (resource.getItem() instanceof FuelCanister) return false;
        ItemStack probe = resource.toStack();
        if (probe.is(ModTags.Items.FUEL_CANISTER_DENY)) return false;
        if (resource.getItem().getCraftingRemainder() != null) return false;
        return probe.getBurnTime(RecipeType.SMELTING, fuelValues) > 0;
    }

    @Override
    protected int getCapacity(int index, ItemResource resource) {
        return resource.isEmpty() ? Item.ABSOLUTE_MAX_STACK_SIZE
                : Math.min(resource.getMaxStackSize(), Item.ABSOLUTE_MAX_STACK_SIZE);
    }
}
