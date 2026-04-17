package com.direwolf20.justdirethings.common.containers.handlers;

import com.direwolf20.justdirethings.common.items.FuelCanister;
import com.direwolf20.justdirethings.util.ModTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jetbrains.annotations.Nullable;

public class FuelCanisterHandler extends ItemStacksResourceHandler {
    public ItemStack stack;
    @Nullable
    private final FuelValues fuelValues;

    public FuelCanisterHandler(int size, ItemStack itemStack, @Nullable FuelValues fuelValues) {
        super(size);
        this.stack = itemStack;
        this.fuelValues = fuelValues;
    }

    @Override
    protected void onContentsChanged(int slot, ItemStack previousContents) {
        ItemStack fuelStack = getResource(slot).toStack(getAmountAsInt(slot));
        if (!stack.isEmpty() && !fuelStack.isEmpty() && fuelValues != null) {
            FuelCanister.incrementFuel(stack, fuelStack, fuelValues);
        }
    }

    @Override
    public boolean isValid(int slot, ItemResource resource) {
        if (resource.isEmpty()) return true;
        if (resource.getItem() instanceof FuelCanister) return false;
        ItemStack probe = resource.toStack();
        if (probe.is(ModTags.Items.FUEL_CANISTER_DENY)) return false;
        if (resource.getItem().getCraftingRemainder() != null) return false;
        // Client-side fallback: no FuelValues, permit; server enforces burn-time check.
        if (fuelValues == null) return true;
        return probe.getBurnTime(RecipeType.SMELTING, fuelValues) > 0;
    }

    @Override
    protected int getCapacity(int index, ItemResource resource) {
        return resource.isEmpty() ? Item.ABSOLUTE_MAX_STACK_SIZE
                : Math.min(resource.getMaxStackSize(), Item.ABSOLUTE_MAX_STACK_SIZE);
    }
}
