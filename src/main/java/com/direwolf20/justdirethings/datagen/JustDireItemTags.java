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

    public static final TagKey<Item> WRENCHES = forgeTag("wrenches");
    public static final TagKey<Item> TOOLS_WRENCH = forgeTag("tools/wrench");

    private static TagKey<Item> forgeTag(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
    }

    public static final TagKey<Item> FUEL_CANISTER_DENY = ItemTags.create(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "deny_fuel_canister"));
    public static final TagKey<Item> AUTO_SMELT_DENY = ItemTags.create(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "auto_smelt_deny"));
    public static final TagKey<Item> AUTO_SMOKE_DENY = ItemTags.create(ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "auto_smoke_deny"));

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(AUTO_SMELT_DENY);
        tag(AUTO_SMOKE_DENY);
        tag(FUEL_CANISTER_DENY)
                .add(Items.LAVA_BUCKET);
        tag(ItemTags.SWORDS)
                .add(Registration.FerricoreSword.get())
                .add(Registration.BlazegoldSword.get())
                .add(Registration.CelestigemSword.get())
                .add(Registration.EclipseAlloySword.get());
        tag(ItemTags.PICKAXES)
                .add(Registration.FerricorePickaxe.get())
                .add(Registration.BlazegoldPickaxe.get())
                .add(Registration.CelestigemPickaxe.get())
                .add(Registration.EclipseAlloyPickaxe.get())
                .add(Registration.CelestigemPaxel.get())
                .add(Registration.EclipseAlloyPaxel.get());
        tag(ItemTags.SHOVELS)
                .add(Registration.FerricoreShovel.get())
                .add(Registration.BlazegoldShovel.get())
                .add(Registration.CelestigemShovel.get())
                .add(Registration.EclipseAlloyShovel.get())
                .add(Registration.CelestigemPaxel.get())
                .add(Registration.EclipseAlloyPaxel.get());
        tag(ItemTags.AXES)
                .add(Registration.FerricoreAxe.get())
                .add(Registration.BlazegoldAxe.get())
                .add(Registration.CelestigemAxe.get())
                .add(Registration.EclipseAlloyAxe.get())
                .add(Registration.CelestigemPaxel.get())
                .add(Registration.EclipseAlloyPaxel.get());
        tag(ItemTags.HOES)
                .add(Registration.FerricoreHoe.get())
                .add(Registration.BlazegoldHoe.get())
                .add(Registration.CelestigemHoe.get())
                .add(Registration.EclipseAlloyHoe.get());
        tag(Tags.Items.INGOTS)
                .add(Registration.FerricoreIngot.get())
                .add(Registration.BlazegoldIngot.get())
                .add(Registration.EclipseAlloyIngot.get());
        tag(Tags.Items.RAW_MATERIALS)
                .add(Registration.RawFerricore.get())
                .add(Registration.RawBlazegold.get())
                .add(Registration.RawEclipseAlloy.get());
        tag(Tags.Items.GEMS)
                .add(Registration.Celestigem.get());
        tag(WRENCHES)
                .add(Registration.FerricoreWrench.get());
        tag(TOOLS_WRENCH)
                .add(Registration.FerricoreWrench.get());
        tag(Tags.Items.STORAGE_BLOCKS)
                .add(Registration.FerricoreBlock_ITEM.get())
                .add(Registration.BlazeGoldBlock_ITEM.get())
                .add(Registration.CelestigemBlock_ITEM.get())
                .add(Registration.EclipseAlloyBlock_ITEM.get());
        tag(ItemTags.FOOT_ARMOR)
                .add(Registration.FerricoreBoots.get())
                .add(Registration.BlazegoldBoots.get())
                .add(Registration.CelestigemBoots.get())
                .add(Registration.EclipseAlloyBoots.get());
        tag(ItemTags.LEG_ARMOR)
                .add(Registration.FerricoreLeggings.get())
                .add(Registration.BlazegoldLeggings.get())
                .add(Registration.CelestigemLeggings.get())
                .add(Registration.EclipseAlloyLeggings.get());
        tag(ItemTags.CHEST_ARMOR)
                .add(Registration.FerricoreChestplate.get())
                .add(Registration.BlazegoldChestplate.get())
                .add(Registration.CelestigemChestplate.get())
                .add(Registration.EclipseAlloyChestplate.get());
        tag(ItemTags.HEAD_ARMOR)
                .add(Registration.FerricoreHelmet.get())
                .add(Registration.BlazegoldHelmet.get())
                .add(Registration.CelestigemHelmet.get())
                .add(Registration.EclipseAlloyHelmet.get());
        tag(ItemTags.FOOT_ARMOR_ENCHANTABLE)
                .add(Registration.FerricoreBoots.get())
                .add(Registration.BlazegoldBoots.get())
                .add(Registration.CelestigemBoots.get())
                .add(Registration.EclipseAlloyBoots.get());
        tag(ItemTags.LEG_ARMOR_ENCHANTABLE)
                .add(Registration.FerricoreLeggings.get())
                .add(Registration.BlazegoldLeggings.get())
                .add(Registration.CelestigemLeggings.get())
                .add(Registration.EclipseAlloyLeggings.get());
        tag(ItemTags.CHEST_ARMOR_ENCHANTABLE)
                .add(Registration.FerricoreChestplate.get())
                .add(Registration.BlazegoldChestplate.get())
                .add(Registration.CelestigemChestplate.get())
                .add(Registration.EclipseAlloyChestplate.get());
        tag(ItemTags.HEAD_ARMOR_ENCHANTABLE)
                .add(Registration.FerricoreHelmet.get())
                .add(Registration.BlazegoldHelmet.get())
                .add(Registration.CelestigemHelmet.get())
                .add(Registration.EclipseAlloyHelmet.get());
    }

    @Override
    public String getName() {
        return "JustDireThings Item Tags";
    }
}
