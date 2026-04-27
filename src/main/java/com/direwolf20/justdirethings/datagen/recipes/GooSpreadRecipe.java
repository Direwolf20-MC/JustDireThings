package com.direwolf20.justdirethings.datagen.recipes;

import com.direwolf20.justdirethings.common.blockentities.basebe.GooBlockBE_Base;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class GooSpreadRecipe implements CraftingRecipe {
    protected final BlockOrTagInput input;
    protected final BlockState output;
    protected final int tierRequirement;
    protected final int craftingDuration;

    public GooSpreadRecipe(BlockOrTagInput input, BlockState output, int tierRequirement, int craftingDuration) {
        this.input = input;
        this.output = output;
        this.tierRequirement = tierRequirement;
        this.craftingDuration = craftingDuration;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RecipeType<CraftingRecipe> getType() {
        return (RecipeType<CraftingRecipe>) (RecipeType<?>) JDTRegistration.GOO_SPREAD_RECIPE_TYPE.get();
    }

    public boolean matches(GooBlockBE_Base gooBlockBE_base, BlockState sourceState) {
        return input.matches(sourceState) && gooBlockBE_base.getTier() >= tierRequirement;
    }

    public BlockState getOutput() {
        return output;
    }

    public BlockOrTagInput getInput() {
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
        return JDTRegistration.GOO_SPREAD_RECIPE_SERIALIZER.get();
    }

    public static final MapCodec<GooSpreadRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            BlockOrTagInput.CODEC.fieldOf("input").forGetter(r -> r.input),
                            RecipeCodecs.BLOCK_STATE.fieldOf("output").forGetter(r -> r.output),
                            Codec.INT.fieldOf("tierRequirement").forGetter(r -> r.tierRequirement),
                            Codec.INT.fieldOf("craftingDuration").forGetter(r -> r.craftingDuration)
                    )
                    .apply(instance, GooSpreadRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, GooSpreadRecipe> STREAM_CODEC = StreamCodec.of(
            GooSpreadRecipe::toNetwork, GooSpreadRecipe::fromNetwork
    );

    public static GooSpreadRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
        BlockOrTagInput inputValue = BlockOrTagInput.STREAM_CODEC.decode(pBuffer);
        BlockState outputState = Block.stateById(pBuffer.readInt());
        int tierRequirement = pBuffer.readInt();
        int craftingDuration = pBuffer.readInt();

        return new GooSpreadRecipe(inputValue, outputState, tierRequirement, craftingDuration);
    }

    public static void toNetwork(RegistryFriendlyByteBuf pBuffer, GooSpreadRecipe pRecipe) {
        BlockOrTagInput.STREAM_CODEC.encode(pBuffer, pRecipe.input);
        pBuffer.writeInt(Block.getId(pRecipe.output));
        pBuffer.writeInt(pRecipe.tierRequirement);
        pBuffer.writeInt(pRecipe.craftingDuration);
    }
}
