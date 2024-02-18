package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ItemTags extends ItemTagsProvider {
    public ItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, ExistingFileHelper helper) {
        super(packOutput, lookupProvider, blockTags.contentsGetter(), JustDireThings.MODID, helper);
    }

    public static final TagKey<Item> FUEL_CANISTER_DENY = net.minecraft.tags.ItemTags.create(new ResourceLocation(JustDireThings.MODID, "deny_fuel_canister"));

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(FUEL_CANISTER_DENY)
                .add(Items.LAVA_BUCKET);
    }

    @Override
    public String getName() {
        return "JustDireThings Item Tags";
    }
}
