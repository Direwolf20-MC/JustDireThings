package com.direwolf20.justdirethings.client.jei;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.datagen.recipes.FluidDropRecipe;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FluidDropRecipeCategory implements IRecipeCategory<FluidDropRecipe> {
    public static final RecipeType<FluidDropRecipe> TYPE =
            RecipeType.create(JustDireThings.MODID, "fluid_drop_recipe", FluidDropRecipe.class);

    public static final int width = 120;
    public static final int height = 40;

    private final IDrawable background;
    private final IDrawable slot;
    private final IDrawable icon;
    private final Component localizedName;
    private final IDrawableStatic arrow;

    public FluidDropRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(width, height);
        slot = guiHelper.getSlotDrawable();
        icon = guiHelper.createDrawableItemStack(new ItemStack(Registration.PolymorphicCatalyst.get()));
        localizedName = Component.translatable("justdirethings.fluiddroprecipe.title");
        this.arrow = guiHelper.getRecipeArrow();
    }

    @Override
    public RecipeType<FluidDropRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(FluidDropRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics gui, double mouseX, double mouseY) {
        RenderSystem.enableBlend();
        arrow.draw(gui, 34, 20);
        background.draw(gui, 17, 0);
        RenderSystem.disableBlend();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FluidDropRecipe recipe, IFocusGroup focuses) {
        BlockState input = recipe.getInput();
        IRecipeSlotBuilder inputSlotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, 9, 20);
        if (input.getBlock() instanceof LiquidBlock liquidBlock) {
            inputSlotBuilder
                    .addFluidStack(liquidBlock.fluid, 1000);
        }
        builder.addSlot(RecipeIngredientRole.CATALYST, 9, 0)
                .addItemStack(new ItemStack(recipe.getCatalyst()));

        BlockState output = recipe.getOutput();
        if (output.getBlock() instanceof LiquidBlock liquidBlock) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 68, 20)
                    .addFluidStack(liquidBlock.fluid, 1000);
        }
    }
}
