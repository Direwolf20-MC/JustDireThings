package com.direwolf20.justdirethings.datagen.recipes;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;


public class GooSpreadRecipeBuilder implements RecipeBuilder {
    @Nullable
    private String group;

    private final ResourceLocation id;
    protected final BlockState input;
    protected final BlockState output;
    protected final int tierRequirement;
    protected final int craftingDuration;
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public GooSpreadRecipeBuilder(ResourceLocation id, BlockState input, BlockState output, int tierRequirement, int craftingDuration) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.tierRequirement = tierRequirement;
        this.craftingDuration = craftingDuration;
    }

    public static GooSpreadRecipeBuilder shapeless(ResourceLocation id, BlockState input, BlockState output, int tierRequirement, int craftingDuration) {
        return new GooSpreadRecipeBuilder(id, input, output, tierRequirement, craftingDuration);
    }

    public GooSpreadRecipeBuilder requires(TagKey<Item> pTag) {
        return this.requires(Ingredient.of(pTag));
    }

    public GooSpreadRecipeBuilder requires(ItemLike pItem) {
        return this.requires(pItem, 1);
    }

    public GooSpreadRecipeBuilder requires(ItemLike pItem, int pQuantity) {
        for (int i = 0; i < pQuantity; ++i) {
            this.requires(Ingredient.of(pItem));
        }

        return this;
    }

    public GooSpreadRecipeBuilder requires(Ingredient pIngredient) {
        return this.requires(pIngredient, 1);
    }

    public GooSpreadRecipeBuilder requires(Ingredient pIngredient, int pQuantity) {
        for (int i = 0; i < pQuantity; ++i) {
            this.ingredients.add(pIngredient);
        }

        return this;
    }

    public GooSpreadRecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
        this.criteria.put(pName, pCriterion);
        return this;
    }

    public GooSpreadRecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    @Override
    public Item getResult() {
        return ItemStack.EMPTY.getItem();
    }

    public void save(RecipeOutput pRecipeOutput) {
        this.save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, BuiltInRegistries.BLOCK.getKey(this.output.getBlock()).getPath() + "-goospread"));
    }

    @Override
    public void save(RecipeOutput pRecipeOutput, ResourceLocation pId) {
        this.ensureValid(pId);
        Advancement.Builder advancement$builder = pRecipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        GooSpreadRecipe shapelessrecipe = new GooSpreadRecipe(
                this.id,
                this.input,
                this.output,
                this.tierRequirement,
                this.craftingDuration
        );
        pRecipeOutput.accept(pId, shapelessrecipe, advancement$builder.build(pId.withPrefix("recipes/" + RecipeCategory.MISC.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation pId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
    }
}
