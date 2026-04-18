package com.direwolf20.justdirethings.datagen.recipes;

import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseAxe;
import com.direwolf20.justdirethings.common.items.tools.basetools.BasePickaxe;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseShovel;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public class PaxelRecipe implements SmithingRecipe {
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final ItemStack result;

    public PaxelRecipe(Ingredient template, Ingredient base, Ingredient addition, ItemStack result) {
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    @Override
    public boolean matches(SmithingRecipeInput input, Level level) {
        return this.template.test(input.template()) && this.base.test(input.base()) && this.addition.test(input.addition());
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput smithingRecipeInput) {
        ItemStack pickaxe = smithingRecipeInput.template();
        ItemStack axe = smithingRecipeInput.base();
        ItemStack shovel = smithingRecipeInput.addition();
        ItemStack result = this.result.copy();

        if (pickaxe.getItem() instanceof BasePickaxe && pickaxe.getItem() instanceof ToggleableTool pickaxetoggleableTool) {
            for (Ability ability : pickaxetoggleableTool.getAbilities()) {
                if (ToggleableTool.hasUpgrade(pickaxe, ability))
                    result.set(JustDireDataComponents.ABILITY_UPGRADE_INSTALLS.get(ability), true);
            }
        } else {
            return ItemStack.EMPTY;
        }
        if (axe.getItem() instanceof BaseAxe && axe.getItem() instanceof ToggleableTool axetoggleableTool) {
            for (Ability ability : axetoggleableTool.getAbilities()) {
                if (ToggleableTool.hasUpgrade(axe, ability))
                    result.set(JustDireDataComponents.ABILITY_UPGRADE_INSTALLS.get(ability), true);
            }
        } else {
            return ItemStack.EMPTY;
        }
        if (shovel.getItem() instanceof BaseShovel && shovel.getItem() instanceof ToggleableTool shoveltoggleableTool) {
            for (Ability ability : shoveltoggleableTool.getAbilities()) {
                if (ToggleableTool.hasUpgrade(shovel, ability))
                    result.set(JustDireDataComponents.ABILITY_UPGRADE_INSTALLS.get(ability), true);
            }
        } else {
            return ItemStack.EMPTY;
        }

        ItemEnchantments pickaxeEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(pickaxe);
        ItemEnchantments axeEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(axe);
        ItemEnchantments shovelEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(shovel);

        EnchantmentHelper.updateEnchantments(result, enchantments -> {
            Set<Holder<Enchantment>> appliedEnchantments = new HashSet<>();

            BiConsumer<ItemEnchantments, String> addEnchantments = (itemEnchantments, toolName) -> {
                itemEnchantments.entrySet().forEach(entry -> {
                    Holder<Enchantment> enchantment = entry.getKey();
                    int level = entry.getIntValue();

                    if (appliedEnchantments.stream().allMatch(existing -> Enchantment.areCompatible(existing, enchantment))) {
                        enchantments.set(enchantment, level);
                        appliedEnchantments.add(enchantment);
                    } else {
                        System.out.println("Skipping incompatible enchantment from " + toolName + ": " + enchantment);
                    }
                });
            };

            addEnchantments.accept(pickaxeEnchantments, "pickaxe");
            addEnchantments.accept(axeEnchantments, "axe");
            addEnchantments.accept(shovelEnchantments, "shovel");
        });

        return result;
    }

    public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider provider) {
        return result.copy();
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

    public ItemStack getResult() {
        return result.copy();
    }

    @Override
    public Optional<Ingredient> templateIngredient() {
        return template.isEmpty() ? Optional.empty() : Optional.of(template);
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
        return JDTRegistration.PAXEL_RECIPE_SERIALIZER.get();
    }

    public static final MapCodec<PaxelRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Ingredient.CODEC.fieldOf("template").forGetter(r -> r.template),
                            Ingredient.CODEC.fieldOf("base").forGetter(r -> r.base),
                            Ingredient.CODEC.fieldOf("addition").forGetter(r -> r.addition),
                            ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result)
                    )
                    .apply(instance, PaxelRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, PaxelRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, PaxelRecipe::getTemplate,
            Ingredient.CONTENTS_STREAM_CODEC, PaxelRecipe::getBase,
            Ingredient.CONTENTS_STREAM_CODEC, PaxelRecipe::getAddition,
            ItemStack.STREAM_CODEC, PaxelRecipe::getResult,
            PaxelRecipe::new
    );
}
