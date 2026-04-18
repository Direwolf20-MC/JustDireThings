package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.direwolf20.justdirethings.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class JustDireItemTags extends ItemTagsProvider {
    public JustDireItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider, JustDireThings.MODID);
    }

    private static TagKey<Item> toolRepairTag(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(JustDireThings.MODID, "repairs_" + name + "_tool"));
    }

    private static TagKey<Item> armorRepairTag(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(JustDireThings.MODID, "repairs_" + name + "_armor"));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.AUTO_SMELT_DENY);
        tag(ModTags.Items.AUTO_SMOKE_DENY);
        tag(ModTags.Items.FUEL_CANISTER_DENY)
                .add(Items.LAVA_BUCKET);
        tag(ItemTags.SWORDS)
                .add(JDTRegistration.FerricoreSword.get())
                .add(JDTRegistration.BlazegoldSword.get())
                .add(JDTRegistration.CelestigemSword.get())
                .add(JDTRegistration.EclipseAlloySword.get());
        tag(ItemTags.PICKAXES)
                .add(JDTRegistration.FerricorePickaxe.get())
                .add(JDTRegistration.BlazegoldPickaxe.get())
                .add(JDTRegistration.CelestigemPickaxe.get())
                .add(JDTRegistration.EclipseAlloyPickaxe.get())
                .add(JDTRegistration.CelestigemPaxel.get())
                .add(JDTRegistration.EclipseAlloyPaxel.get());
        tag(ItemTags.SHOVELS)
                .add(JDTRegistration.FerricoreShovel.get())
                .add(JDTRegistration.BlazegoldShovel.get())
                .add(JDTRegistration.CelestigemShovel.get())
                .add(JDTRegistration.EclipseAlloyShovel.get())
                .add(JDTRegistration.CelestigemPaxel.get())
                .add(JDTRegistration.EclipseAlloyPaxel.get());
        tag(ItemTags.AXES)
                .add(JDTRegistration.FerricoreAxe.get())
                .add(JDTRegistration.BlazegoldAxe.get())
                .add(JDTRegistration.CelestigemAxe.get())
                .add(JDTRegistration.EclipseAlloyAxe.get())
                .add(JDTRegistration.CelestigemPaxel.get())
                .add(JDTRegistration.EclipseAlloyPaxel.get());
        tag(ItemTags.HOES)
                .add(JDTRegistration.FerricoreHoe.get())
                .add(JDTRegistration.BlazegoldHoe.get())
                .add(JDTRegistration.CelestigemHoe.get())
                .add(JDTRegistration.EclipseAlloyHoe.get());
        tag(Tags.Items.INGOTS)
                .add(JDTRegistration.FerricoreIngot.get())
                .add(JDTRegistration.BlazegoldIngot.get())
                .add(JDTRegistration.EclipseAlloyIngot.get());
        tag(Tags.Items.RAW_MATERIALS)
                .add(JDTRegistration.RawFerricore.get())
                .add(JDTRegistration.RawBlazegold.get())
                .add(JDTRegistration.RawEclipseAlloy.get());
        tag(Tags.Items.GEMS)
                .add(JDTRegistration.Celestigem.get());
        tag(ModTags.Items.WRENCHES)
                .add(JDTRegistration.FerricoreWrench.get());
        tag(ModTags.Items.TOOLS_WRENCH)
                .add(JDTRegistration.FerricoreWrench.get());
        tag(Tags.Items.STORAGE_BLOCKS)
                .add(JDTRegistration.FerricoreBlock_ITEM.get())
                .add(JDTRegistration.BlazeGoldBlock_ITEM.get())
                .add(JDTRegistration.CelestigemBlock_ITEM.get())
                .add(JDTRegistration.EclipseAlloyBlock_ITEM.get());
        tag(ItemTags.FOOT_ARMOR)
                .add(JDTRegistration.FerricoreBoots.get())
                .add(JDTRegistration.BlazegoldBoots.get())
                .add(JDTRegistration.CelestigemBoots.get())
                .add(JDTRegistration.EclipseAlloyBoots.get());
        tag(ItemTags.LEG_ARMOR)
                .add(JDTRegistration.FerricoreLeggings.get())
                .add(JDTRegistration.BlazegoldLeggings.get())
                .add(JDTRegistration.CelestigemLeggings.get())
                .add(JDTRegistration.EclipseAlloyLeggings.get());
        tag(ItemTags.CHEST_ARMOR)
                .add(JDTRegistration.FerricoreChestplate.get())
                .add(JDTRegistration.BlazegoldChestplate.get())
                .add(JDTRegistration.CelestigemChestplate.get())
                .add(JDTRegistration.EclipseAlloyChestplate.get());
        tag(ItemTags.HEAD_ARMOR)
                .add(JDTRegistration.FerricoreHelmet.get())
                .add(JDTRegistration.BlazegoldHelmet.get())
                .add(JDTRegistration.CelestigemHelmet.get())
                .add(JDTRegistration.EclipseAlloyHelmet.get());
        tag(ItemTags.FOOT_ARMOR_ENCHANTABLE)
                .add(JDTRegistration.FerricoreBoots.get())
                .add(JDTRegistration.BlazegoldBoots.get())
                .add(JDTRegistration.CelestigemBoots.get())
                .add(JDTRegistration.EclipseAlloyBoots.get());
        tag(ItemTags.LEG_ARMOR_ENCHANTABLE)
                .add(JDTRegistration.FerricoreLeggings.get())
                .add(JDTRegistration.BlazegoldLeggings.get())
                .add(JDTRegistration.CelestigemLeggings.get())
                .add(JDTRegistration.EclipseAlloyLeggings.get());
        tag(ItemTags.CHEST_ARMOR_ENCHANTABLE)
                .add(JDTRegistration.FerricoreChestplate.get())
                .add(JDTRegistration.BlazegoldChestplate.get())
                .add(JDTRegistration.CelestigemChestplate.get())
                .add(JDTRegistration.EclipseAlloyChestplate.get());
        tag(ItemTags.HEAD_ARMOR_ENCHANTABLE)
                .add(JDTRegistration.FerricoreHelmet.get())
                .add(JDTRegistration.BlazegoldHelmet.get())
                .add(JDTRegistration.CelestigemHelmet.get())
                .add(JDTRegistration.EclipseAlloyHelmet.get());
        tag(ItemTags.BOW_ENCHANTABLE)
                .add(JDTRegistration.FerricoreBow.get())
                .add(JDTRegistration.BlazegoldBow.get())
                .add(JDTRegistration.CelestigemBow.get())
                .add(JDTRegistration.EclipseAlloyBow.get());
        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .add(JDTRegistration.FerricoreBow.get())
                .add(JDTRegistration.BlazegoldBow.get())
                .add(JDTRegistration.CelestigemBow.get())
                .add(JDTRegistration.EclipseAlloyBow.get());
        tag(ModTags.Items.RAW_FERRICORE)
                .add(JDTRegistration.RawFerricore.get());
        tag(ModTags.Items.RAW_BLAZEGOLD)
                .add(JDTRegistration.RawBlazegold.get());
        tag(ModTags.Items.RAW_ECLIPSEALLOY)
                .add(JDTRegistration.RawEclipseAlloy.get());
        tag(ModTags.Items.INGOT_FERRICORE)
                .add(JDTRegistration.FerricoreIngot.get());
        tag(ModTags.Items.INGOT_BLAZEGOLD)
                .add(JDTRegistration.BlazegoldIngot.get());
        tag(ModTags.Items.INGOT_ECLIPSEALLOY)
                .add(JDTRegistration.EclipseAlloyIngot.get());
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
                .add(JDTRegistration.FerricoreBow.get())
                .add(JDTRegistration.BlazegoldBow.get())
                .add(JDTRegistration.CelestigemPickaxe.get())
                .add(JDTRegistration.EclipseAlloyBow.get());
        tag(ModTags.Items.RANGED_WEAPON)
                .add(JDTRegistration.FerricoreBow.get())
                .add(JDTRegistration.BlazegoldBow.get())
                .add(JDTRegistration.CelestigemPickaxe.get())
                .add(JDTRegistration.EclipseAlloyBow.get());
        tag(ModTags.Items.MELEE_WEAPON)
                .add(JDTRegistration.FerricoreSword.get())
                .add(JDTRegistration.FerricoreAxe.get())
                .add(JDTRegistration.BlazegoldSword.get())
                .add(JDTRegistration.BlazegoldAxe.get())
                .add(JDTRegistration.CelestigemSword.get())
                .add(JDTRegistration.CelestigemAxe.get())
                .add(JDTRegistration.EclipseAlloySword.get())
                .add(JDTRegistration.EclipseAlloyAxe.get())
                .add(JDTRegistration.CelestigemPaxel.get())
                .add(JDTRegistration.EclipseAlloyPaxel.get());
        tag(ModTags.Items.MINING_TOOL)
                .add(JDTRegistration.CelestigemPaxel.get())
                .add(JDTRegistration.EclipseAlloyPaxel.get());
        tag(ModTags.Items.PAXEL)
                .add(JDTRegistration.CelestigemPaxel.get())
                .add(JDTRegistration.EclipseAlloyPaxel.get());
        tag(ItemTags.CLUSTER_MAX_HARVESTABLES)
                .add(JDTRegistration.FerricorePickaxe.get())
                .add(JDTRegistration.BlazegoldPickaxe.get())
                .add(JDTRegistration.CelestigemPickaxe.get())
                .add(JDTRegistration.EclipseAlloyPickaxe.get())
                .add(JDTRegistration.CelestigemPaxel.get())
                .add(JDTRegistration.EclipseAlloyPaxel.get());
        tag(ModTags.Items.PARADOX_DENY)
                .add(Items.BEDROCK);
        tag(ModTags.Items.STORAGEBLOCKS)
                .add(JDTRegistration.CharcoalBlock_ITEM.get());
        tag(ModTags.Items.CHARCOALBLOCKS)
                .add(JDTRegistration.CharcoalBlock_ITEM.get());

        tag(ModTags.Items.GOO_RECIPE_TIER_1)
                .add(JDTRegistration.GooBlock_Tier1_ITEM.get())
                .addOptionalTag(ModTags.Items.GOO_RECIPE_TIER_2);

        tag(ModTags.Items.GOO_RECIPE_TIER_2)
                .add(JDTRegistration.GooBlock_Tier2_ITEM.get())
                .addOptionalTag(ModTags.Items.GOO_RECIPE_TIER_3);

        tag(ModTags.Items.GOO_RECIPE_TIER_3)
                .add(JDTRegistration.GooBlock_Tier3_ITEM.get())
                .addOptionalTag(ModTags.Items.GOO_RECIPE_TIER_4);

        tag(ModTags.Items.GOO_RECIPE_TIER_4)
                .add(JDTRegistration.GooBlock_Tier4_ITEM.get());

        tag(toolRepairTag("ferricore")).add(JDTRegistration.FerricoreIngot.get());
        tag(toolRepairTag("blazegold")).add(JDTRegistration.BlazegoldIngot.get());
        tag(toolRepairTag("celestigem")).add(JDTRegistration.Celestigem.get());
        tag(toolRepairTag("eclipsealloy")).add(JDTRegistration.EclipseAlloyIngot.get());

        tag(armorRepairTag("ferricore")).add(JDTRegistration.FerricoreIngot.get());
        tag(armorRepairTag("blazegold")).add(JDTRegistration.BlazegoldIngot.get());
        tag(armorRepairTag("celestigem")).add(JDTRegistration.Celestigem.get());
        tag(armorRepairTag("eclipsealloy")).add(JDTRegistration.EclipseAlloyIngot.get());
    }
}
