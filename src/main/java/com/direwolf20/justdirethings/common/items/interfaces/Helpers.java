package com.direwolf20.justdirethings.common.items.interfaces;

import com.direwolf20.justdirethings.client.particles.itemparticle.ItemFlowParticleData;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.datagen.JustDireItemTags;
import com.direwolf20.justdirethings.setup.Registration;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool.getInstantRFCost;

public class Helpers {
    public static final Predicate<BlockState> oreCondition = s -> s.is(Tags.Blocks.ORES) || s.is(Tags.Blocks.CLUSTERS);
    public static final Predicate<BlockState> fallingBlockCondition = s -> s.getBlock() instanceof FallingBlock;
    public static final Predicate<BlockState> logCondition = s -> s.is(BlockTags.LOGS);

    public static void breakBlocksNew(Level level, BlockPos pos, LivingEntity pPlayer, ItemStack pStack, boolean damageTool, boolean instaBreak) {
        if (pPlayer instanceof ServerPlayer player && level instanceof ServerLevel serverLevel) {
            BlockState state = level.getBlockState(pos);
            GameType type = player.getAbilities().instabuild ? GameType.CREATIVE : GameType.SURVIVAL;
            BlockEvent.BreakEvent exp = CommonHooks.fireBlockBreak(serverLevel, type, player, pos, state);
            if (exp.isCanceled()) {
                return;
            }
            BlockEntity blockEntity = level.getBlockEntity(pos);
            Block block = state.getBlock();
            //This is how vanilla does it? With assistance from Shadows!
            if (block instanceof GameMasterBlock && !player.canUseGameMasterBlocks()) {
                level.sendBlockUpdated(pos, state, state, 3);
                return;
            }
            if (player.blockActionRestricted(level, pos, type)) {
                return;
            }
            float destroySpeed = state.getDestroySpeed(level, pos);
            BlockState removedBlockState = state.getBlock().playerWillDestroy(level, pos, state, player);
            if (player.getAbilities().instabuild) {
                removeBlock(serverLevel, player, pos, removedBlockState, false);
                return;
            }
            ItemStack toolCopy = pStack.copy();
            boolean canHarvest = removedBlockState.canHarvestBlock(level, pos, player);
            //pStack.mineBlock(level, removedBlockState, pos, player); Removed because I handle this below, and its only ever called from MY tools
            boolean removed = removeBlock(serverLevel, player, pos, removedBlockState, canHarvest);
            if (canHarvest && removed) {
                block.playerDestroy(level, player, pos, removedBlockState, blockEntity, pStack);
            }
            if (damageTool && destroySpeed != 0.0F) {
                damageTool(pStack, pPlayer);
                if (instaBreak) {
                    damageTool(pStack, pPlayer, getInstantRFCost(destroySpeed, level, pStack));
                }
            }
            /*if (pStack.isEmpty() && !toolCopy.isEmpty()) { //This is done in damageTool(Above) - so this was actually running twice...
                EventHooks.onPlayerDestroyItem(player, toolCopy, InteractionHand.MAIN_HAND);
            }*/
        }
    }

    public static boolean removeBlock(ServerLevel level, ServerPlayer player, BlockPos pos, BlockState state, boolean canHarvest) {
        boolean removed = state.onDestroyedByPlayer(level, pos, player, canHarvest, level.getFluidState(pos));
        if (removed) {
            state.getBlock().destroy(level, pos, state);
        }
        return removed;
    }

    /*public static List<ItemStack> breakBlocks(Level level, BlockPos pos, LivingEntity pPlayer, ItemStack pStack, boolean damageTool, boolean instaBreak) {
        List<ItemStack> drops = new ArrayList<>();
        if (pPlayer instanceof Player player) {
            BlockState state = level.getBlockState(pos);
            if (level instanceof ServerLevel serverLevel) {
                BlockEvent.BreakEvent breakEvent = new BlockEvent.BreakEvent(serverLevel, pos, state, player);
                if (NeoForge.EVENT_BUS.post(breakEvent).isCanceled())
                    return drops;
            }
            BlockEntity blockEntity = level.getBlockEntity(pos);
            //This is how vanilla does it?
            float destroySpeed = state.getDestroySpeed(level, pos);
            BlockState removedBlockState = state.getBlock().playerWillDestroy(level, pos, state, player);
            boolean removed = state.onDestroyedByPlayer(level, pos, player, true, level.getFluidState(pos));
            if (removed) {
                if (level instanceof ServerLevel serverLevel) {
                    // Capture block drops into a list of ItemEntities
                    List<ItemEntity> newDrops = Block.getDrops(state, serverLevel, pos, blockEntity, pPlayer, pStack)
                            .stream()
                            .map(stack -> new ItemEntity(serverLevel, pos.getX(), pos.getY(), pos.getZ(), stack))
                            .collect(Collectors.toList());

                    BlockDropsEvent dropsEvent = new BlockDropsEvent(serverLevel, pos, state, blockEntity, newDrops, player, pStack);
                    NeoForge.EVENT_BUS.post(dropsEvent);
                    if (!dropsEvent.isCanceled()) {
                        // Convert back to ItemStacks if needed (for further processing)
                        for (ItemEntity drop : newDrops) {
                            drops.add(drop.getItem());
                        }
                        // Always pass false for the dropXP (last) param to spawnAfterBreak since we handle XP.
                        state.spawnAfterBreak(serverLevel, pos, pStack, false);
                    }
                }
                state.getBlock().destroy(level, pos, removedBlockState);
                player.awardStat(Stats.BLOCK_MINED.get(state.getBlock()));
                player.causeFoodExhaustion(0.005F);
                if (damageTool && destroySpeed != 0.0F) {
                    damageTool(pStack, pPlayer);
                    if (instaBreak) {
                        damageTool(pStack, pPlayer, getInstantRFCost(destroySpeed, level, pStack));
                    }
                }

                return drops;
            }
        }
        return new ArrayList<>();
    }*/

    public static void damageTool(ItemStack stack, LivingEntity player) {
        if (stack.getItem() instanceof PoweredTool poweredTool) {
            stack.hurtAndBreak(poweredTool.getBlockBreakFECost(), player, LivingEntity.getSlotForHand(InteractionHand.MAIN_HAND));
        } else {
            ItemStack cloneStack = stack.copy();
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(InteractionHand.MAIN_HAND));
            if (stack.isEmpty() && !cloneStack.isEmpty() && player instanceof Player player1)
                net.neoforged.neoforge.event.EventHooks.onPlayerDestroyItem(player1, cloneStack, InteractionHand.MAIN_HAND);
        }
    }

    public static void damageTool(ItemStack stack, LivingEntity player, int amount) {
        if (stack.getItem() instanceof PoweredItem poweredTool) {
            stack.hurtAndBreak(amount, player, LivingEntity.getSlotForHand(InteractionHand.MAIN_HAND));
        } else {
            ItemStack cloneStack = stack.copy();
            stack.hurtAndBreak(amount, player, LivingEntity.getSlotForHand(InteractionHand.MAIN_HAND));
            if (stack.isEmpty() && !cloneStack.isEmpty() && player instanceof Player player1)
                net.neoforged.neoforge.event.EventHooks.onPlayerDestroyItem(player1, cloneStack, InteractionHand.MAIN_HAND);
        }
    }

    public static void damageTool(ItemStack stack, LivingEntity player, Ability ability) {
        if (stack.getItem() instanceof PoweredItem) {
            stack.hurtAndBreak(ability.getFeCost(), player, LivingEntity.getSlotForHand(InteractionHand.MAIN_HAND));
        } else {
            ItemStack cloneStack = stack.copy();
            stack.hurtAndBreak(ability.getDurabilityCost(), player, LivingEntity.getSlotForHand(InteractionHand.MAIN_HAND));
            if (stack.isEmpty() && !cloneStack.isEmpty() && player instanceof Player player1)
                net.neoforged.neoforge.event.EventHooks.onPlayerDestroyItem(player1, cloneStack, InteractionHand.MAIN_HAND);
        }
    }

    public static void damageTool(ItemStack stack, LivingEntity player, Ability ability, int multiplier) {
        if (stack.getItem() instanceof PoweredItem) {
            stack.hurtAndBreak(ability.getFeCost() * multiplier, player, LivingEntity.getSlotForHand(InteractionHand.MAIN_HAND));
        } else {
            ItemStack cloneStack = stack.copy();
            stack.hurtAndBreak(ability.getDurabilityCost() * multiplier, player, LivingEntity.getSlotForHand(InteractionHand.MAIN_HAND));
            if (stack.isEmpty() && !cloneStack.isEmpty() && player instanceof Player player1)
                net.neoforged.neoforge.event.EventHooks.onPlayerDestroyItem(player1, cloneStack, InteractionHand.MAIN_HAND);
        }
    }

    public static int testUseTool(ItemStack stack) {
        if (stack.getItem() instanceof PoweredTool poweredTool) {
            IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (energyStorage == null) return -1; //Shouldn't Happen!
            return energyStorage.getEnergyStored() - poweredTool.getBlockBreakFECost();
        } else {
            return stack.getMaxDamage() - stack.getDamageValue() - 1;
        }
    }

    public static int testUseTool(ItemStack stack, int cost) {
        if (stack.getItem() instanceof PoweredItem) {
            IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (energyStorage == null) return -1; //Shouldn't Happen!
            return energyStorage.getEnergyStored() - cost;
        } else {
            return stack.getMaxDamage() - stack.getDamageValue() - cost;
        }
    }

    public static int testUseTool(ItemStack stack, Ability ability) {
        if (stack.getItem() instanceof PoweredItem) {
            IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (energyStorage == null) return -1; //Shouldn't Happen!
            return energyStorage.getEnergyStored() - ability.getFeCost();
        } else {
            return stack.getMaxDamage() - stack.getDamageValue() - ability.getDurabilityCost();
        }
    }

    public static int testUseTool(ItemStack stack, Ability ability, int multiplier) {
        if (stack.getItem() instanceof PoweredItem) {
            IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (energyStorage == null) return -1; //Shouldn't Happen!
            return energyStorage.getEnergyStored() - (ability.getFeCost() * multiplier);
        } else {
            return stack.getMaxDamage() - stack.getDamageValue() - (ability.getDurabilityCost() * multiplier);
        }
    }

    public static void combineDrops(List<ItemStack> drops, List<ItemStack> newDrops) {
        for (ItemStack newDrop : newDrops) {
            // Attempt to find a matching ItemStack in 'drops' that can be merged with 'newDrop'
            Optional<ItemStack> match = drops.stream()
                    .filter(drop -> ItemStack.isSameItemSameComponents(drop, newDrop))
                    .findFirst();

            if (match.isPresent()) {
                // Found a match, try to add the counts together
                ItemStack existingDrop = match.get();
                // Calculate how much of the 'newDrop' stack can actually be moved over
                int transferableAmount = Math.min(newDrop.getCount(), existingDrop.getMaxStackSize() - existingDrop.getCount());

                if (transferableAmount > 0) {
                    // Increase the count of the existing stack
                    existingDrop.grow(transferableAmount);
                    // Decrease the count of the new stack accordingly
                    newDrop.shrink(transferableAmount);
                }

                // If after transferring, the newDrop still has items left, add it as a new entry.
                if (!newDrop.isEmpty()) {
                    drops.add(newDrop);
                }
            } else {
                // No existing ItemStack could be found or modified, so add 'newDrop' directly
                drops.add(newDrop);
            }
        }
    }

    public static ItemStack getSmeltedItem(Level level, ItemStack itemStack) {
        RegistryAccess registryAccess = level.registryAccess();
        RecipeManager recipeManager = level.getRecipeManager();
        ItemStack returnStack = ItemStack.EMPTY;
        Optional<RecipeHolder<SmeltingRecipe>> smeltingRecipe = recipeManager.getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(itemStack), level);
        if (smeltingRecipe.isPresent() && !itemStack.is(JustDireItemTags.AUTO_SMELT_DENY))
            returnStack = smeltingRecipe.get().value().getResultItem(registryAccess);
        if (returnStack.isEmpty()) return itemStack;
        return returnStack;
    }

    public static List<ItemStack> smeltDrops(ServerLevel level, List<ItemStack> drops, ItemStack tool, LivingEntity entityLiving, boolean[] didISmelt) {
        List<ItemStack> returnList = new ArrayList<>();
        RegistryAccess registryAccess = level.registryAccess();
        RecipeManager recipeManager = level.getRecipeManager();
        didISmelt[0] = false;
        for (ItemStack drop : drops) {
            // Check if there's a smelting recipe for the drop
            Optional<RecipeHolder<SmeltingRecipe>> smeltingRecipe = recipeManager.getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(drop), level);

            if (smeltingRecipe.isPresent() && !drop.is(JustDireItemTags.AUTO_SMELT_DENY)) {
                // Get the result of the smelting recipe
                ItemStack smeltedResult = smeltingRecipe.get().value().getResultItem(registryAccess);

                if (!smeltedResult.isEmpty() && (testUseTool(tool, Ability.SMELTER, drop.getCount()) >= 0)) {
                    // If the smelting result is valid, prepare to replace the original drop with the smelted result
                    ItemStack resultStack = smeltedResult.copy();
                    resultStack.setCount(drop.getCount()); // Assume all items in the stack are smelted
                    if (!tool.isEmpty())
                        damageTool(tool, entityLiving, Ability.SMELTER, drop.getCount());
                    returnList.add(resultStack);
                    didISmelt[0] = true;
                } else {
                    returnList.add(drop);
                }
            } else {
                returnList.add(drop);
            }
        }
        return returnList;
    }

    public static void smokeDrop(ServerLevel level, ItemEntity drop, ItemStack tool, LivingEntity entityLiving, boolean[] didISmoke) {
        RegistryAccess registryAccess = level.registryAccess();
        RecipeManager recipeManager = level.getRecipeManager();
        didISmoke[0] = false;

        // Check if there's a smoking recipe for the drop
        Optional<RecipeHolder<SmokingRecipe>> smokingRecipe = recipeManager.getRecipeFor(RecipeType.SMOKING, new SingleRecipeInput(drop.getItem()), level);

        if (smokingRecipe.isPresent() && !drop.getItem().is(JustDireItemTags.AUTO_SMOKE_DENY)) {
            // Get the result of the smoking recipe
            ItemStack smokedResults = smokingRecipe.get().value().getResultItem(registryAccess);

            if (!smokedResults.isEmpty() && (testUseTool(tool, Ability.SMOKER, drop.getItem().getCount()) >= 0)) {
                didISmoke[0] = true;
                smokedResults.setCount(drop.getItem().getCount()); // Assume all items in the stack are smoked
                // If the smoking result is valid, replace the original drop with the smoked result
                drop.setItem(smokedResults.copy());
                if (!tool.isEmpty())
                    damageTool(tool, entityLiving, Ability.SMOKER, drop.getItem().getCount());
            }
        }
    }

    public static void dropDrops(List<ItemStack> drops, ServerLevel level, BlockPos dropAtPos) {
        for (ItemStack drop : drops) {
            ItemEntity itemEntity = new ItemEntity(level, dropAtPos.getX() + 0.5f, dropAtPos.getY() + 0.5f, dropAtPos.getZ() + 0.5f, drop);
            level.addFreshEntity(itemEntity);
        }
    }

    public static ItemStack teleportDrop(ItemStack itemStack, IItemHandler handler) {
        ItemStack leftover = ItemHandlerHelper.insertItemStacked(handler, itemStack, false);
        return leftover;
    }

    public static ItemStack teleportDrop(ItemStack itemStack, IItemHandler handler, ItemStack tool, Player player) {
        if (testUseTool(tool, Ability.DROPTELEPORT) < 0)
            return itemStack;
        ItemStack leftover = ItemHandlerHelper.insertItemStacked(handler, itemStack, false);
        if (leftover.isEmpty())
            damageTool(tool, player, Ability.DROPTELEPORT);
        return leftover;
    }

    public static void teleportDrops(List<ItemStack> drops, IItemHandler handler) {
        List<ItemStack> leftovers = new ArrayList<>();
        for (ItemStack drop : drops) {
            ItemStack leftover = teleportDrop(drop, handler);
            if (!leftover.isEmpty()) {
                leftovers.add(leftover);
            }
        }
        // Clear the original drops list and add all leftovers to it
        drops.clear();
        drops.addAll(leftovers);
    }

    public static void teleportDrops(List<ItemStack> drops, IItemHandler handler, ItemStack tool, Player player) {
        List<ItemStack> leftovers = new ArrayList<>();
        for (ItemStack drop : drops) {
            ItemStack leftover = teleportDrop(drop, handler, tool, player);
            if (!leftover.isEmpty()) {
                leftovers.add(leftover);
            }
        }
        // Clear the original drops list and add all leftovers to it
        drops.clear();
        drops.addAll(leftovers);
    }

    public static Set<BlockPos> findLikeBlocks(Level pLevel, BlockState pState, BlockPos pPos, Direction direction, int maxBreak, int range) {
        if (direction == null)
            return findBlocks(pLevel, pState, pPos, maxBreak, range);
        else
            return findBlocks(pLevel, pState, pPos, maxBreak, direction, maxBreak);
    }

    /**
     * Basically a veinminer
     */
    private static Set<BlockPos> findBlocks(Level pLevel, BlockState pState, BlockPos pPos, int maxBreak, int radius) {
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
    private static Set<BlockPos> findBlocks(Level pLevel, BlockState pState, BlockPos pPos, int maxBreak, Direction direction, int range) {
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
    public static Set<BlockPos> findTaggedBlocks(Level pLevel, List<TagKey<Block>> tags, BlockPos pPos, int maxBreak, int radius) {
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

    public static Set<BlockPos> findLikeBlocks(Level pLevel, BlockState pState, BlockPos pPos, int maxBreak, int radius, List<TagKey<Block>> extraTags) {
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


    public static boolean doLavaRepair(ItemStack stack, ItemEntity entity) {
        if (isInLava(entity)) {
            stack.set(JustDireDataComponents.FLOATINGTICKS, 0);
            stack.set(JustDireDataComponents.LAVAREPAIR_LAVAPOS, entity.blockPosition());
            entity.setPickUpDelay(85);
            entity.setOnGround(true);
        }
        if (!validateLava(stack, entity)) {
            stack.remove(JustDireDataComponents.FLOATINGTICKS);
            stack.remove(JustDireDataComponents.LAVAREPAIR_LAVAPOS);
            entity.setOnGround(false);
        }
        if (stack.has(JustDireDataComponents.FLOATINGTICKS)) {
            if (entity.position().y - getLavaPos(stack).getY() < 3)
                entity.setDeltaMovement(new Vec3(0, 0.05, 0)); // Slower floating effect
            else
                entity.setDeltaMovement(new Vec3(0, 0.005, 0)); // Slower floating effect
            entity.move(MoverType.SELF, entity.getDeltaMovement());
            stack.update(JustDireDataComponents.FLOATINGTICKS, 0, v -> v + 1);
            entity.setOnGround(true);
            if (stack.getOrDefault(JustDireDataComponents.FLOATINGTICKS, 0) >= 80) {
                turnLavaIntoObsidian(stack, entity);
                repairItem(stack);
                stack.remove(JustDireDataComponents.FLOATINGTICKS);
                stack.remove(JustDireDataComponents.LAVAREPAIR_LAVAPOS);
                entity.setOnGround(false);
            }
            if (!entity.level().isClientSide && stack.getOrDefault(JustDireDataComponents.FLOATINGTICKS, 0) < 40)
                doParticles(stack, entity);
        }
        return false; // Return false to allow normal item update processing
    }

    private static BlockPos getLavaPos(ItemStack stack) {
        return stack.getOrDefault(JustDireDataComponents.LAVAREPAIR_LAVAPOS, BlockPos.ZERO);
    }

    private static void doParticles(ItemStack itemStack, ItemEntity entity) {
        Random random = new Random();
        BlockPos pos = getLavaPos(itemStack);
        ItemFlowParticleData data = new ItemFlowParticleData(new ItemStack(Registration.GooBlock_Tier2.get()), entity.getX(), entity.getY() + 0.75, entity.getZ(), 3);
        for (int i = 0; i < 5; i++) {
            double d0 = (double) pos.getX() + random.nextDouble();
            double d1 = (double) pos.getY() + 0.95;
            double d2 = (double) pos.getZ() + random.nextDouble();
            ((ServerLevel) entity.level()).sendParticles(data, d0, d1, d2, 1, 0, 0, 0, 0);
        }
    }

    private static boolean isInLava(ItemEntity entity) {
        BlockState blockState = entity.level().getBlockState(entity.blockPosition());
        return blockState.is(Blocks.LAVA) && blockState.getValue(BlockStateProperties.LEVEL) == 0;
    }

    private static void repairItem(ItemStack stack) {
        stack.setDamageValue(0);
    }

    private static boolean validateLava(ItemStack stack, ItemEntity entity) {
        BlockState blockState = entity.level().getBlockState(getLavaPos(stack));
        return blockState.is(Blocks.LAVA) && blockState.getValue(BlockStateProperties.LEVEL) == 0;
    }

    private static void turnLavaIntoObsidian(ItemStack stack, ItemEntity entity) {
        BlockPos lavaPos = getLavaPos(stack);
        entity.level().setBlock(lavaPos, Blocks.OBSIDIAN.defaultBlockState(), 3); // Remove the lava block
        entity.level().playSound(null, lavaPos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1F, 1.0F);
    }

    public static Multimap<Attribute, AttributeModifier> addAttributeToModifiers(Multimap<Attribute, AttributeModifier> originalModifiers, Attribute attributeToAdd, AttributeModifier attributeModifier) {
        Multimap<Attribute, AttributeModifier> modifiers = ArrayListMultimap.create(originalModifiers);
        modifiers.put(attributeToAdd, attributeModifier);
        return modifiers;
    }
}
