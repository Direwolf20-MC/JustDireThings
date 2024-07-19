package com.direwolf20.justdirethings.common.events;

import com.direwolf20.justdirethings.common.items.tools.basetools.BaseBow;
import com.direwolf20.justdirethings.datagen.recipes.FluidDropRecipe;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class EntityEvents {
    public record FluidInputs(BlockState blockState, Item item) {
    }

    static Map<FluidInputs, BlockState> fluidCraftCache = new HashMap<>();

    @SubscribeEvent
    public static void livingUseItem(LivingEntityUseItemEvent.Start event) {
        if (event.getEntity() instanceof Player && event.getItem().getItem() instanceof BaseBow baseBow) {
            event.setDuration(event.getDuration() - (20 - (int) baseBow.getMaxDraw()));
        }
    }

    @SubscribeEvent
    public static void entityTick(EntityTickEvent.Post e) {
        Entity entity = e.getEntity();
        Level level = entity.level();
        if (!(entity instanceof ItemEntity itemEntity)) return;
        BlockState blockState = entity.getInBlockState();
        if (!(blockState.getBlock() instanceof LiquidBlock)) return;
        BlockState fluidDropOutput = findRecipe(blockState, itemEntity);
        if (!fluidDropOutput.isAir()) {
            BlockPos blockPos = entity.blockPosition();
            if (level.setBlockAndUpdate(blockPos, fluidDropOutput)) {
                itemEntity.getItem().shrink(1);
                FluidStack fluidStack = new FluidStack(level.getFluidState(blockPos).getType(), 1000);
                FluidType fluidType = fluidStack.getFluidType();
                if (fluidType.isVaporizedOnPlacement(level, blockPos, fluidStack)) {
                    level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                    fluidType.onVaporize(null, level, blockPos, fluidStack);
                } else {
                    if (!level.isClientSide)
                        level.playSound(null, blockPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }

    @Nullable
    private static BlockState findRecipe(BlockState blockState, ItemEntity entity) {
        FluidInputs fluidInputs = new FluidInputs(blockState, entity.getItem().getItem());
        if (fluidCraftCache.containsKey(fluidInputs))
            return fluidCraftCache.get(fluidInputs);
        RecipeManager recipeManager = entity.level().getRecipeManager();

        for (RecipeHolder<?> recipe : recipeManager.getAllRecipesFor(Registration.FLUID_DROP_RECIPE_TYPE.get())) {
            if (recipe.value() instanceof FluidDropRecipe fluidDropRecipe && fluidDropRecipe.matches(blockState, entity.getItem())) {
                fluidCraftCache.put(fluidInputs, fluidDropRecipe.getOutput());
                break;
            }
        }
        if (!fluidCraftCache.containsKey(fluidInputs))
            fluidCraftCache.put(fluidInputs, Blocks.AIR.defaultBlockState());
        return fluidCraftCache.get(fluidInputs);
    }

    private static void clearCache() {
        fluidCraftCache.clear();
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent e) {
        clearCache();
    }

    @SubscribeEvent
    public static void onReloadServerResources(AddReloadListenerEvent e) {
        clearCache();
    }

    @SubscribeEvent
    public static void onClientRecipesUpdated(RecipesUpdatedEvent e) {
        clearCache();
    }
}
