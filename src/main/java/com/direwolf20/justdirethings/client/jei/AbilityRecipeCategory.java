package com.direwolf20.justdirethings.client.jei;

import com.direwolf20.justdirethings.datagen.recipes.AbilityRecipe;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingRecipeInput;

public class AbilityRecipeCategory implements ISmithingCategoryExtension<AbilityRecipe> {

    public AbilityRecipeCategory() {
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setBase(AbilityRecipe recipe, T ingredientAcceptor) {
        ingredientAcceptor.add(recipe.getBase());
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setAddition(AbilityRecipe recipe, T ingredientAcceptor) {
        ingredientAcceptor.add(recipe.getAddition());
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setTemplate(AbilityRecipe recipe, T ingredientAcceptor) {
        recipe.getTemplate().ifPresent(ingredientAcceptor::add);
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setOutput(AbilityRecipe recipe, T ingredientAcceptor) {
        ClientLevel level = Minecraft.getInstance().level;
        assert level != null;
        ItemStack resultItem = recipe.getResultItem(level.registryAccess());
        ingredientAcceptor.add(resultItem);
    }

    @Override
    public void onDisplayedIngredientsUpdate(AbilityRecipe recipe, IRecipeSlotDrawable templateSlot, IRecipeSlotDrawable baseSlot, IRecipeSlotDrawable additionSlot, IRecipeSlotDrawable outputSlot, IFocusGroup focuses) {
        SmithingRecipeInput input = new SmithingRecipeInput(
            templateSlot.getDisplayedItemStack().orElse(ItemStack.EMPTY),
            baseSlot.getDisplayedItemStack().orElse(ItemStack.EMPTY),
            additionSlot.getDisplayedItemStack().orElse(ItemStack.EMPTY)
        );
        ItemStack result = recipe.assemble(input);
        outputSlot.createDisplayOverrides().add(result);
    }
}
