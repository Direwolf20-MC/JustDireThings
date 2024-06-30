package com.direwolf20.justdirethings.client.jei;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.jei.ghostfilters.GhostFilterBasic;
import com.direwolf20.justdirethings.client.screens.basescreens.BaseScreen;
import com.direwolf20.justdirethings.common.blocks.baseblocks.BaseMachineBlock;
import com.direwolf20.justdirethings.datagen.recipes.FluidDropRecipe;
import com.direwolf20.justdirethings.datagen.recipes.GooSpreadRecipe;
import com.direwolf20.justdirethings.setup.Registration;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@JeiPlugin
public class JEIIntegration implements IModPlugin {

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "jei_plugin");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        IRecipeManager recipeRegistry = jeiRuntime.getRecipeManager();
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<RecipeHolder<CraftingRecipe>> hiddenRecipes = new ArrayList<>();
        for (var sidedBlock : Registration.SIDEDBLOCKS.getEntries()) {
            if (sidedBlock.get() instanceof BaseMachineBlock baseMachineBlock) {
                Optional<RecipeHolder<?>> recipe = recipeManager.byKey(ResourceLocation.parse(sidedBlock.getId() + "_nbtclear"));
                recipe.ifPresent(recipeHolder -> hiddenRecipes.add((RecipeHolder<CraftingRecipe>) recipeHolder));
            }
        }
        for (var sidedBlock : Registration.BLOCKS.getEntries()) {
            if (sidedBlock.get() instanceof BaseMachineBlock baseMachineBlock) {
                Optional<RecipeHolder<?>> recipe = recipeManager.byKey(ResourceLocation.parse(sidedBlock.getId() + "_nbtclear"));
                recipe.ifPresent(recipeHolder -> hiddenRecipes.add((RecipeHolder<CraftingRecipe>) recipeHolder));
            }
        }


        recipeRegistry.hideRecipes(RecipeTypes.CRAFTING, hiddenRecipes);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(
                new GooSpreadRecipeCategory(guiHelper),
                new FluidDropRecipeCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        assert Minecraft.getInstance().level != null;
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<GooSpreadRecipe> goospreadrecipes = recipeManager.getAllRecipesFor(Registration.GOO_SPREAD_RECIPE_TYPE.get())
                .stream().map(RecipeHolder::value).collect(Collectors.toList());

        registration.addRecipes(GooSpreadRecipeCategory.TYPE, goospreadrecipes);

        List<FluidDropRecipe> fluidDropRecipes = recipeManager.getAllRecipesFor(Registration.FLUID_DROP_RECIPE_TYPE.get())
                .stream().map(RecipeHolder::value).collect(Collectors.toList());

        registration.addRecipes(FluidDropRecipeCategory.TYPE, fluidDropRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(Registration.GooBlock_Tier1.get()), GooSpreadRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(Registration.GooBlock_Tier2.get()), GooSpreadRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(Registration.GooBlock_Tier3.get()), GooSpreadRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(Registration.GooBlock_Tier4.get()), GooSpreadRecipeCategory.TYPE);
        //registry.addRecipeCatalyst(new ItemStack(Registration.PolymorphicCatalyst.get()), FluidDropRecipeCategory.TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGhostIngredientHandler(BaseScreen.class, new GhostFilterBasic());
    }

}
