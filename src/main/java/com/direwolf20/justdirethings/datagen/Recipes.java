package com.direwolf20.justdirethings.datagen;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

public class Recipes extends RecipeProvider {


    public Recipes(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        //Blocks

        //Items
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.Fuel_Canister.get())
                .pattern(" i ")
                .pattern("ici")
                .pattern(" i ")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('c', Items.COAL)
                .group("justdirethings")
                .unlockedBy("has_coal", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COAL))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.Pocket_Generator.get())
                .pattern("ici")
                .pattern("rfr")
                .pattern("ici")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('c', Items.COAL)
                .define('r', Tags.Items.DUSTS_REDSTONE)
                .define('f', Registration.Fuel_Canister.get())
                .group("justdirethings")
                .unlockedBy("has_fuel_canister", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.Fuel_Canister.get()))
                .save(consumer);

    }
}
