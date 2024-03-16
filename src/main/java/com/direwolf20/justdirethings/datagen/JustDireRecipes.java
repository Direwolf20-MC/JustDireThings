package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.datagen.recipes.GooSpreadRecipeBuilder;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

public class JustDireRecipes extends RecipeProvider {


    public JustDireRecipes(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        //Blocks
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.GooBlock_Tier1.get())
                .pattern("csc")
                .pattern("fdf")
                .pattern("csc")
                .define('d', Items.DIRT)
                .define('c', Items.CLAY_BALL)
                .define('s', Items.SUGAR)
                .define('f', Items.ROTTEN_FLESH)
                .group("justdirethings")
                .unlockedBy("has_coal", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COAL))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.GooBlock_Tier2.get())
                .pattern("brb")
                .pattern("wgw")
                .pattern("brb")
                .define('g', Registration.GooBlock_Tier1_ITEM.get())
                .define('b', Items.BLAZE_POWDER)
                .define('r', Tags.Items.DUSTS_REDSTONE)
                .define('w', Items.NETHER_WART)
                .group("justdirethings")
                .unlockedBy("has_goo_block1", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.GooBlock_Tier1_ITEM.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.GooBlock_Tier3.get())
                .pattern("brb")
                .pattern("wgw")
                .pattern("brb")
                .define('g', Registration.GooBlock_Tier2_ITEM.get())
                .define('b', Items.ENDER_PEARL)
                .define('r', Items.CHORUS_FRUIT)
                .define('w', Items.SHULKER_SHELL)
                .group("justdirethings")
                .unlockedBy("has_goo_block2", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.GooBlock_Tier2_ITEM.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.GooBlock_Tier4.get())
                .pattern("brb")
                .pattern("wgw")
                .pattern("brb")
                .define('g', Registration.GooBlock_Tier3_ITEM.get())
                .define('b', Items.SCULK)
                .define('r', Items.SCULK_SHRIEKER)
                .define('w', Items.ECHO_SHARD)
                .group("justdirethings")
                .unlockedBy("has_goo_block3", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.GooBlock_Tier3_ITEM.get()))
                .save(consumer);

        //Items
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.Fuel_Canister.get())
                .pattern(" i ")
                .pattern("ici")
                .pattern(" i ")
                .define('i', Registration.FerricoreIngot.get())
                .define('c', Items.COAL)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.FerricoreIngot.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.Pocket_Generator.get())
                .pattern("iri")
                .pattern("cfc")
                .pattern("iri")
                .define('i', Registration.FerricoreIngot.get())
                .define('c', Items.COAL)
                .define('r', Items.REDSTONE_BLOCK)
                .define('f', Items.FURNACE)
                .group("justdirethings")
                .unlockedBy("has_fuel_canister", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.Fuel_Canister.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.Pocket_GeneratorT2.get())
                .pattern("iri")
                .pattern("cfc")
                .pattern("iri")
                .define('i', Registration.Celestigem.get())
                .define('c', Items.COAL)
                .define('r', Items.REDSTONE_BLOCK)
                .define('f', Registration.Pocket_Generator.get())
                .group("justdirethings")
                .unlockedBy("has_celestigem", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.Celestigem.get()))
                .save(consumer);

        //GooSpread Recipes
        GooSpreadRecipeBuilder.shapeless(new ResourceLocation(JustDireThings.MODID, "dire_iron_block"), Blocks.IRON_BLOCK.defaultBlockState(), Registration.RawFerricoreOre.get().defaultBlockState(), 1, 2400)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t1", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.GooBlock_Tier1_ITEM.get()))
                .save(consumer);
        GooSpreadRecipeBuilder.shapeless(new ResourceLocation(JustDireThings.MODID, "dire_gold_block"), Blocks.GOLD_BLOCK.defaultBlockState(), Registration.RawBlazegoldOre.get().defaultBlockState(), 2, 2400)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t2", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.GooBlock_Tier2_ITEM.get()))
                .save(consumer);
        GooSpreadRecipeBuilder.shapeless(new ResourceLocation(JustDireThings.MODID, "dire_diamond_block"), Blocks.DIAMOND_BLOCK.defaultBlockState(), Registration.RawCelestigemOre.get().defaultBlockState(), 3, 4800)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t3", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.GooBlock_Tier3_ITEM.get()))
                .save(consumer);
        GooSpreadRecipeBuilder.shapeless(new ResourceLocation(JustDireThings.MODID, "dire_netherite_block"), Blocks.NETHERITE_BLOCK.defaultBlockState(), Registration.RawEclipseAlloyOre.get().defaultBlockState(), 4, 4800)
                .group("justdirethings")
                .unlockedBy("has_goo_block_t4", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.GooBlock_Tier4_ITEM.get()))
                .save(consumer);

        //Smelting
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Registration.RawFerricore.get()), RecipeCategory.MISC,
                        Registration.FerricoreIngot.get(), 1.0f, 400)
                .unlockedBy("has_ferricore_raw", inventoryTrigger(ItemPredicate.Builder.item().of(Registration.RawFerricore.get()).build()))
                .save(consumer, new ResourceLocation(JustDireThings.MODID, "ferricore_ingot_smelted"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Registration.RawBlazegold.get()), RecipeCategory.MISC,
                        Registration.BlazegoldIngot.get(), 1.0f, 400)
                .unlockedBy("has_blazegold_raw", inventoryTrigger(ItemPredicate.Builder.item().of(Registration.RawBlazegold.get()).build()))
                .save(consumer, new ResourceLocation(JustDireThings.MODID, "blazegold_ingot_smelted"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Registration.RawEclipseAlloy.get()), RecipeCategory.MISC,
                        Registration.EclipseAlloyIngot.get(), 1.0f, 400)
                .unlockedBy("has_eclipsealloy_raw", inventoryTrigger(ItemPredicate.Builder.item().of(Registration.RawEclipseAlloy.get()).build()))
                .save(consumer, new ResourceLocation(JustDireThings.MODID, "eclipsealloy_ingot_smelted"));

        //Tools
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.FerricoreSword.get())
                .pattern(" f ")
                .pattern(" f ")
                .pattern(" s ")
                .define('f', Registration.FerricoreIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.FerricoreIngot.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.FerricorePickaxe.get())
                .pattern("fff")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', Registration.FerricoreIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.FerricoreIngot.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.FerricoreShovel.get())
                .pattern(" f ")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', Registration.FerricoreIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.FerricoreIngot.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.FerricoreAxe.get())
                .pattern("ff ")
                .pattern("fs ")
                .pattern(" s ")
                .define('f', Registration.FerricoreIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.FerricoreIngot.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.FerricoreHoe.get())
                .pattern("ff ")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', Registration.FerricoreIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_ferricore_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.FerricoreIngot.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.BlazegoldSword.get())
                .pattern(" f ")
                .pattern(" f ")
                .pattern(" s ")
                .define('f', Registration.BlazegoldIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.BlazegoldIngot.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.BlazegoldPickaxe.get())
                .pattern("fff")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', Registration.BlazegoldIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.BlazegoldIngot.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.BlazegoldShovel.get())
                .pattern(" f ")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', Registration.BlazegoldIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.BlazegoldIngot.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.BlazegoldAxe.get())
                .pattern("ff ")
                .pattern("fs ")
                .pattern(" s ")
                .define('f', Registration.BlazegoldIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.BlazegoldIngot.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.BlazegoldHoe.get())
                .pattern("ff ")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', Registration.BlazegoldIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_blazegold_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.BlazegoldIngot.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.CelestigemSword.get())
                .pattern(" f ")
                .pattern(" f ")
                .pattern(" s ")
                .define('f', Registration.Celestigem.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_celestigem", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.Celestigem.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.CelestigemPickaxe.get())
                .pattern("fff")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', Registration.Celestigem.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_celestigem", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.Celestigem.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.CelestigemShovel.get())
                .pattern(" f ")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', Registration.Celestigem.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_celestigem", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.Celestigem.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.CelestigemAxe.get())
                .pattern("ff ")
                .pattern("fs ")
                .pattern(" s ")
                .define('f', Registration.Celestigem.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_celestigem", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.Celestigem.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.CelestigemHoe.get())
                .pattern("ff ")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', Registration.Celestigem.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_celestigem", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.Celestigem.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.EclipseAlloySword.get())
                .pattern(" f ")
                .pattern(" f ")
                .pattern(" s ")
                .define('f', Registration.EclipseAlloyIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.EclipseAlloyIngot.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.EclipseAlloyPickaxe.get())
                .pattern("fff")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', Registration.EclipseAlloyIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.EclipseAlloyIngot.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.EclipseAlloyShovel.get())
                .pattern(" f ")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', Registration.EclipseAlloyIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.EclipseAlloyIngot.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.EclipseAlloyAxe.get())
                .pattern("ff ")
                .pattern("fs ")
                .pattern(" s ")
                .define('f', Registration.EclipseAlloyIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.EclipseAlloyIngot.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.EclipseAlloyHoe.get())
                .pattern("ff ")
                .pattern(" s ")
                .pattern(" s ")
                .define('f', Registration.EclipseAlloyIngot.get())
                .define('s', Items.STICK)
                .group("justdirethings")
                .unlockedBy("has_eclipsealloy_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.EclipseAlloyIngot.get()))
                .save(consumer);

        //Resource Conversions
        nineBlockStorageRecipes(consumer, RecipeCategory.MISC, Registration.FerricoreIngot.get(), RecipeCategory.BUILDING_BLOCKS, Registration.FerricoreBlock.get(), Registration.FerricoreIngot.getId().toString() + "_9x9", "justdirethings", Registration.FerricoreBlock.getId().toString() + "_9x9", "justdirethings");
        nineBlockStorageRecipes(consumer, RecipeCategory.MISC, Registration.BlazegoldIngot.get(), RecipeCategory.BUILDING_BLOCKS, Registration.BlazeGoldBlock.get(), Registration.BlazegoldIngot.getId().toString() + "_9x9", "justdirethings", Registration.BlazeGoldBlock.getId().toString() + "_9x9", "justdirethings");
        nineBlockStorageRecipes(consumer, RecipeCategory.MISC, Registration.Celestigem.get(), RecipeCategory.BUILDING_BLOCKS, Registration.CelestigemBlock.get(), Registration.Celestigem.getId().toString() + "_9x9", "justdirethings", Registration.CelestigemBlock.getId().toString() + "_9x9", "justdirethings");
        nineBlockStorageRecipes(consumer, RecipeCategory.MISC, Registration.EclipseAlloyIngot.get(), RecipeCategory.BUILDING_BLOCKS, Registration.EclipseAlloyBlock.get(), Registration.EclipseAlloyIngot.getId().toString() + "_9x9", "justdirethings", Registration.EclipseAlloyBlock.getId().toString() + "_9x9", "justdirethings");

    }
}
