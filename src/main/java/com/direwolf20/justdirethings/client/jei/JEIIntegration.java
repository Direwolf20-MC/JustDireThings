package com.direwolf20.justdirethings.client.jei;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.jei.ghostfilters.GhostFilterBasic;
import com.direwolf20.justdirethings.client.screens.basescreens.BaseScreen;
import com.direwolf20.justdirethings.common.blocks.baseblocks.BaseMachineBlock;
import com.direwolf20.justdirethings.datagen.recipes.AbilityRecipe;
import com.direwolf20.justdirethings.datagen.recipes.FluidDropRecipe;
import com.direwolf20.justdirethings.datagen.recipes.GooSpreadRecipe;
import com.direwolf20.justdirethings.datagen.recipes.PaxelRecipe;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.IExtendableSmithingRecipeCategory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JeiPlugin
public class JEIIntegration implements IModPlugin {

    @Nonnull
    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(JustDireThings.MODID, "jei_plugin");
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        IRecipeManager recipeRegistry = jeiRuntime.getRecipeManager();
        List<RecipeHolder<CraftingRecipe>> hiddenRecipes = new ArrayList<>();
        for (var sidedBlock : JDTRegistration.SIDEDBLOCKS.getEntries()) {
            if (sidedBlock.get() instanceof BaseMachineBlock) {
                addHiddenNbtClear(hiddenRecipes, sidedBlock.getId());
            }
        }
        for (var sidedBlock : JDTRegistration.BLOCKS.getEntries()) {
            if (sidedBlock.get() instanceof BaseMachineBlock) {
                addHiddenNbtClear(hiddenRecipes, sidedBlock.getId());
            }
        }

        recipeRegistry.hideRecipes(RecipeTypes.CRAFTING, hiddenRecipes);
    }

    @SuppressWarnings("unchecked")
    private static void addHiddenNbtClear(List<RecipeHolder<CraftingRecipe>> out, Identifier blockId) {
        Identifier recipeId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), blockId.getPath() + "_nbtclear");
        ResourceKey<Recipe<?>> recipeKey = ResourceKey.create(Registries.RECIPE, recipeId);
        RecipeHolder<?> holder = JEIRecipeSync.CLIENT_RECIPES.byKey(recipeKey);
        if (holder != null && holder.value() instanceof CraftingRecipe) {
            out.add((RecipeHolder<CraftingRecipe>) holder);
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(
                new GooSpreadRecipeCategory(guiHelper),
                new FluidDropRecipeCategory(guiHelper)
        );
        registration.addRecipeCategories(new OreToResourceCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Collection<RecipeHolder<GooSpreadRecipe>> gooSpreadRecipes = JEIRecipeSync.byType(JDTRegistration.GOO_SPREAD_RECIPE_TYPE.get());
        registration.addRecipes(GooSpreadRecipeCategory.TYPE, List.copyOf(gooSpreadRecipes));

        Collection<RecipeHolder<FluidDropRecipe>> fluidDropRecipes = JEIRecipeSync.byType(JDTRegistration.FLUID_DROP_RECIPE_TYPE.get());
        registration.addRecipes(FluidDropRecipeCategory.TYPE, List.copyOf(fluidDropRecipes));

        //Ore to Resources
        registration.addRecipes(OreToResourceCategory.TYPE,
                List.of(new OreToResourceRecipe(JDTRegistration.RawFerricoreOre.get(), new ItemStack(JDTRegistration.RawFerricore)),
                        new OreToResourceRecipe(JDTRegistration.RawBlazegoldOre.get(), new ItemStack(JDTRegistration.RawBlazegold)),
                        new OreToResourceRecipe(JDTRegistration.RawCelestigemOre.get(), new ItemStack(JDTRegistration.Celestigem)),
                        new OreToResourceRecipe(JDTRegistration.RawEclipseAlloyOre.get(), new ItemStack(JDTRegistration.RawEclipseAlloy)),
                        new OreToResourceRecipe(JDTRegistration.RawCoal_T1.get(), new ItemStack(JDTRegistration.Coal_T1)),
                        new OreToResourceRecipe(JDTRegistration.RawCoal_T2.get(), new ItemStack(JDTRegistration.Coal_T2)),
                        new OreToResourceRecipe(JDTRegistration.RawCoal_T3.get(), new ItemStack(JDTRegistration.Coal_T3)),
                        new OreToResourceRecipe(JDTRegistration.RawCoal_T4.get(), new ItemStack(JDTRegistration.Coal_T4))
                ));

    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        IExtendableSmithingRecipeCategory smithingCategory = registration.getSmithingCategory();
        smithingCategory.addExtension(AbilityRecipe.class, new AbilityRecipeCategory());
        smithingCategory.addExtension(PaxelRecipe.class, new PaxelRecipeCategory());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addCraftingStation(GooSpreadRecipeCategory.TYPE,
                new ItemStack(JDTRegistration.GooBlock_Tier1.get()),
                new ItemStack(JDTRegistration.GooBlock_Tier2.get()),
                new ItemStack(JDTRegistration.GooBlock_Tier3.get()),
                new ItemStack(JDTRegistration.GooBlock_Tier4.get()));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGhostIngredientHandler(BaseScreen.class, new GhostFilterBasic());
    }

}
