package com.direwolf20.justdirethings.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class ItemStackKey {
    public final Item item;
    public final CompoundTag nbt;
    private final int hash;


    public ItemStackKey(ItemStack stack, boolean compareNBT) {
        this.item = stack.getItem();
        this.nbt = compareNBT ? stack.getTag() : new CompoundTag();
        this.hash = Objects.hash(item, nbt);
    }

    public ItemStack getStack() {
        return new ItemStack(item, 1, nbt);
    }

    public ItemStack getStack(int amt) {
        return new ItemStack(item, amt, nbt);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemStackKey) {
            return (((ItemStackKey) obj).item == this.item) && Objects.equals(((ItemStackKey) obj).nbt, this.nbt);
        }
        return false;
    }
}
