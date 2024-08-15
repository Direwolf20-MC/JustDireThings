package com.direwolf20.justdirethings.client.jei;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class OreToResourceCategory implements IRecipeCategory<OreToResourceRecipe> {
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable pickaxeIcon;
    private final IDrawableAnimated animatedArrow;
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "ore_to_resource");

    public OreToResourceCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(120, 30);  // Adjust size as needed
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Registration.RawFerricoreOre.get()));
        IDrawableStatic arrowDrawable = guiHelper.createDrawable(ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, "textures/jei/gui/gui_vanilla.png"), 82, 128, 24, 17);
        this.animatedArrow = guiHelper.createAnimatedDrawable(arrowDrawable, 40, IDrawableAnimated.StartDirection.LEFT, false);  // 20 ticks duration, left-to-right animation, no looping
        this.pickaxeIcon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.IRON_PICKAXE));
    }

    @Override
    public RecipeType<OreToResourceRecipe> getRecipeType() {
        return new RecipeType<>(UID, OreToResourceRecipe.class);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("justdirethings.oretoresource.title");
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
    public void draw(OreToResourceRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics gui, double mouseX, double mouseY) {
        RenderSystem.enableBlend();

        // Draw the animated arrow
        animatedArrow.draw(gui, 46, 10);  // Position the arrow as needed

        background.draw(gui, 17, 0);
        pickaxeIcon.draw(gui, 50, -2);
        RenderSystem.disableBlend();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, OreToResourceRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 10).addItemStack(recipe.getOreBlock());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 10).addItemStack(recipe.getOutput());
    }
}
