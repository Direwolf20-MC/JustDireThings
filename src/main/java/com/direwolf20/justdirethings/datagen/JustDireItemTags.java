package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class JustDireItemTags extends ItemTagsProvider {
    public JustDireItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, ExistingFileHelper helper) {
        super(packOutput, lookupProvider, blockTags.contentsGetter(), JustDireThings.MODID, helper);
    }

    public static final TagKey<Item> FUEL_CANISTER_DENY = ItemTags.create(new ResourceLocation(JustDireThings.MODID, "deny_fuel_canister"));

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(FUEL_CANISTER_DENY)
                .add(Items.LAVA_BUCKET);
        tag(ItemTags.SWORDS)
                .add(Registration.FerricoreSword.get())
                .add(Registration.BlazegoldSword.get());
        tag(ItemTags.PICKAXES)
                .add(Registration.FerricorePickaxe.get())
                .add(Registration.BlazegoldPickaxe.get());
        tag(ItemTags.SHOVELS)
                .add(Registration.FerricoreShovel.get())
                .add(Registration.BlazegoldShovel.get());
        tag(ItemTags.AXES)
                .add(Registration.FerricoreAxe.get())
                .add(Registration.BlazegoldAxe.get());
        tag(ItemTags.HOES)
                .add(Registration.FerricoreHoe.get())
                .add(Registration.BlazegoldHoe.get());
        tag(Tags.Items.INGOTS)
                .add(Registration.FerricoreIngot.get())
                .add(Registration.BlazegoldIngot.get());
        tag(Tags.Items.RAW_MATERIALS)
                .add(Registration.RawFerricore.get())
                .add(Registration.RawBlazegold.get());
        tag(Tags.Items.GEMS)
                .add(Registration.Celestigem.get());
    }

    @Override
    public String getName() {
        return "JustDireThings Item Tags";
    }
}
