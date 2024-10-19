package com.direwolf20.justdirethings.datagen.recipes;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseAxe;
import com.direwolf20.justdirethings.common.items.tools.basetools.BasePickaxe;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseShovel;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
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
import java.util.stream.Stream;

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
    public RecipeType<?> getType() {
        return RecipeType.SMITHING;
    }

    public boolean matches(SmithingRecipeInput p_346082_, Level p_345460_) {
        return this.template.test(p_346082_.template()) && this.base.test(p_346082_.base()) && this.addition.test(p_346082_.addition());
    }

    public ItemStack assemble(SmithingRecipeInput smithingRecipeInput, HolderLookup.Provider provider) {
        ItemStack pickaxe = smithingRecipeInput.template();
        ItemStack axe = smithingRecipeInput.base();
        ItemStack shovel = smithingRecipeInput.addition();
        ItemStack result = getResultItem(provider);

        if (isTemplateIngredient(pickaxe) && pickaxe.getItem() instanceof ToggleableTool pickaxetoggleableTool) {
            for (Ability ability : pickaxetoggleableTool.getAbilities()) {
                if (ToggleableTool.hasUpgrade(pickaxe, ability))
                    result.set(JustDireDataComponents.ABILITY_UPGRADE_INSTALLS.get(ability), true);
            }

            Optional<? extends ItemEnchantments> enchantments = pickaxe.getComponentsPatch().get(DataComponents.ENCHANTMENTS);

        } else {
            return ItemStack.EMPTY;
        }
        if (isBaseIngredient(axe) && axe.getItem() instanceof ToggleableTool axetoggleableTool) {
            for (Ability ability : axetoggleableTool.getAbilities()) {
                if (ToggleableTool.hasUpgrade(axe, ability))
                    result.set(JustDireDataComponents.ABILITY_UPGRADE_INSTALLS.get(ability), true);
            }
        } else {
            return ItemStack.EMPTY;
        }
        if (isAdditionIngredient(shovel) && shovel.getItem() instanceof ToggleableTool shoveltoggleableTool) {
            for (Ability ability : shoveltoggleableTool.getAbilities()) {
                if (ToggleableTool.hasUpgrade(shovel, ability))
                    result.set(JustDireDataComponents.ABILITY_UPGRADE_INSTALLS.get(ability), true);
            }
        } else {
            return ItemStack.EMPTY;
        }

        // Transfer and combine enchantments from pickaxe, axe, and shovel
        ItemEnchantments pickaxeEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(pickaxe);
        ItemEnchantments axeEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(axe);
        ItemEnchantments shovelEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(shovel);

        // Apply combined enchantments to the result
        EnchantmentHelper.updateEnchantments(result, enchantments -> {
            // A set to store the already applied enchantments for compatibility checks
            Set<Holder<Enchantment>> appliedEnchantments = new HashSet<>();

            // Helper function to check and add enchantments while ensuring compatibility
            BiConsumer<ItemEnchantments, String> addEnchantments = (itemEnchantments, toolName) -> {
                itemEnchantments.entrySet().forEach(entry -> {
                    Holder<Enchantment> enchantment = entry.getKey();
                    int level = entry.getIntValue();

                    // Ensure the enchantment is compatible with already applied enchantments
                    if (appliedEnchantments.stream().allMatch(existing -> Enchantment.areCompatible(existing, enchantment))) {
                        enchantments.set(enchantment, level); // Add the enchantment to the result
                        appliedEnchantments.add(enchantment); // Track the added enchantment
                    } else {
                        System.out.println("Skipping incompatible enchantment from " + toolName + ": " + enchantment);
                    }
                });
            };

            // Add enchantments from each tool with compatibility checks
            addEnchantments.accept(pickaxeEnchantments, "pickaxe");
            addEnchantments.accept(axeEnchantments, "axe");
            addEnchantments.accept(shovelEnchantments, "shovel");
        });

        return result;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public boolean isTemplateIngredient(ItemStack stack) {
        return stack.getItem() instanceof BasePickaxe;
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack) {
        return stack.getItem() instanceof BaseAxe;
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return stack.getItem() instanceof BaseShovel;
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
    public boolean isIncomplete() {
        return Stream.of(this.template, this.base, this.addition).anyMatch(Ingredient::hasNoItems);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Registration.PAXEL_RECIPE_SERIALIZER.get();
    }


    public static class Serializer implements RecipeSerializer<PaxelRecipe> {
        private static final ResourceLocation NAME = ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "paxel");
        private static final MapCodec<PaxelRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p_311734_ -> p_311734_.group(
                                Ingredient.CODEC.fieldOf("template").forGetter(p_301070_ -> p_301070_.template),
                                Ingredient.CODEC.fieldOf("base").forGetter(p_300969_ -> p_300969_.base),
                                Ingredient.CODEC.fieldOf("addition").forGetter(p_300977_ -> p_300977_.addition),
                                net.minecraft.world.item.ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_300935_ -> p_300935_.result)
                        )
                        .apply(p_311734_, PaxelRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, PaxelRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, PaxelRecipe::getTemplate,
                Ingredient.CONTENTS_STREAM_CODEC, PaxelRecipe::getBase,
                Ingredient.CONTENTS_STREAM_CODEC, PaxelRecipe::getAddition,
                ItemStack.STREAM_CODEC, PaxelRecipe::getResult,
                PaxelRecipe::new
        );


        @Override
        public MapCodec<PaxelRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PaxelRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
