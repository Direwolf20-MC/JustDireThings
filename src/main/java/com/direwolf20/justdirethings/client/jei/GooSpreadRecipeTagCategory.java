package com.direwolf20.justdirethings.client.jei;

import com.direwolf20.justdirethings.datagen.recipes.GooSpreadRecipeTag;
import com.direwolf20.justdirethings.setup.Registration;
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
import net.neoforged.neoforge.common.crafting.BlockTagIngredient;

import java.util.List;

public class GooSpreadRecipeTagCategory implements IRecipeCategory<RecipeHolder<GooSpreadRecipeTag>> {
    public static final IRecipeHolderType<GooSpreadRecipeTag> TYPE =
            IRecipeHolderType.create(Registration.GOO_SPREAD_RECIPE_TYPE_TAG.get());

    public static final int width = 120;
    public static final int height = 40;

    private final IDrawable icon;
    private final Component localizedName;
    private final IDrawableStatic arrow;

    public GooSpreadRecipeTagCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(Registration.GooBlock_Tier1.get()));
        localizedName = Component.translatable("justdirethings.goospreadrecipetag.title");
        this.arrow = guiHelper.getRecipeArrow();
    }

    @Override
    public IRecipeType<RecipeHolder<GooSpreadRecipeTag>> getRecipeType() {
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
    public void draw(RecipeHolder<GooSpreadRecipeTag> recipe, IRecipeSlotsView slotsView, GuiGraphicsExtractor gui, double mouseX, double mouseY) {
        arrow.draw(gui, 54, 12);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<GooSpreadRecipeTag> recipe, IFocusGroup focuses) {
        BlockTagIngredient input = recipe.value().getInput();
        IRecipeSlotBuilder inputSlotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, 9, 12);
        List<ItemStack> itemstacks = input.items().map(h -> new ItemStack(h.value())).toList();
        inputSlotBuilder.addItemStacks(itemstacks);

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
