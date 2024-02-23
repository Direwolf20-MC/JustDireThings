package com.direwolf20.justdirethings.common.blockentities.gooblocks;

import com.direwolf20.justdirethings.datagen.recipes.GooSpreadRecipe;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GooBlockBE_Base extends BlockEntity {
    public GooBlockBE_Base(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tickClient() {

    }

    public void tickServer() {
        findRecipe(getBlockPos().above());
    }

    @Nullable
    private GooSpreadRecipe findRecipe(BlockPos coords) {
        BlockState state = getLevel().getBlockState(coords);
        RecipeManager recipeManager = getLevel().getRecipeManager();

        for (RecipeHolder<?> recipe : recipeManager.getAllRecipesFor(Registration.GOO_SPREAD_RECIPE_TYPE.get())) {
            if (recipe.value() instanceof GooSpreadRecipe gooSpreadRecipe && gooSpreadRecipe.matches(getLevel(), coords, this, state)) {
                return gooSpreadRecipe;
            }
        }

        return null;
    }
}
