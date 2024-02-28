package com.direwolf20.justdirethings.client.jei;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.datagen.recipes.GooSpreadRecipe;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class GooSpreadRecipeCategory implements IRecipeCategory<GooSpreadRecipe> {
    public static final RecipeType<GooSpreadRecipe> TYPE =
            RecipeType.create(JustDireThings.MODID, "goo_spread_recipe", com.direwolf20.justdirethings.datagen.recipes.GooSpreadRecipe.class);

    public static final int width = 120;
    public static final int height = 40;

    private final IDrawable background;
    private final IDrawable slot;
    private final IDrawable icon;
    private final Component localizedName;

    public GooSpreadRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(width, height);
        slot = guiHelper.getSlotDrawable();
        icon = guiHelper.createDrawableItemStack(new ItemStack(Registration.GooBlock_Tier1.get()));
        localizedName = Component.translatable("justdirethings.goospreadrecipe.title");
    }

    @Override
    public RecipeType<GooSpreadRecipe> getRecipeType() {
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
    public void draw(GooSpreadRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics gui, double mouseX, double mouseY) {
        RenderSystem.enableBlend();
        background.draw(gui, 17, 0);
        RenderSystem.disableBlend();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GooSpreadRecipe recipe, IFocusGroup focuses) {
        BlockState input = recipe.getInput();
        IRecipeSlotBuilder inputSlotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, 9, 12);
        if (input.getBlock().asItem() != Items.AIR) {
            inputSlotBuilder
                    .addItemStack(new ItemStack(input.getBlock()));
        }
        List<ItemStack> catalystlist = new ArrayList<>();
        catalystlist.add(new ItemStack(Registration.GooBlock_Tier1.get()));
        catalystlist.add(new ItemStack(Registration.GooBlock_Tier2.get()));
        builder.addSlot(RecipeIngredientRole.CATALYST, 39, 12)
                .addItemStacks(catalystlist);

        BlockState output = recipe.getOutput();
        if (output.getBlock().asItem() != Items.AIR) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 68, 12)
                    .addItemStack(new ItemStack(output.getBlock()));
        }
    }
}
