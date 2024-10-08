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
import net.neoforged.neoforge.common.crafting.BlockTagIngredient;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;


public class GooSpreadRecipeTagBuilder implements RecipeBuilder {
    @Nullable
    private String group;

    private final ResourceLocation id;
    protected final BlockTagIngredient input;
    protected final BlockState output;
    protected final int tierRequirement;
    protected final int craftingDuration;
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public GooSpreadRecipeTagBuilder(ResourceLocation id, BlockTagIngredient input, BlockState output, int tierRequirement, int craftingDuration) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.tierRequirement = tierRequirement;
        this.craftingDuration = craftingDuration;
    }

    public static GooSpreadRecipeTagBuilder shapeless(ResourceLocation id, BlockTagIngredient input, BlockState output, int tierRequirement, int craftingDuration) {
        return new GooSpreadRecipeTagBuilder(id, input, output, tierRequirement, craftingDuration);
    }

    public GooSpreadRecipeTagBuilder requires(TagKey<Item> pTag) {
        return this.requires(Ingredient.of(pTag));
    }

    public GooSpreadRecipeTagBuilder requires(ItemLike pItem) {
        return this.requires(pItem, 1);
    }

    public GooSpreadRecipeTagBuilder requires(ItemLike pItem, int pQuantity) {
        for (int i = 0; i < pQuantity; ++i) {
            this.requires(Ingredient.of(pItem));
        }

        return this;
    }

    public GooSpreadRecipeTagBuilder requires(Ingredient pIngredient) {
        return this.requires(pIngredient, 1);
    }

    public GooSpreadRecipeTagBuilder requires(Ingredient pIngredient, int pQuantity) {
        for (int i = 0; i < pQuantity; ++i) {
            this.ingredients.add(pIngredient);
        }

        return this;
    }

    public GooSpreadRecipeTagBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
        this.criteria.put(pName, pCriterion);
        return this;
    }

    public GooSpreadRecipeTagBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    @Override
    public Item getResult() {
        return ItemStack.EMPTY.getItem();
    }

    public void save(RecipeOutput pRecipeOutput) {
        this.save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, BuiltInRegistries.BLOCK.getKey(this.output.getBlock()).getPath() + "-goospread_tag"));
    }

    @Override
    public void save(RecipeOutput pRecipeOutput, ResourceLocation pId) {
        this.ensureValid(pId);
        Advancement.Builder advancement$builder = pRecipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        GooSpreadRecipeTag shapelessrecipe = new GooSpreadRecipeTag(
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
