package com.direwolf20.justdirethings.common.items.tools.utils;

import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.tags.TagKey;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

import static net.minecraft.tags.BlockTags.*;

public enum GooTier implements Tier {
    FERRICORE(INCORRECT_FOR_IRON_TOOL, 500, 7.0F, 2.5F, 15, () -> Ingredient.of(Registration.FerricoreIngot.get())),
    BLAZEGOLD(INCORRECT_FOR_DIAMOND_TOOL, 1440, 12.0F, 3.0F, 22, () -> Ingredient.of(Registration.BlazegoldIngot.get())),
    CELESTIGEM(INCORRECT_FOR_DIAMOND_TOOL, 1561, 10.0F, 4.0F, 18, () -> Ingredient.of(Registration.Celestigem.get())),
    ECLIPSEALLOY(INCORRECT_FOR_NETHERITE_TOOL, 2561, 16.0F, 5.0F, 25, () -> Ingredient.of(Registration.EclipseAlloyIngot.get()));

    private final TagKey<Block> incorrectBlocksForDrops;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    GooTier(TagKey<Block> incorrectBlocksForDrops, int pUses, float pSpeed, float pDamage, int pEnchantmentValue, Supplier<Ingredient> pRepairIngredient) {
        this.incorrectBlocksForDrops = incorrectBlocksForDrops;
        this.uses = pUses;
        this.speed = pSpeed;
        this.damage = pDamage;
        this.enchantmentValue = pEnchantmentValue;
        this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
    }

    @Override
    public int getUses() {
        return this.uses;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.damage;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return null;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
