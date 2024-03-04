package com.direwolf20.justdirethings.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.IPlantable;

public class GooSoil extends FarmBlock {
    public GooSoil() {
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
        //pEntity.causeFallDamage(pFallDistance, 1.0F, pEntity.damageSources().fall()); // Todo should I do this?
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
        BlockPos cropPos = pPos.above();
        BlockState crop = pLevel.getBlockState(cropPos);
        if (crop.getBlock() instanceof CropBlock cropBlock) {
            if (crop.isRandomlyTicking()) {
                float f = 2;
                if (cropBlock.isValidBonemealTarget(pLevel, cropPos, crop) && pRandom.nextInt((int) (25.0F / f) + 1) == 0) {
                    cropBlock.performBonemeal(pLevel, pRandom, cropPos, crop);
                    pLevel.levelEvent(1505, cropPos, 0); //Does Bonemeal Particles
                }
            }
            /*if (cropBlock.isMaxAge(crop)) { // Todo: Tier 2
                BlockState state = pLevel.getBlockState(cropPos);
                LootParams.Builder lootparams$builder = new LootParams.Builder(pLevel)
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(cropPos))
                        .withParameter(LootContextParams.BLOCK_STATE, crop)
                        .withParameter(LootContextParams.TOOL, new ItemStack(Registration.FerricoreHoe.get()));

                // If specific block conditions are needed, set them in the LootContext builder.

                BlockState placeState = Blocks.AIR.defaultBlockState();
                List<ItemStack> drops = state.getDrops(lootparams$builder);
                for (ItemStack drop : drops) {
                    if (drop.getItem() instanceof BlockItem blockItem) {
                        placeState = blockItem.getBlock().defaultBlockState();
                        drop.shrink(1);
                    }
                }
                if (placeState == Blocks.AIR.defaultBlockState()) {
                    pLevel.destroyBlock(cropPos, true);
                } else {
                    pLevel.destroyBlock(cropPos, false);
                    for (ItemStack drop : drops) {
                        ItemEntity itemEntity = new ItemEntity(pLevel, cropPos.getX(), cropPos.getY(), cropPos.getZ(), drop);
                        itemEntity.setPickUpDelay(40);
                        pLevel.addFreshEntity(itemEntity);
                    }
                    pLevel.setBlockAndUpdate(cropPos, placeState);
                }
            }*/
        }
    }
}
