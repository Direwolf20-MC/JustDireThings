package com.direwolf20.justdirethings.datagen.recipes;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.blockentities.basebe.GooBlockBE_Base;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.crafting.BlockTagIngredient;

public class GooSpreadRecipeTag implements CraftingRecipe {
    private final Identifier id;
    protected final BlockTagIngredient input;
    protected final BlockState output;
    protected int tierRequirement;
    protected int craftingDuration;

    public GooSpreadRecipeTag(Identifier id, BlockTagIngredient input, BlockState output, int tierRequirement, int craftingDuration) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.tierRequirement = tierRequirement;
        this.craftingDuration = craftingDuration;
    }

    @Override
    public RecipeType<?> getType() {
        return Registration.GOO_SPREAD_RECIPE_TYPE_TAG.get();
    }

    public boolean matches(GooBlockBE_Base gooBlockBE_base, BlockState sourceState) {
        return sourceState.is(input.getTag()) && gooBlockBE_base.getTier() >= tierRequirement;
    }

    public BlockState getOutput() {
        return output;
    }

    public BlockTagIngredient getInput() {
        return input;
    }

    public int getTierRequirement() {
        return tierRequirement;
    }

    public int getCraftingDuration() {
        return craftingDuration;
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean matches(CraftingInput p_346065_, Level p_345375_) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInput p_345149_, HolderLookup.Provider p_346030_) {
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
        return Registration.GOO_SPREAD_RECIPE_SERIALIZER_TAG.get();
    }


    public static final MapCodec<GooSpreadRecipeTag> CODEC = RecordCodecBuilder.mapCodec(
            p_311734_ -> p_311734_.group(
                            Identifier.CODEC.fieldOf("id").forGetter(p_301134_ -> p_301134_.id),
                            BlockTagIngredient.CODEC.fieldOf("input").forGetter(p_301135_ -> p_301135_.input),
                            BlockState.CODEC.fieldOf("output").forGetter(p_301136_ -> p_301136_.output),
                            Codec.INT.fieldOf("tierRequirement").forGetter(p_301137_ -> p_301137_.tierRequirement),
                            Codec.INT.fieldOf("craftingDuration").forGetter(p_301138_ -> p_301138_.craftingDuration)
                    )
                    .apply(p_311734_, GooSpreadRecipeTag::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, GooSpreadRecipeTag> STREAM_CODEC = StreamCodec.of(
            GooSpreadRecipeTag::toNetwork, GooSpreadRecipeTag::fromNetwork
    );

    public static GooSpreadRecipeTag fromNetwork(RegistryFriendlyByteBuf pBuffer) {
        Identifier resourceLocation = pBuffer.readIdentifier();
        Identifier tagLocation = pBuffer.readIdentifier(); // Read the tag's Identifier
        BlockTagIngredient inputIngredient = new BlockTagIngredient(TagKey.create(Registries.BLOCK, tagLocation)); // Create the BlockTagIngredient
        BlockState outputState = Block.stateById(pBuffer.readInt());
        int tierRequirement = pBuffer.readInt();
        int craftingDuration = pBuffer.readInt();

        return new GooSpreadRecipeTag(resourceLocation, inputIngredient, outputState, tierRequirement, craftingDuration);
    }

    public static void toNetwork(RegistryFriendlyByteBuf pBuffer, GooSpreadRecipeTag pRecipe) {
        pBuffer.writeIdentifier(pRecipe.id);
        pBuffer.writeIdentifier(pRecipe.input.getTag().location()); // Write the tag's Identifier
        pBuffer.writeInt(Block.getId(pRecipe.output));
        pBuffer.writeInt(pRecipe.tierRequirement);
        pBuffer.writeInt(pRecipe.craftingDuration);
    }
}
