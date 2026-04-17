package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class JustDireItemTags extends ItemTagsProvider {
    public JustDireItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider, JustDireThings.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.AUTO_SMELT_DENY);
        tag(ModTags.Items.AUTO_SMOKE_DENY);
        tag(ModTags.Items.FUEL_CANISTER_DENY)
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
        tag(ModTags.Items.WRENCHES)
                .add(Registration.FerricoreWrench.get());
        tag(ModTags.Items.TOOLS_WRENCH)
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
        tag(ItemTags.BOW_ENCHANTABLE)
                .add(Registration.FerricoreBow.get())
                .add(Registration.BlazegoldBow.get())
                .add(Registration.CelestigemBow.get())
                .add(Registration.EclipseAlloyBow.get());
        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .add(Registration.FerricoreBow.get())
                .add(Registration.BlazegoldBow.get())
                .add(Registration.CelestigemBow.get())
                .add(Registration.EclipseAlloyBow.get());
        tag(ModTags.Items.RAW_FERRICORE)
                .add(Registration.RawFerricore.get());
        tag(ModTags.Items.RAW_BLAZEGOLD)
                .add(Registration.RawBlazegold.get());
        tag(ModTags.Items.RAW_ECLIPSEALLOY)
                .add(Registration.RawEclipseAlloy.get());
        tag(ModTags.Items.INGOT_FERRICORE)
                .add(Registration.FerricoreIngot.get());
        tag(ModTags.Items.INGOT_BLAZEGOLD)
                .add(Registration.BlazegoldIngot.get());
        tag(ModTags.Items.INGOT_ECLIPSEALLOY)
                .add(Registration.EclipseAlloyIngot.get());
        tag(ModTags.Items.GOO_REVIVE_TIER_1)
                .add(Items.SUGAR)
                .add(Items.ROTTEN_FLESH);
        tag(ModTags.Items.GOO_REVIVE_TIER_2)
                .add(Items.NETHER_WART)
                .add(Items.BLAZE_POWDER);
        tag(ModTags.Items.GOO_REVIVE_TIER_3)
                .add(Items.CHORUS_FRUIT)
                .add(Items.ENDER_PEARL);
        tag(ModTags.Items.GOO_REVIVE_TIER_4)
                .add(Items.SCULK)
                .add(Items.SCULK_CATALYST);
        tag(ModTags.Items.BOWS)
                .add(Registration.FerricoreBow.get())
                .add(Registration.BlazegoldBow.get())
                .add(Registration.CelestigemPickaxe.get())
                .add(Registration.EclipseAlloyBow.get());
        tag(ModTags.Items.RANGED_WEAPON)
                .add(Registration.FerricoreBow.get())
                .add(Registration.BlazegoldBow.get())
                .add(Registration.CelestigemPickaxe.get())
                .add(Registration.EclipseAlloyBow.get());
        tag(ModTags.Items.MELEE_WEAPON)
                .add(Registration.FerricoreSword.get())
                .add(Registration.FerricoreAxe.get())
                .add(Registration.BlazegoldSword.get())
                .add(Registration.BlazegoldAxe.get())
                .add(Registration.CelestigemSword.get())
                .add(Registration.CelestigemAxe.get())
                .add(Registration.EclipseAlloySword.get())
                .add(Registration.EclipseAlloyAxe.get())
                .add(Registration.CelestigemPaxel.get())
                .add(Registration.EclipseAlloyPaxel.get());
        tag(ModTags.Items.MINING_TOOL)
                .add(Registration.CelestigemPaxel.get())
                .add(Registration.EclipseAlloyPaxel.get());
        tag(ModTags.Items.PAXEL)
                .add(Registration.CelestigemPaxel.get())
                .add(Registration.EclipseAlloyPaxel.get());
        tag(ItemTags.CLUSTER_MAX_HARVESTABLES)
                .add(Registration.FerricorePickaxe.get())
                .add(Registration.BlazegoldPickaxe.get())
                .add(Registration.CelestigemPickaxe.get())
                .add(Registration.EclipseAlloyPickaxe.get())
                .add(Registration.CelestigemPaxel.get())
                .add(Registration.EclipseAlloyPaxel.get());
        tag(ModTags.Items.PARADOX_DENY)
                .add(Items.BEDROCK);
        tag(ModTags.Items.STORAGEBLOCKS)
                .add(Registration.CharcoalBlock_ITEM.get());
        tag(ModTags.Items.CHARCOALBLOCKS)
                .add(Registration.CharcoalBlock_ITEM.get());

        tag(ModTags.Items.GOO_RECIPE_TIER_1)
                .add(Registration.GooBlock_Tier1_ITEM.get())
                .addOptionalTag(ModTags.Items.GOO_RECIPE_TIER_2);

        tag(ModTags.Items.GOO_RECIPE_TIER_2)
                .add(Registration.GooBlock_Tier2_ITEM.get())
                .addOptionalTag(ModTags.Items.GOO_RECIPE_TIER_3);

        tag(ModTags.Items.GOO_RECIPE_TIER_3)
                .add(Registration.GooBlock_Tier3_ITEM.get())
                .addOptionalTag(ModTags.Items.GOO_RECIPE_TIER_4);

        tag(ModTags.Items.GOO_RECIPE_TIER_4)
                .add(Registration.GooBlock_Tier4_ITEM.get());
    }
}
