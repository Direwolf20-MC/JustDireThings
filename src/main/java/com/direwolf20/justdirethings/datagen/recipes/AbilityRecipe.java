package com.direwolf20.justdirethings.datagen.recipes;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.items.abilityupgrades.UpgradeBlank;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.stream.Stream;

public class AbilityRecipe implements SmithingRecipe {
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;

    public AbilityRecipe(Ingredient template, Ingredient base, Ingredient addition) {
        this.template = template;
        this.base = base;
        this.addition = addition;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.SMITHING;
    }

    public boolean matches(SmithingRecipeInput p_346082_, Level p_345460_) {
        return isTemplateIngredient(p_346082_.template()) && isBaseIngredient(p_346082_.base()) && isAdditionIngredient(p_346082_.addition());
    }

    public ItemStack assemble(SmithingRecipeInput smithingRecipeInput, HolderLookup.Provider provider) {
        ItemStack base = smithingRecipeInput.base();
        ItemStack upgrade = smithingRecipeInput.addition();
        if (isBaseIngredient(base)) {
            Ability ability = Ability.getAbilityFromUpgradeItem(upgrade.getItem());
            if (ability != null && !ToggleableTool.hasUpgrade(base, ability) && Config.AVAILABLE_ABILITY_MAP.get(ability).get()) {
                ItemStack itemstack1 = base.copyWithCount(1);
                itemstack1.set(JustDireDataComponents.ABILITY_UPGRADE_INSTALLS.get(ability), true);
                return itemstack1;
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        ItemStack itemstack = new ItemStack(getBase().getItems()[0].getItem());
        Ability ability = Ability.getAbilityFromUpgradeItem(getAddition().getItems()[0].getItem());
        if (!Config.AVAILABLE_ABILITY_MAP.get(ability).get())
            return new ItemStack(Items.AIR);
        itemstack.set(JustDireDataComponents.ABILITY_UPGRADE_INSTALLS.get(ability), true);

        return itemstack;
    }

    @Override
    public boolean isTemplateIngredient(ItemStack stack) {
        return stack.getItem() == Registration.UPGRADE_BASE.get();
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack) {
        return stack.getItem() instanceof ToggleableTool;
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return stack.getItem() instanceof UpgradeBlank;
    }

    public Ingredient getTemplate() {
        return template;
    }

    public Ingredient getBase() {
        return base;
    }

    public Ingredient getAddition() {
        return addition;
    }

    @Override
    public boolean isIncomplete() {
        return Stream.of(this.template, this.base, this.addition).anyMatch(Ingredient::hasNoItems);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Registration.ABILITY_RECIPE_SERIALIZER.get();
    }


    public static class Serializer implements RecipeSerializer<AbilityRecipe> {
        private static final ResourceLocation NAME = ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "ability");
        private static final MapCodec<AbilityRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p_311734_ -> p_311734_.group(
                                Ingredient.CODEC.fieldOf("template").forGetter(p_301070_ -> p_301070_.template),
                                Ingredient.CODEC.fieldOf("base").forGetter(p_300969_ -> p_300969_.base),
                                Ingredient.CODEC.fieldOf("addition").forGetter(p_300977_ -> p_300977_.addition)
                        )
                        .apply(p_311734_, AbilityRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, AbilityRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, AbilityRecipe::getTemplate,
                Ingredient.CONTENTS_STREAM_CODEC, AbilityRecipe::getBase,
                Ingredient.CONTENTS_STREAM_CODEC, AbilityRecipe::getAddition,
                AbilityRecipe::new
        );


        @Override
        public MapCodec<AbilityRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AbilityRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
