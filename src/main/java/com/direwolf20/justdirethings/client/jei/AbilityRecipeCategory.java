package com.direwolf20.justdirethings.client.jei;

import com.direwolf20.justdirethings.datagen.recipes.AbilityRecipe;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;
import net.minecraft.world.item.crafting.Ingredient;

public class AbilityRecipeCategory implements ISmithingCategoryExtension<AbilityRecipe> {

    public AbilityRecipeCategory() {
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setBase(AbilityRecipe recipe, T ingredientAcceptor) {
        Ingredient ingredient = recipe.getBase();
        ingredientAcceptor.addIngredients(ingredient);
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setAddition(AbilityRecipe recipe, T ingredientAcceptor) {
        Ingredient ingredient = recipe.getAddition();
        ingredientAcceptor.addIngredients(ingredient);
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setTemplate(AbilityRecipe recipe, T ingredientAcceptor) {
        Ingredient ingredient = recipe.getTemplate();
        ingredientAcceptor.addIngredients(ingredient);
    }
}
