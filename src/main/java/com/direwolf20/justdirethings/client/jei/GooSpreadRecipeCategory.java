package com.direwolf20.justdirethings.client.jei;

import com.direwolf20.justdirethings.datagen.recipes.GooSpreadRecipe;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.direwolf20.justdirethings.util.ModTags;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;

public class GooSpreadRecipeCategory implements IRecipeCategory<RecipeHolder<GooSpreadRecipe>> {
    public static final IRecipeHolderType<GooSpreadRecipe> TYPE =
            IRecipeHolderType.create(JDTRegistration.GOO_SPREAD_RECIPE_TYPE.get());

    public static final int width = 120;
    public static final int height = 40;

    private final IDrawable icon;
    private final Component localizedName;
    private final IDrawableStatic arrow;

    public GooSpreadRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(JDTRegistration.GooBlock_Tier1.get()));
        localizedName = Component.translatable("justdirethings.goospreadrecipe.title");
        this.arrow = guiHelper.getRecipeArrow();
    }

    @Override
    public IRecipeType<RecipeHolder<GooSpreadRecipe>> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(RecipeHolder<GooSpreadRecipe> recipe, IRecipeSlotsView slotsView, GuiGraphicsExtractor gui, double mouseX, double mouseY) {
        arrow.draw(gui, 54, 12);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<GooSpreadRecipe> recipe, IFocusGroup focuses) {
        BlockState input = recipe.value().getInput();
        IRecipeSlotBuilder inputSlotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, 9, 12);
        if (input.getBlock().asItem() != Items.AIR) {
            inputSlotBuilder.add(new ItemStack(input.getBlock()));
        } else if (input.getBlock() instanceof LiquidBlock liquidBlock) {
            inputSlotBuilder.add(liquidBlock.fluid, 1000);
        }

        HolderLookup.Provider registries = Minecraft.getInstance().level.registryAccess();
        builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 29, 12)
                .add(Ingredient.of(
                        registries.lookupOrThrow(Registries.ITEM)
                                .getOrThrow(ModTags.Items.GOO_RECIPE_TIERS.get(recipe.value().getTierRequirement() - 1))));

        BlockState output = recipe.value().getOutput();
        if (output.getBlock().asItem() != Items.AIR) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 88, 12)
                    .add(new ItemStack(output.getBlock()));
        } else if (output.getBlock() instanceof LiquidBlock liquidBlock) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 88, 12)
                    .add(liquidBlock.fluid, 1000);
        }
    }
}
