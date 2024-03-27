package com.direwolf20.justdirethings.datagen.recipes;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.blockentities.basebe.GooBlockBE_Base;
import com.direwolf20.justdirethings.setup.Registration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class GooSpreadRecipe implements CraftingRecipe {
    private final ResourceLocation id;
    protected final BlockState input;
    protected final BlockState output;
    protected int tierRequirement;
    protected int craftingDuration;

    public GooSpreadRecipe(ResourceLocation id, BlockState input, BlockState output, int tierRequirement, int craftingDuration) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.tierRequirement = tierRequirement;
        this.craftingDuration = craftingDuration;
    }

    @Override
    public RecipeType<?> getType() {
        return Registration.GOO_SPREAD_RECIPE_TYPE.get();
    }

    public boolean matches(Level level, BlockPos blockPos, GooBlockBE_Base gooBlockBE_base, BlockState sourceState) {
        return sourceState.equals(input) && gooBlockBE_base.getTier() >= tierRequirement;
    }

    public BlockState getOutput() {
        return output;
    }

    public BlockState getInput() {
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
        return Registration.GOO_SPREAD_RECIPE_SERIALIZER.get();
    }


    public static class Serializer implements RecipeSerializer<GooSpreadRecipe> {
        private static final net.minecraft.resources.ResourceLocation NAME = new net.minecraft.resources.ResourceLocation(JustDireThings.MODID, "goospread");
        private static final Codec<GooSpreadRecipe> CODEC = RecordCodecBuilder.create(
                p_311734_ -> p_311734_.group(
                                ResourceLocation.CODEC.fieldOf("id").forGetter(p_301134_ -> p_301134_.id),
                                BlockState.CODEC.fieldOf("input").forGetter(p_301135_ -> p_301135_.input),
                                BlockState.CODEC.fieldOf("output").forGetter(p_301136_ -> p_301136_.output),
                                Codec.INT.fieldOf("tierRequirement").forGetter(p_301137_ -> p_301137_.tierRequirement),
                                Codec.INT.fieldOf("craftingDuration").forGetter(p_301138_ -> p_301138_.craftingDuration)
                        )
                        .apply(p_311734_, GooSpreadRecipe::new)
        );

        @Override
        public Codec<GooSpreadRecipe> codec() {
            return CODEC;
        }

        public GooSpreadRecipe fromNetwork(FriendlyByteBuf pBuffer) {
            ResourceLocation resourceLocation = pBuffer.readResourceLocation();
            BlockState inputState = Block.stateById(pBuffer.readInt());
            BlockState outputState = Block.stateById(pBuffer.readInt());
            int tierRequirement = pBuffer.readInt();
            int craftingDuration = pBuffer.readInt();

            return new GooSpreadRecipe(resourceLocation, inputState, outputState, tierRequirement, craftingDuration);
        }

        public void toNetwork(FriendlyByteBuf pBuffer, GooSpreadRecipe pRecipe) {
            pBuffer.writeResourceLocation(pRecipe.id);
            pBuffer.writeInt(Block.getId(pRecipe.input));
            pBuffer.writeInt(Block.getId(pRecipe.output));
            pBuffer.writeInt(pRecipe.tierRequirement);
            pBuffer.writeInt(pRecipe.craftingDuration);
        }
    }
}
