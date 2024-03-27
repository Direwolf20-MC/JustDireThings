package com.direwolf20.justdirethings.common.blocks.soil;

import com.direwolf20.justdirethings.common.blockentities.GooSoilBE;
import com.direwolf20.justdirethings.common.items.tools.utils.Helpers;
import com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.IPlantable;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.block.CactusBlock.AGE;

public class GooSoilBase extends FarmBlock {
    public GooSoilBase() {
        super(Properties.of()
                .sound(SoundType.GRAVEL)
                .strength(2.0f)
                .randomTicks()
        );
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState blockstate = pLevel.getBlockState(pPos.above());
        if (blockstate.is(Blocks.BAMBOO) || blockstate.is(Blocks.CACTUS) || blockstate.is(Blocks.BAMBOO_SAPLING))
            return true;
        return !blockstate.isSolid() || blockstate.getBlock() instanceof FenceGateBlock || blockstate.getBlock() instanceof MovingPistonBlock;
    }

    @Override
    public boolean canSustainPlant(BlockState blockState, BlockGetter level, BlockPos pos, Direction facing, IPlantable plantable) {
        BlockState plant = plantable.getPlant(level, pos.relative(facing));
        net.neoforged.neoforge.common.PlantType type = plantable.getPlantType(level, pos.relative(facing));

        if (plant.getBlock() == Blocks.CACTUS)
            return true;

        if (plant.getBlock() == Blocks.SUGAR_CANE)
            return true;

        if (plant.getBlock() == Blocks.BAMBOO || plant.getBlock() == Blocks.BAMBOO_SAPLING)
            return true;

        //if (plantable instanceof BushBlock)
        //    return true; //Bushes?

        if (net.neoforged.neoforge.common.PlantType.NETHER.equals(type)) {
            return true; //Netherwart
        } else if (net.neoforged.neoforge.common.PlantType.CROP.equals(type)) {
            return true; //Wheat
        } else if (net.neoforged.neoforge.common.PlantType.CAVE.equals(type)) {
            return false; //Mushrooms
        } else if (net.neoforged.neoforge.common.PlantType.PLAINS.equals(type)) {
            return false; //Saplings / Flowers -- Vanilla this is apparently true - who knew!?
        } else if (net.neoforged.neoforge.common.PlantType.WATER.equals(type)) {
            return (blockState.is(Blocks.WATER) || blockState.getBlock() instanceof IceBlock) && level.getFluidState(pos.relative(facing)).isEmpty();
        }
        return false;
    }

    @Override
    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
        pEntity.causeFallDamage(pFallDistance, 1.0F, pEntity.damageSources().fall());
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);
    }

    /**
     * Performs a random tick on a block.
     */
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
    }

    public static void bonemealMe(ServerLevel pLevel, BlockPos pPos) {
        BlockPos cropPos = pPos.above();
        BlockState crop = pLevel.getBlockState(cropPos);
        BlockState crop1 = null;
        if (crop.getBlock() instanceof CropBlock cropblock) {
            if (!cropblock.isMaxAge(crop)) {
                crop1 = cropblock.getStateForAge(cropblock.getAge(crop) + 1);
            }
        } else if (crop.getBlock() instanceof StemBlock) {
            int k = crop.getValue(StemBlock.AGE);
            if (k < 7) {
                crop1 = crop.setValue(StemBlock.AGE, k + 1);
            }
        } else if (crop.is(Blocks.SWEET_BERRY_BUSH)) {
            int j = crop.getValue(SweetBerryBushBlock.AGE);
            if (j < 3) {
                crop1 = crop.setValue(SweetBerryBushBlock.AGE, j + 1);
            }
        } else if (crop.is(Blocks.SUGAR_CANE) || crop.is(Blocks.CACTUS)) { //Bamboo is weird, Cactus and Sugarcane are at least consistent
            BlockPos blockpos = cropPos.above();
            int j = crop.getValue(AGE);
            if (j == 15) {
                pLevel.setBlockAndUpdate(blockpos, crop.getBlock().defaultBlockState());
                BlockState blockstate = crop.setValue(AGE, Integer.valueOf(0));
                pLevel.setBlock(cropPos, blockstate, 4);
                pLevel.neighborChanged(blockstate, blockpos, crop.getBlock(), cropPos, false);
            } else {
                pLevel.setBlock(cropPos, crop.setValue(AGE, Integer.valueOf(j + 1)), 4);
            }
        }
        if (crop1 != null) {
            pLevel.setBlockAndUpdate(cropPos, crop1);
        }
    }

    public static List<ItemStack> harvestCrop(ServerLevel pLevel, BlockPos cropPos, BlockState crop) {
        List<ItemStack> drops = new ArrayList<>();
        if (crop.getBlock() instanceof CropBlock cropBlock) {
            if (cropBlock.isMaxAge(crop)) {
                BlockState placeState = Blocks.AIR.defaultBlockState();
                BlockEntity blockEntity = pLevel.getBlockEntity(cropPos);
                drops.addAll(Block.getDrops(crop, pLevel, cropPos, blockEntity));
                for (ItemStack drop : drops) {
                    if (drop.getItem() instanceof BlockItem blockItem) {
                        placeState = blockItem.getBlock().defaultBlockState();
                        drop.shrink(1);
                        break;
                    }
                }
                pLevel.destroyBlock(cropPos, false);
                pLevel.setBlockAndUpdate(cropPos, placeState);
            }
        } else if (crop.is(Blocks.SUGAR_CANE) || crop.is(Blocks.CACTUS) || crop.is(Blocks.BAMBOO)) {
            List<BlockPos> posToCheck = new ArrayList<>();
            for (int i = 0; i < 10; i++) { //In case it grew a lot since last check
                BlockPos pos = cropPos.above(i);
                if (pLevel.getBlockState(pos).is(crop.getBlock()))
                    posToCheck.add(pos);
                else
                    break;
            }
            for (int i = posToCheck.size() - 1; i >= 0; i--) {
                BlockPos clearPos = posToCheck.get(i);
                BlockEntity blockEntity = pLevel.getBlockEntity(clearPos);
                drops.addAll(Block.getDrops(pLevel.getBlockState(clearPos), pLevel, clearPos, blockEntity));
                pLevel.destroyBlock(clearPos, false);
            }
        }

        return drops;
    }

    public static void teleportDrops(ServerLevel pLevel, BlockPos pPos, List<ItemStack> drops, BlockPos cropPos) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity != null && blockEntity instanceof GooSoilBE gooSoilBE) {
            IItemHandler handler = gooSoilBE.getAttachedInventory(pLevel);
            if (handler != null) {
                Helpers.teleportDrops(drops, handler);
                if (drops.isEmpty()) //Only spawn particles if we teleported everything - granted this isn't perfect, but way better than exhaustive testing
                    ToggleableTool.teleportParticles(pLevel, cropPos, 5);
            }
        }
    }

    public static void dropDrops(ServerLevel pLevel, List<ItemStack> drops, BlockPos cropPos) {
        for (ItemStack drop : drops) {
            ItemEntity itemEntity = new ItemEntity(pLevel, cropPos.getX(), cropPos.getY(), cropPos.getZ(), drop);
            itemEntity.lifespan = 40;
            pLevel.addFreshEntity(itemEntity);
        }
    }

    public static void autoHarvest(ServerLevel pLevel, BlockPos pPos) {
        BlockPos cropPos = pPos.above();
        BlockState crop = pLevel.getBlockState(cropPos);
        if (crop.getBlock() instanceof CropBlock) {
            List<ItemStack> drops = harvestCrop(pLevel, cropPos, crop);
            if (!drops.isEmpty()) {
                teleportDrops(pLevel, pPos, drops, cropPos);
                dropDrops(pLevel, drops, cropPos);
            }
        } else if (crop.is(Blocks.SUGAR_CANE) || crop.is(Blocks.CACTUS) || crop.is(Blocks.BAMBOO)) {
            BlockPos secondPos = cropPos.above();
            BlockState secondState = pLevel.getBlockState(secondPos);
            if (secondState.is(crop.getBlock())) {
                List<ItemStack> drops = harvestCrop(pLevel, secondPos, secondState);
                if (!drops.isEmpty()) {
                    teleportDrops(pLevel, pPos, drops, secondPos);
                    dropDrops(pLevel, drops, secondPos);
                }
            }
        }
    }
}