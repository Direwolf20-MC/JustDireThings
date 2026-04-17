package com.direwolf20.justdirethings.datagen.recipes;

import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FluidDropRecipe implements CraftingRecipe {
    private final Identifier id;
    protected final BlockState input;
    protected final BlockState output;
    protected final Item catalyst;

    public FluidDropRecipe(Identifier id, BlockState input, BlockState output, Item catalyst) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.catalyst = catalyst;
    }

    public FluidDropRecipe(Identifier id, BlockState input, BlockState output, Holder<Item> catalyst) {
        this(id, input, output, catalyst.value());
    }

    @Override
    @SuppressWarnings("unchecked")
    public RecipeType<CraftingRecipe> getType() {
        return (RecipeType<CraftingRecipe>) (RecipeType<?>) Registration.FLUID_DROP_RECIPE_TYPE.get();
    }

    public boolean matches(BlockState blockState, ItemStack catalystStack) {
        if (!catalystStack.is(catalyst)) return false;
        return blockState.getFluidState().is(input.getFluidState().getType()) && blockState.getFluidState().isSource();
    }

    public Identifier getId() {
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
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean showNotification() {
        return false;
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
    public RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return Registration.FLUID_DROP_RECIPE_SERIALIZER.get();
    }

    public static final MapCodec<FluidDropRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Identifier.CODEC.fieldOf("id").forGetter(r -> r.id),
                            BlockState.CODEC.fieldOf("input").forGetter(r -> r.input),
                            BlockState.CODEC.fieldOf("output").forGetter(r -> r.output),
                            Item.CODEC.fieldOf("catalyst").forGetter(r -> r.catalyst.builtInRegistryHolder())
                    )
                    .apply(instance, FluidDropRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidDropRecipe> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, FluidDropRecipe::getId,
            ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY), FluidDropRecipe::getInput,
            ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY), FluidDropRecipe::getOutput,
            ByteBufCodecs.holderRegistry(Registries.ITEM), FluidDropRecipe::getCatalystHolder,
            FluidDropRecipe::new
    );
}
