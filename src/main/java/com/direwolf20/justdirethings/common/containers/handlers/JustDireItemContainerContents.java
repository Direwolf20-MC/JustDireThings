package com.direwolf20.justdirethings.common.containers.handlers;

import com.google.common.collect.Iterables;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Stream;

public final class JustDireItemContainerContents {
    private static final int NO_SLOT = -1;
    private static final int MAX_SIZE = 256;
    public static final JustDireItemContainerContents EMPTY = new JustDireItemContainerContents(NonNullList.create());
    public static final Codec<JustDireItemContainerContents> CODEC = JustDireItemContainerContents.Slot.CODEC
            .sizeLimitedListOf(256)
            .xmap(JustDireItemContainerContents::fromSlots, JustDireItemContainerContents::asSlots);
    public static final StreamCodec<RegistryFriendlyByteBuf, JustDireItemContainerContents> STREAM_CODEC = ItemStack.OPTIONAL_STREAM_CODEC
            .apply(ByteBufCodecs.list(256))
            .map(JustDireItemContainerContents::new, p_331691_ -> p_331691_.items);
    private final NonNullList<ItemStack> items;
    private final int hashCode;

    private JustDireItemContainerContents(NonNullList<ItemStack> p_332193_) {
        if (p_332193_.size() > 256) {
            throw new IllegalArgumentException("Got " + p_332193_.size() + " items, but maximum is 256");
        } else {
            this.items = p_332193_;
            this.hashCode = ItemStack.hashStackList(p_332193_);
        }
    }

    private JustDireItemContainerContents(int p_331689_) {
        this(NonNullList.withSize(p_331689_, ItemStack.EMPTY));
    }

    private JustDireItemContainerContents(List<ItemStack> p_331046_) {
        this(p_331046_.size());

        for (int i = 0; i < p_331046_.size(); i++) {
            this.items.set(i, p_331046_.get(i));
        }
    }

    private static JustDireItemContainerContents fromSlots(List<JustDireItemContainerContents.Slot> p_331424_) {
        OptionalInt optionalint = p_331424_.stream().mapToInt(JustDireItemContainerContents.Slot::index).max();
        if (optionalint.isEmpty()) {
            return EMPTY;
        } else {
            JustDireItemContainerContents itemcontainercontents = new JustDireItemContainerContents(optionalint.getAsInt() + 1);

            for (JustDireItemContainerContents.Slot itemcontainercontents$slot : p_331424_) {
                itemcontainercontents.items.set(itemcontainercontents$slot.index(), itemcontainercontents$slot.item());
            }

            return itemcontainercontents;
        }
    }

    public static JustDireItemContainerContents fromItems(List<ItemStack> stacks) {
        int i = stacks.size();
        JustDireItemContainerContents itemcontainercontents = new JustDireItemContainerContents(i);

        for (int j = 0; j < i; j++) {
            itemcontainercontents.items.set(j, stacks.get(j).copy());
        }

        return itemcontainercontents;

    }

    private static int findLastNonEmptySlot(List<ItemStack> p_340916_) {
        for (int i = p_340916_.size() - 1; i >= 0; i--) {
            if (!p_340916_.get(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    private List<JustDireItemContainerContents.Slot> asSlots() {
        List<JustDireItemContainerContents.Slot> list = new ArrayList<>();

        for (int i = 0; i < this.items.size(); i++) {
            ItemStack itemstack = this.items.get(i);
            if (!itemstack.isEmpty()) {
                list.add(new JustDireItemContainerContents.Slot(i, itemstack));
            }
        }

        return list;
    }

    public void copyInto(NonNullList<ItemStack> p_330513_) {
        for (int i = 0; i < p_330513_.size(); i++) {
            ItemStack itemstack = i < this.items.size() ? this.items.get(i) : ItemStack.EMPTY;
            p_330513_.set(i, itemstack.copy());
        }
    }

    public ItemStack copyOne() {
        return this.items.isEmpty() ? ItemStack.EMPTY : this.items.get(0).copy();
    }

    public Stream<ItemStack> stream() {
        return this.items.stream().map(ItemStack::copy);
    }

    public Stream<ItemStack> nonEmptyStream() {
        return this.items.stream().filter(p_331322_ -> !p_331322_.isEmpty()).map(ItemStack::copy);
    }

    public Iterable<ItemStack> nonEmptyItems() {
        return Iterables.filter(this.items, p_331420_ -> !p_331420_.isEmpty());
    }

    public Iterable<ItemStack> nonEmptyItemsCopy() {
        return Iterables.transform(this.nonEmptyItems(), ItemStack::copy);
    }

    @Override
    public boolean equals(Object p_331711_) {
        if (this == p_331711_) {
            return true;
        } else {
            if (p_331711_ instanceof JustDireItemContainerContents itemcontainercontents && ItemStack.listMatches(this.items, itemcontainercontents.items)) {
                return true;
            }

            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    static record Slot(int index, ItemStack item) {
        public static final Codec<JustDireItemContainerContents.Slot> CODEC = RecordCodecBuilder.create(
                p_331695_ -> p_331695_.group(
                                Codec.intRange(0, 255).fieldOf("slot").forGetter(JustDireItemContainerContents.Slot::index),
                                ItemStack.CODEC.fieldOf("item").forGetter(JustDireItemContainerContents.Slot::item)
                        )
                        .apply(p_331695_, JustDireItemContainerContents.Slot::new)
        );
    }
}
