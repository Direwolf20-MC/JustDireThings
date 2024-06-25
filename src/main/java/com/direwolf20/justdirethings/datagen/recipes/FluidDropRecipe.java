package com.direwolf20.justdirethings.datagen.recipes;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FluidDropRecipe implements CraftingRecipe {
    private final ResourceLocation id;
    protected final BlockState input;
    protected final BlockState output;
    protected final Item catalyst;

    public FluidDropRecipe(ResourceLocation id, BlockState input, BlockState output, Item catalyst) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.catalyst = catalyst;
    }

    public FluidDropRecipe(ResourceLocation id, BlockState input, BlockState output, Holder<Item> catalyst) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.catalyst = catalyst.value();
    }

    @Override
    public RecipeType<?> getType() {
        return Registration.FLUID_DROP_RECIPE_TYPE.get();
    }

    public boolean matches(BlockState blockState, ItemStack catalystStack) {
        if (!catalystStack.is(catalyst)) return false;
        return /*blockState.getBlock() instanceof LiquidBlock liquidBlock &&*/ blockState.getFluidState().is(input.getFluidState().getType()) && blockState.getFluidState().isSource();
    }

    public ResourceLocation getId() {
        return id;
    }

    public BlockState getOutput() {
        return output;
    }

    public BlockState getInput() {
        return input;
    }

    public Item getCatalyst() {
        return catalyst;
    }

    public Holder<Item> getCatalystHolder() {
        return catalyst == null ? null : catalyst.builtInRegistryHolder();
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
        return Registration.FLUID_DROP_RECIPE_SERIALIZER.get();
    }


    public static class Serializer implements RecipeSerializer<FluidDropRecipe> {
        private static final ResourceLocation NAME = ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "fluiddrop");
        private static final MapCodec<FluidDropRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p_311734_ -> p_311734_.group(
                                ResourceLocation.CODEC.fieldOf("id").forGetter(p_301134_ -> p_301134_.id),
                                BlockState.CODEC.fieldOf("input").forGetter(p_301135_ -> p_301135_.input),
                                BlockState.CODEC.fieldOf("output").forGetter(p_301136_ -> p_301136_.output),
                                ItemStack.ITEM_NON_AIR_CODEC.fieldOf("catalyst").forGetter(p_301137_ -> p_301137_.catalyst.builtInRegistryHolder())
                        )
                        .apply(p_311734_, FluidDropRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, FluidDropRecipe> STREAM_CODEC = StreamCodec.composite(
                ResourceLocation.STREAM_CODEC, FluidDropRecipe::getId,
                ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY), FluidDropRecipe::getInput,
                ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY), FluidDropRecipe::getOutput,
                ByteBufCodecs.holderRegistry(Registries.ITEM), FluidDropRecipe::getCatalystHolder,
                FluidDropRecipe::new
        );


        @Override
        public MapCodec<FluidDropRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FluidDropRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
