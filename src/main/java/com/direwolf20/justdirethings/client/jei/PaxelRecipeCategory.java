package com.direwolf20.justdirethings.client.jei;

import com.direwolf20.justdirethings.datagen.recipes.PaxelRecipe;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingRecipeInput;

public class PaxelRecipeCategory implements ISmithingCategoryExtension<PaxelRecipe> {

    public PaxelRecipeCategory() {
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setBase(PaxelRecipe recipe, T ingredientAcceptor) {
        Ingredient ingredient = recipe.getBase();
        ingredientAcceptor.addIngredients(ingredient);
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setAddition(PaxelRecipe recipe, T ingredientAcceptor) {
        Ingredient ingredient = recipe.getAddition();
        ingredientAcceptor.addIngredients(ingredient);
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setTemplate(PaxelRecipe recipe, T ingredientAcceptor) {
        Ingredient ingredient = recipe.getTemplate();
        ingredientAcceptor.addIngredients(ingredient);
    }


    @Override
    public <T extends IIngredientAcceptor<T>> void setOutput(PaxelRecipe recipe, T ingredientAcceptor) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        assert level != null;
        RegistryAccess registryAccess = level.registryAccess();
        ItemStack resultItem = recipe.getResultItem(registryAccess);
        ingredientAcceptor.addItemStack(resultItem);
    }

    @Override
    public void onDisplayedIngredientsUpdate(PaxelRecipe recipe, IRecipeSlotDrawable templateSlot, IRecipeSlotDrawable baseSlot, IRecipeSlotDrawable additionSlot, IRecipeSlotDrawable outputSlot, IFocusGroup focuses) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        assert level != null;
        RegistryAccess registryAccess = level.registryAccess();

        SmithingRecipeInput input = new SmithingRecipeInput(
            templateSlot.getDisplayedItemStack().orElse(ItemStack.EMPTY),
            baseSlot.getDisplayedItemStack().orElse(ItemStack.EMPTY),
            additionSlot.getDisplayedItemStack().orElse(ItemStack.EMPTY)
        );
        ItemStack result = recipe.assemble(input, registryAccess);
        outputSlot.createDisplayOverrides()
            .addItemStack(result);
    }
}
