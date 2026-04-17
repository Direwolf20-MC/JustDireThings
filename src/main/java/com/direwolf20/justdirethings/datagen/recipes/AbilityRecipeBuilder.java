package com.direwolf20.justdirethings.datagen.recipes;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;


public class AbilityRecipeBuilder implements RecipeBuilder {
    @Nullable
    private String group;

    private final Optional<Ingredient> template;
    private final Ingredient base;
    private final Ingredient addition;
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public AbilityRecipeBuilder(Optional<Ingredient> template, Ingredient base, Ingredient addition) {
        this.template = template;
        this.base = base;
        this.addition = addition;
    }

    public static AbilityRecipeBuilder shapeless(Optional<Ingredient> template, Ingredient base, Ingredient addition) {
        return new AbilityRecipeBuilder(template, base, addition);
    }

    @Override
    public AbilityRecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
        this.criteria.put(pName, pCriterion);
        return this;
    }

    @Override
    public AbilityRecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        Item baseItem = this.base.items().findFirst().orElseThrow().value();
        Item additionItem = this.addition.items().findFirst().orElseThrow().value();
        String armorName = BuiltInRegistries.ITEM.getKey(baseItem).getPath();
        String abilityItemPath = BuiltInRegistries.ITEM.getKey(additionItem).getPath();
        String abilityName = abilityItemPath.substring(abilityItemPath.indexOf("_") + 1);
        return ResourceKey.create(Registries.RECIPE,
                Identifier.fromNamespaceAndPath(JustDireThings.MODID, armorName + "-" + abilityName));
    }

    @Override
    public void save(RecipeOutput pRecipeOutput, ResourceKey<Recipe<?>> pId) {
        this.ensureValid(pId);
        Advancement.Builder advancement$builder = pRecipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        AbilityRecipe recipe = new AbilityRecipe(
                this.template,
                this.base,
                this.addition
        );
        Identifier path = pId.identifier();
        pRecipeOutput.accept(pId, recipe, advancement$builder.build(path.withPrefix("recipes/" + RecipeCategory.MISC.getFolderName() + "/")));
    }

    private void ensureValid(ResourceKey<Recipe<?>> pId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId.identifier());
        }
    }
}
