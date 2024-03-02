package com.direwolf20.justdirethings.common.items.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.stream.Collectors;

public interface TieredGooItem {
    GooTier gooTier();

    default int getGooTier() {
        return gooTier().getGooTier();
    }

    default void breakBlocks(ServerLevel level, BlockPos pos, LivingEntity pPlayer, ItemStack pStack) {
        /*BlockState state = level.getBlockState(pos); //Todo: Tier 2 and 3
        List<ItemStack> Block.getDrops();
        LootParams.Builder lootparams$builder = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.THIS_ENTITY, pPlayer)
                .withParameter(LootContextParams.TOOL, pStack)
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .withParameter(LootContextParams.TOOL, new ItemStack(Registration.FerricoreHoe.get()));*/

        // If specific block conditions are needed, set them in the LootContext builder.

        level.destroyBlock(pos, true);
    }

    /**
     * Basically a veinminer
     */
    default Set<BlockPos> findLikeBlocks(Level pLevel, BlockState pState, BlockPos pPos, int maxBreak, int radius) {
        Set<BlockPos> foundBlocks = new HashSet<>(); //The matching Blocks
        Queue<BlockPos> blocksToCheck = new LinkedList<>(); //A list of blocks to check around
        Set<BlockPos> checkedBlocks = new HashSet<>(); //A list of blocks we already checked

        foundBlocks.add(pPos); //Obviously the block we broke is included in the return!
        blocksToCheck.add(pPos); //Start scanning around the block we broke

        while (!blocksToCheck.isEmpty()) {
            BlockPos posToCheck = blocksToCheck.poll(); //Get the next blockPos to scan around

            if (!checkedBlocks.add(posToCheck))
                continue; //Don't check blockPos we've checked before

            Set<BlockPos> matchingBlocks = BlockPos.betweenClosedStream(posToCheck.offset(-radius, -radius, -radius), posToCheck.offset(radius, radius, radius))
                    .filter(blockPos -> pLevel.getBlockState(blockPos).is(pState.getBlock()))
                    .map(BlockPos::immutable)
                    .collect(Collectors.toSet());

            for (BlockPos toAdd : matchingBlocks) { //Ensure we don't go beyond maxBreak
                if (foundBlocks.size() < maxBreak) {
                    foundBlocks.add(toAdd); //Add all the blocks we found to our set of found blocks
                    if (!checkedBlocks.contains(toAdd))
                        blocksToCheck.add(toAdd); //Add all the blocks we found to be checked as well
                } else
                    return foundBlocks;
            }
        }
        return foundBlocks;
    }

    /**
     * Looks in a specified direction for similar blocks - used by shovels to clear all like blocks above them
     */
    default Set<BlockPos> findLikeBlocks(Level pLevel, BlockState pState, BlockPos pPos, int maxBreak, Direction direction, int range) {
        Set<BlockPos> foundBlocks = new HashSet<>(); //The matching Blocks
        foundBlocks.add(pPos); //Obviously the block we broke is included in the return!

        for (int i = 1; i < range; i++) {
            BlockPos posToCheck = pPos.relative(direction, i); //The next blockPos to check
            BlockState blockState = pLevel.getBlockState(posToCheck);
            if (blockState.is(pState.getBlock())) {
                foundBlocks.add(posToCheck);
            } else {
                break;
            }
            if (foundBlocks.size() >= maxBreak)
                break;
        }
        return foundBlocks;
    }

    /**
     * Basically a veinminer
     */
    default Set<BlockPos> findTaggedBlocks(Level pLevel, List<TagKey<Block>> tags, BlockPos pPos, int maxBreak, int radius) {
        Set<BlockPos> foundBlocks = new HashSet<>(); //The matching Blocks
        Queue<BlockPos> blocksToCheck = new LinkedList<>(); //A list of blocks to check around
        Set<BlockPos> checkedBlocks = new HashSet<>(); //A list of blocks we already checked

        blocksToCheck.add(pPos); //Start scanning around the block we broke

        while (!blocksToCheck.isEmpty()) {
            BlockPos posToCheck = blocksToCheck.poll(); //Get the next blockPos to scan around

            if (!checkedBlocks.add(posToCheck))
                continue; //Don't check blockPos we've checked before

            Set<BlockPos> matchingBlocks = BlockPos.betweenClosedStream(posToCheck.offset(-radius, -radius, -radius), posToCheck.offset(radius, radius, radius))
                    .filter(blockPos -> tags.stream().anyMatch(pLevel.getBlockState(blockPos)::is))
                    .map(BlockPos::immutable)
                    .collect(Collectors.toSet());

            for (BlockPos toAdd : matchingBlocks) { //Ensure we don't go beyond maxBreak
                if (foundBlocks.size() < maxBreak) {
                    foundBlocks.add(toAdd); //Add all the blocks we found to our set of found blocks
                    if (!checkedBlocks.contains(toAdd))
                        blocksToCheck.add(toAdd); //Add all the blocks we found to be checked as well
                } else
                    return foundBlocks;
            }
        }
        return foundBlocks;
    }

    /**
     * Same as above, but you can pass in extra block tags to break - for example, if you wanna also break all leaves when blockstate is a log
     */

    default Set<BlockPos> findLikeBlocks(Level pLevel, BlockState pState, BlockPos pPos, int maxBreak, int radius, List<TagKey<Block>> extraTags) {
        Set<BlockPos> foundBlocks = new HashSet<>(); //The matching Blocks
        Queue<BlockPos> blocksToCheck = new LinkedList<>(); //A list of blocks to check around
        Queue<BlockPos> secondaryBlocksToCheck = new LinkedList<>(); // Matching states will always iterate first, extraTags will be scanned second!
        Set<BlockPos> checkedBlocks = new HashSet<>(); //A list of blocks we already checked

        foundBlocks.add(pPos); //Obviously the block we broke is included in the return!
        blocksToCheck.add(pPos); //Start scanning around the block we broke

        while (!blocksToCheck.isEmpty() || !secondaryBlocksToCheck.isEmpty()) {
            boolean isPrimaryPhase = !blocksToCheck.isEmpty(); //Primary first!
            BlockPos posToCheck = isPrimaryPhase ? blocksToCheck.poll() : secondaryBlocksToCheck.poll(); //Get the next blockPos to scan around

            if (!checkedBlocks.add(posToCheck))
                continue; //Don't check blockPos we've checked before

            BlockPos.betweenClosedStream(posToCheck.offset(-radius, -radius, -radius), posToCheck.offset(radius, radius, radius))
                    .forEach(blockPos -> {
                        if (foundBlocks.size() >= maxBreak) {
                            return; // Exit if we've reached the maxBreak limit
                        }
                        BlockState foundState = pLevel.getBlockState(blockPos);
                        boolean isPrimaryBlock = foundState.is(pState.getBlock());
                        boolean isSecondaryBlock = extraTags.stream().anyMatch(foundState::is);

                        if (isPrimaryBlock || isSecondaryBlock) {
                            foundBlocks.add(blockPos.immutable());

                            if (!checkedBlocks.contains(blockPos.immutable())) {
                                // Decide which queue to add the found block to
                                if (isPrimaryBlock) {
                                    blocksToCheck.add(blockPos.immutable());
                                } else {
                                    secondaryBlocksToCheck.add(blockPos.immutable());
                                }
                            }
                        }
                    });
        }
        return foundBlocks;
    }
}
