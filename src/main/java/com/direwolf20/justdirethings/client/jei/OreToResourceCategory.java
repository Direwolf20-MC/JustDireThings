package com.direwolf20.justdirethings.client.jei;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class OreToResourceCategory implements IRecipeCategory<OreToResourceRecipe> {
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic arrow;
    private final IDrawable pickaxeIcon;
    private final ResourceLocation arrowTexture = ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, "textures/jei/gui/gui_vanilla.png");
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "ore_to_resource");

    public OreToResourceCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(120, 30);  // Adjust size as needed
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Registration.RawFerricoreOre.get()));
        this.arrow = guiHelper.createDrawable(ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, "textures/jei/gui/gui_vanilla.png"), 82, 128, 24, 17);
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

        int arrowWidth = 24;  // The width of the arrow (adjust this to match your arrow texture)
        int animationDuration = 40; // Duration of the arrow drawing in ticks
        int pauseDuration = 10; // Duration of the pause in ticks
        int totalDuration = animationDuration + pauseDuration; // Total duration including the pause

        // Calculate animation frame based on total duration (including pause)
        int ticks = (int) (System.currentTimeMillis() / 50 % totalDuration); // Convert to ticks (20 ticks per second)

        if (ticks < animationDuration) {
            // Calculate the visible portion of the arrow from left to right during animation
            int uOffset = Mth.floor((float) ticks / animationDuration * arrowWidth);

            // Draw the animated arrow growing from left to right
            gui.blit(arrowTexture, 46, 10, 82, 128, uOffset, 17);
        } else {
            // During the pause, draw the full arrow
            gui.blit(arrowTexture, 46, 10, 82, 128, arrowWidth, 17);
        }

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
