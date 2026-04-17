package com.direwolf20.justdirethings.datagen.recipes;

import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class AbilityRecipe implements SmithingRecipe {
    private final Optional<Ingredient> template;
    private final Ingredient base;
    private final Ingredient addition;

    public AbilityRecipe(Optional<Ingredient> template, Ingredient base, Ingredient addition) {
        this.template = template;
        this.base = base;
        this.addition = addition;
    }

    @Override
    public boolean matches(SmithingRecipeInput input, Level level) {
        return Ingredient.testOptionalIngredient(this.template, input.template())
                && this.base.test(input.base())
                && this.addition.test(input.addition());
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput smithingRecipeInput) {
        ItemStack base = smithingRecipeInput.base();
        ItemStack upgrade = smithingRecipeInput.addition();
        if (base.getItem() instanceof ToggleableTool toggleableTool) {
            Ability ability = Ability.getAbilityFromUpgradeItem(upgrade.getItem());
            if (ability != null && toggleableTool.hasAbility(ability) && !ToggleableTool.hasUpgrade(base, ability) && Config.AVAILABLE_ABILITY_MAP.get(ability).get()) {
                ItemStack itemstack1 = base.copyWithCount(1);
                itemstack1.set(JustDireDataComponents.ABILITY_UPGRADE_INSTALLS.get(ability), true);
                return itemstack1;
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider provider) {
        ItemStack baseStack = this.base.items().findFirst().map(h -> new ItemStack(h.value())).orElse(ItemStack.EMPTY);
        if (baseStack.isEmpty()) return ItemStack.EMPTY;
        Ability ability = this.addition.items().findFirst().map(h -> Ability.getAbilityFromUpgradeItem(h.value())).orElse(null);
        if (ability == null) return ItemStack.EMPTY;

        if (Config.SERVER_CONFIG.isLoaded() && !Config.AVAILABLE_ABILITY_MAP.get(ability).get())
            return new ItemStack(Items.AIR);

        baseStack.set(JustDireDataComponents.ABILITY_UPGRADE_INSTALLS.get(ability), true);
        return baseStack;
    }

    public Optional<Ingredient> getTemplate() {
        return template;
    }

    public Ingredient getBase() {
        return base;
    }

    public Ingredient getAddition() {
        return addition;
    }

    @Override
    public Optional<Ingredient> templateIngredient() {
        return template;
    }

    @Override
    public Ingredient baseIngredient() {
        return base;
    }

    @Override
    public Optional<Ingredient> additionIngredient() {
        return addition.isEmpty() ? Optional.empty() : Optional.of(addition);
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeSerializer<? extends SmithingRecipe> getSerializer() {
        return Registration.ABILITY_RECIPE_SERIALIZER.get();
    }

    public static final MapCodec<AbilityRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Ingredient.CODEC.optionalFieldOf("template").forGetter(r -> r.template),
                            Ingredient.CODEC.fieldOf("base").forGetter(r -> r.base),
                            Ingredient.CODEC.fieldOf("addition").forGetter(r -> r.addition)
                    )
                    .apply(instance, AbilityRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, AbilityRecipe::getTemplate,
            Ingredient.CONTENTS_STREAM_CODEC, AbilityRecipe::getBase,
            Ingredient.CONTENTS_STREAM_CODEC, AbilityRecipe::getAddition,
            AbilityRecipe::new
    );
}
