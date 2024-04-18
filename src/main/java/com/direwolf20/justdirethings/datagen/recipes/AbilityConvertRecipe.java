package com.direwolf20.justdirethings.datagen.recipes;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;


public class AbilityConvertRecipe implements CraftingRecipe {
    protected final ItemStack input;
    protected final ItemStack output;
    protected String abilityRequirement;
    
    public AbilityConvertRecipe(ItemStack input, ItemStack output, String abilityRequirement) {
        this.input = input;
        this.output = output;
        this.abilityRequirement = abilityRequirement;
    }

    @Override
    public RecipeType<?> getType() {
        return Registration.ABILITY_CONVERT_RECIPE_TYPE.get();
    }

    public boolean matches(Level level, ItemStack tool) {
    	if (tool.getItem() instanceof ToggleableTool toggleableTool) {    		
    		return toggleableTool.canUseAbility(tool, Ability.valueOf(abilityRequirement.toUpperCase()));
    	}
    	return false;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getInput() {
        return input;
    }

    public Ability getAbilityRequirement() {
        return Ability.valueOf(abilityRequirement.toUpperCase());
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public boolean matches(CraftingContainer pInv, Level pLevel) {
        return false;
    }

    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Registration.ABILITY_CONVERT_RECIPE_SERIALIZER.get();
    }


    public static class Serializer implements RecipeSerializer<AbilityConvertRecipe> {
        private static final net.minecraft.resources.ResourceLocation NAME = new net.minecraft.resources.ResourceLocation(JustDireThings.MODID, "autosmelt");
        private static final Codec<AbilityConvertRecipe> CODEC = RecordCodecBuilder.create(
                p_311734_ -> p_311734_.group(
                		ItemStack.CODEC.fieldOf("input").forGetter(p_301135_ -> p_301135_.input),
                		ItemStack.CODEC.fieldOf("output").forGetter(p_301136_ -> p_301136_.output),
                                Codec.STRING.fieldOf("abilityRequirement").forGetter(p_301137_ -> p_301137_.abilityRequirement)
                        )
                        .apply(p_311734_, AbilityConvertRecipe::new)
        );

        @Override
        public Codec<AbilityConvertRecipe> codec() {
            return CODEC;
        }

        public AbilityConvertRecipe fromNetwork(FriendlyByteBuf pBuffer) {
        	ItemStack inputState = pBuffer.readItem();
        	ItemStack outputState = pBuffer.readItem();
        	String abilityRequirement = pBuffer.readUtf(256);

            return new AbilityConvertRecipe(inputState, outputState, abilityRequirement);
        }

        public void toNetwork(FriendlyByteBuf pBuffer, AbilityConvertRecipe pRecipe) {
            pBuffer.writeItem(pRecipe.input);
            pBuffer.writeItem(pRecipe.output);
            pBuffer.writeUtf(pRecipe.abilityRequirement);
        }
    }
}
