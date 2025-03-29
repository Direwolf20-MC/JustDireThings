package com.direwolf20.justdirethings.client.jei;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.jei.ghostfilters.GhostFilterBasic;
import com.direwolf20.justdirethings.client.screens.basescreens.BaseScreen;
import com.direwolf20.justdirethings.common.blocks.baseblocks.BaseMachineBlock;
import com.direwolf20.justdirethings.datagen.recipes.*;
import com.direwolf20.justdirethings.setup.Registration;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.IExtendableSmithingRecipeCategory;
import mezz.jei.api.registration.*;
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
                new GooSpreadRecipeTagCategory(guiHelper),
                new FluidDropRecipeCategory(guiHelper)
        );
        registration.addRecipeCategories(new OreToResourceCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        assert Minecraft.getInstance().level != null;
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<GooSpreadRecipe> goospreadrecipes = recipeManager.getAllRecipesFor(Registration.GOO_SPREAD_RECIPE_TYPE.get())
                .stream().map(RecipeHolder::value).collect(Collectors.toList());

        registration.addRecipes(GooSpreadRecipeCategory.TYPE, goospreadrecipes);

        List<GooSpreadRecipeTag> goospreadtagrecipes = recipeManager.getAllRecipesFor(Registration.GOO_SPREAD_RECIPE_TYPE_TAG.get())
                .stream().map(RecipeHolder::value).collect(Collectors.toList());

        registration.addRecipes(GooSpreadRecipeTagCategory.TYPE, goospreadtagrecipes);

        List<FluidDropRecipe> fluidDropRecipes = recipeManager.getAllRecipesFor(Registration.FLUID_DROP_RECIPE_TYPE.get())
                .stream().map(RecipeHolder::value).collect(Collectors.toList());

        registration.addRecipes(FluidDropRecipeCategory.TYPE, fluidDropRecipes);

        //Ore to Resources
        registration.addRecipes(new RecipeType<>(OreToResourceCategory.UID, OreToResourceRecipe.class),
                List.of(new OreToResourceRecipe(Registration.RawFerricoreOre.get(), new ItemStack(Registration.RawFerricore)),
                        new OreToResourceRecipe(Registration.RawBlazegoldOre.get(), new ItemStack(Registration.RawBlazegold)),
                        new OreToResourceRecipe(Registration.RawCelestigemOre.get(), new ItemStack(Registration.Celestigem)),
                        new OreToResourceRecipe(Registration.RawEclipseAlloyOre.get(), new ItemStack(Registration.RawEclipseAlloy)),
                        new OreToResourceRecipe(Registration.RawCoal_T1.get(), new ItemStack(Registration.Coal_T1)),
                        new OreToResourceRecipe(Registration.RawCoal_T2.get(), new ItemStack(Registration.Coal_T2)),
                        new OreToResourceRecipe(Registration.RawCoal_T3.get(), new ItemStack(Registration.Coal_T3)),
                        new OreToResourceRecipe(Registration.RawCoal_T4.get(), new ItemStack(Registration.Coal_T4))
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
        registry.addRecipeCatalyst(new ItemStack(Registration.GooBlock_Tier1.get()), GooSpreadRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(Registration.GooBlock_Tier2.get()), GooSpreadRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(Registration.GooBlock_Tier3.get()), GooSpreadRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(Registration.GooBlock_Tier4.get()), GooSpreadRecipeCategory.TYPE);

        registry.addRecipeCatalyst(new ItemStack(Registration.GooBlock_Tier1.get()), GooSpreadRecipeTagCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(Registration.GooBlock_Tier2.get()), GooSpreadRecipeTagCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(Registration.GooBlock_Tier3.get()), GooSpreadRecipeTagCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(Registration.GooBlock_Tier4.get()), GooSpreadRecipeTagCategory.TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGhostIngredientHandler(BaseScreen.class, new GhostFilterBasic());
    }

}
