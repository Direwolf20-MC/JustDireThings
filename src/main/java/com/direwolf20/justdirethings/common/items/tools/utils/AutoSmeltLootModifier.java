package com.direwolf20.justdirethings.common.items.tools.utils;

import com.direwolf20.justdirethings.setup.Registration;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.function.Supplier;

/**
 * An alternate way to smelt drops - I wrote all this, but went back to my old approach. Will re-implement if told its necessary
 */
public class AutoSmeltLootModifier extends LootModifier {
    public static final Supplier<Codec<AutoSmeltLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, AutoSmeltLootModifier::new)));


    public AutoSmeltLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool != null && tool.getItem() instanceof ToggleableTool toggleableTool) {
            if (toggleableTool.canUseAbility(tool, Ability.SMELTER)) {
                ObjectArrayList<ItemStack> ret = new ObjectArrayList<ItemStack>();
                generatedLoot.forEach((stack) -> ret.add(smelt(stack, context)));
                return ret;
            }
        }
        return generatedLoot;
    }

    private static ItemStack smelt(ItemStack stack, LootContext context) {
        return context.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), context.getLevel())
                .map(smeltingRecipe -> smeltingRecipe.value().getResultItem(context.getLevel().registryAccess()))
                .filter(itemStack -> !itemStack.isEmpty())
                .map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, stack.getCount() * itemStack.getCount()))
                .orElse(stack);
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return Registration.AUTO_SMELT_LOOT_MODIFIER.get();
    }
}