package com.direwolf20.justdirethings.datagen.recipes;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;


public class GooSpreadRecipeBuilder implements RecipeBuilder {
    @Nullable
    private String group;

    private final Identifier id;
    protected final BlockState input;
    protected final BlockState output;
    protected final int tierRequirement;
    protected final int craftingDuration;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public GooSpreadRecipeBuilder(Identifier id, BlockState input, BlockState output, int tierRequirement, int craftingDuration) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.tierRequirement = tierRequirement;
        this.craftingDuration = craftingDuration;
    }

    public static GooSpreadRecipeBuilder shapeless(Identifier id, BlockState input, BlockState output, int tierRequirement, int craftingDuration) {
        return new GooSpreadRecipeBuilder(id, input, output, tierRequirement, craftingDuration);
    }

    @Override
    public GooSpreadRecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
        this.criteria.put(pName, pCriterion);
        return this;
    }

    @Override
    public GooSpreadRecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        Identifier outputName = BuiltInRegistries.BLOCK.getKey(this.output.getBlock());
        return ResourceKey.create(Registries.RECIPE,
                Identifier.fromNamespaceAndPath(JustDireThings.MODID, outputName.getPath() + "-goospread"));
    }

    @Override
    public void save(RecipeOutput pRecipeOutput, ResourceKey<Recipe<?>> pId) {
        this.ensureValid(pId);
        Advancement.Builder advancement$builder = pRecipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        GooSpreadRecipe recipe = new GooSpreadRecipe(
                this.id,
                this.input,
                this.output,
                this.tierRequirement,
                this.craftingDuration
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
