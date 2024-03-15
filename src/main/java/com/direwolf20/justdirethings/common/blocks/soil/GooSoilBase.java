package com.direwolf20.justdirethings.common.blocks.soil;

import com.direwolf20.justdirethings.common.blockentities.GooSoilBE;
import com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool;
import com.direwolf20.justdirethings.util.MiscHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.IPlantable;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;

import static com.direwolf20.justdirethings.common.items.tools.utils.Helpers.teleportDrops;

public class GooSoilBase extends FarmBlock {
    public GooSoilBase() {
        super(Properties.of()
                .sound(SoundType.GRAVEL)
                .strength(2.0f)
                .randomTicks()
        );
    }

    @Override
    public boolean canSustainPlant(BlockState blockState, BlockGetter level, BlockPos pos, Direction facing, IPlantable plantable) {
        return true;
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

    /**
     * @param odds The chance of bonemeal occuring, 1 in odds chance - lower == more likely
     */
    public static void bonemealMe(ServerLevel pLevel, BlockPos pPos, RandomSource pRandom, int odds) {
        BlockPos cropPos = pPos.above();
        BlockState crop = pLevel.getBlockState(cropPos);
        if (crop.getBlock() instanceof CropBlock cropBlock) {
            if (crop.isRandomlyTicking()) {
                if (cropBlock.isValidBonemealTarget(pLevel, cropPos, crop) && pRandom.nextInt(odds) == 0) {
                    cropBlock.performBonemeal(pLevel, pRandom, cropPos, crop);
                    pLevel.levelEvent(1505, cropPos, 0); //Does Bonemeal Particles
                }
            }
        }
    }

    public static void autoHarvest(ServerLevel pLevel, BlockPos pPos) {
        BlockPos cropPos = pPos.above();
        BlockState crop = pLevel.getBlockState(cropPos);
        if (crop.getBlock() instanceof CropBlock cropBlock) {
            if (cropBlock.isMaxAge(crop)) {
                BlockState placeState = Blocks.AIR.defaultBlockState();
                List<ItemStack> drops = Block.getDrops(crop, pLevel, cropPos, null);
                for (ItemStack drop : drops) {
                    if (drop.getItem() instanceof BlockItem blockItem) {
                        placeState = blockItem.getBlock().defaultBlockState();
                        drop.shrink(1);
                    }
                }
                //if (placeState == Blocks.AIR.defaultBlockState()) {
                //    pLevel.destroyBlock(cropPos, true);
                //} else {
                pLevel.destroyBlock(cropPos, false);
                BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
                if (blockEntity != null && blockEntity instanceof GooSoilBE gooSoilBE) {
                    GlobalPos globalPos = gooSoilBE.getBoundInventory();
                    Direction direction = gooSoilBE.getInventorySide();
                    if (globalPos != null) {
                        IItemHandler handler = MiscHelpers.getAttachedInventory(pLevel.getServer().getLevel(globalPos.dimension()), globalPos.pos(), direction);
                        if (handler != null) {
                            teleportDrops(drops, handler);
                            if (drops.isEmpty()) //Only spawn particles if we teleported everything - granted this isn't perfect, but way better than exhaustive testing
                                ToggleableTool.teleportParticles((ServerLevel) pLevel, cropPos, 5);
                        }
                    }
                }
                for (ItemStack drop : drops) {
                    ItemEntity itemEntity = new ItemEntity(pLevel, cropPos.getX(), cropPos.getY(), cropPos.getZ(), drop);
                    itemEntity.lifespan = 40;
                    pLevel.addFreshEntity(itemEntity);
                }
                pLevel.setBlockAndUpdate(cropPos, placeState);
            }
        }
    }
}