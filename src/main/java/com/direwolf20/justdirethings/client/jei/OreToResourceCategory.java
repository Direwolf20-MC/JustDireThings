package com.direwolf20.justdirethings.client.jei;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class OreToResourceCategory implements IRecipeCategory<OreToResourceRecipe> {
    private final IDrawable icon;
    private final IDrawable pickaxeIcon;
    private final IDrawableAnimated animatedArrow;
    public static final Identifier UID = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "ore_to_resource");
    public static final IRecipeType<OreToResourceRecipe> TYPE = IRecipeType.create(UID, OreToResourceRecipe.class);

    public OreToResourceCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Registration.RawFerricoreOre.get()));
        IDrawableStatic arrowDrawable = guiHelper.getRecipeArrow();
        this.animatedArrow = guiHelper.createAnimatedDrawable(arrowDrawable, 40, IDrawableAnimated.StartDirection.LEFT, false);
        this.pickaxeIcon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.IRON_PICKAXE));
    }

    @Override
    public IRecipeType<OreToResourceRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("justdirethings.oretoresource.title");
    }

    @Override
    public int getWidth() {
        return 120;
    }

    @Override
    public int getHeight() {
        return 30;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(OreToResourceRecipe recipe, IRecipeSlotsView slotsView, GuiGraphicsExtractor gui, double mouseX, double mouseY) {
        animatedArrow.draw(gui, 46, 10);
        pickaxeIcon.draw(gui, 50, -2);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, OreToResourceRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 10).add(recipe.getOreBlock());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 10).add(recipe.getOutput());
    }
}
