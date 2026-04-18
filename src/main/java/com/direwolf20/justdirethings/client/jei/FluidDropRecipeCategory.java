package com.direwolf20.justdirethings.client.jei;

import com.direwolf20.justdirethings.datagen.recipes.FluidDropRecipe;
import com.direwolf20.justdirethings.setup.Registration;
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
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FluidDropRecipeCategory implements IRecipeCategory<RecipeHolder<FluidDropRecipe>> {
    public static final IRecipeHolderType<FluidDropRecipe> TYPE =
            IRecipeHolderType.create(Registration.FLUID_DROP_RECIPE_TYPE.get());

    public static final int width = 120;
    public static final int height = 40;

    private final IDrawable icon;
    private final Component localizedName;
    private final IDrawableStatic arrow;

    public FluidDropRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(Registration.PolymorphicCatalyst.get()));
        localizedName = Component.translatable("justdirethings.fluiddroprecipe.title");
        this.arrow = guiHelper.getRecipeArrow();
    }

    @Override
    public IRecipeType<RecipeHolder<FluidDropRecipe>> getRecipeType() {
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
    public void draw(RecipeHolder<FluidDropRecipe> recipe, IRecipeSlotsView slotsView, GuiGraphicsExtractor gui, double mouseX, double mouseY) {
        arrow.draw(gui, 34, 20);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<FluidDropRecipe> recipe, IFocusGroup focuses) {
        BlockState input = recipe.value().getInput();
        IRecipeSlotBuilder inputSlotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, 9, 20);
        if (input.getBlock() instanceof LiquidBlock liquidBlock) {
            inputSlotBuilder.add(liquidBlock.fluid, 1000);
        }
        builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 9, 0)
                .add(new ItemStack(recipe.value().getCatalyst()));

        BlockState output = recipe.value().getOutput();
        if (output.getBlock() instanceof LiquidBlock liquidBlock) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 68, 20)
                    .add(liquidBlock.fluid, 1000);
        } else {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 68, 20)
                    .add(new ItemStack(output.getBlock()));
        }
    }
}
