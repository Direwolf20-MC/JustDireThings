package com.direwolf20.justdirethings.common.items.interfaces;

import com.direwolf20.justdirethings.client.particles.itemparticle.ItemFlowParticleData;
import com.direwolf20.justdirethings.datagen.JustDireItemTags;
import com.direwolf20.justdirethings.datagen.recipes.AbilityConvertRecipe;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
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
    public static final Predicate<BlockState> oreCondition = s -> s.is(Tags.Blocks.ORES);
    public static final Predicate<BlockState> fallingBlockCondition = s -> s.getBlock() instanceof FallingBlock;
    public static final Predicate<BlockState> logCondition = s -> s.is(BlockTags.LOGS);

    public static void breakBlocks(ServerLevel level, BlockPos pos) {
        level.destroyBlock(pos, true);
    }

    public static List<ItemStack> breakBlocks(Level level, BlockPos pos, LivingEntity pPlayer, ItemStack pStack, boolean damageTool, boolean instaBreak) {
        List<ItemStack> drops = new ArrayList<>();
        if (pPlayer instanceof Player player) {
            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, level.getBlockState(pos), player);
            if (NeoForge.EVENT_BUS.post(event).isCanceled()) return drops;

            BlockState state = level.getBlockState(pos);
            BlockEntity blockEntity = level.getBlockEntity(pos);
            //This is how vanilla does it?
            float destroySpeed = state.getDestroySpeed(level, pos);
            boolean removed = state.onDestroyedByPlayer(level, pos, player, true, level.getFluidState(pos));
            if (removed) {
                if (level instanceof ServerLevel serverLevel)
                    drops.addAll(Block.getDrops(state, serverLevel, pos, blockEntity, pPlayer, pStack));
                state.getBlock().destroy(level, pos, state);
                player.awardStat(Stats.BLOCK_MINED.get(state.getBlock()));
                player.causeFoodExhaustion(0.005F);
                if (damageTool && destroySpeed != 0.0F) {
                    damageTool(pStack, pPlayer);
                    if (instaBreak) {
                        damageTool(pStack, pPlayer, getInstantRFCost(destroySpeed));
                    }
                }

                return drops;
            }
        }
        return new ArrayList<>();
    }

    public static void damageTool(ItemStack stack, LivingEntity player) {
        if (stack.getItem() instanceof PoweredTool poweredTool) {
            stack.hurtAndBreak(poweredTool.getBlockBreakFECost(), player, pOnBroken -> pOnBroken.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        } else {
            stack.hurtAndBreak(1, player, pOnBroken -> pOnBroken.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
    }

    public static void damageTool(ItemStack stack, LivingEntity player, int amount) {
        if (stack.getItem() instanceof PoweredTool poweredTool) {
            stack.hurtAndBreak(amount, player, pOnBroken -> pOnBroken.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        } else {
            stack.hurtAndBreak(amount, player, pOnBroken -> pOnBroken.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
    }

    public static void damageTool(ItemStack stack, LivingEntity player, Ability ability) {
        if (stack.getItem() instanceof PoweredTool) {
            stack.hurtAndBreak(ability.getFeCost(), player, pOnBroken -> pOnBroken.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        } else {
            stack.hurtAndBreak(ability.getDurabilityCost(), player, pOnBroken -> pOnBroken.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
    }

    public static void damageTool(ItemStack stack, LivingEntity player, Ability ability, int multiplier) {
        if (stack.getItem() instanceof PoweredTool) {
            stack.hurtAndBreak(ability.getFeCost() * multiplier, player, pOnBroken -> pOnBroken.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        } else {
            stack.hurtAndBreak(ability.getDurabilityCost() * multiplier, player, pOnBroken -> pOnBroken.broadcastBreakEvent(EquipmentSlot.MAINHAND));
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
        if (stack.getItem() instanceof PoweredTool) {
            IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (energyStorage == null) return -1; //Shouldn't Happen!
            return energyStorage.getEnergyStored() - cost;
        } else {
            return stack.getMaxDamage() - stack.getDamageValue() - cost;
        }
    }

    public static int testUseTool(ItemStack stack, Ability ability) {
        if (stack.getItem() instanceof PoweredTool) {
            IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (energyStorage == null) return -1; //Shouldn't Happen!
            return energyStorage.getEnergyStored() - ability.getFeCost();
        } else {
            return stack.getMaxDamage() - stack.getDamageValue() - ability.getDurabilityCost();
        }
    }

    public static int testUseTool(ItemStack stack, Ability ability, int multiplier) {
        if (stack.getItem() instanceof PoweredTool) {
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
                    .filter(drop -> ItemStack.isSameItemSameTags(drop, newDrop))
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
        Optional<RecipeHolder<SmeltingRecipe>> smeltingRecipe = recipeManager.getRecipeFor(RecipeType.SMELTING, new SimpleContainer(itemStack), level);
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
            Optional<RecipeHolder<SmeltingRecipe>> smeltingRecipe = recipeManager.getRecipeFor(RecipeType.SMELTING, new SimpleContainer(drop), level);

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

    public static Optional<RecipeHolder<AbilityConvertRecipe>> getAbilityConvertRecipe(Ability ability, ItemStack inputItem, ServerLevel level) {
        RecipeManager recipeManager = level.getRecipeManager();
        List<RecipeHolder<AbilityConvertRecipe>> abilityConvertRecipes = recipeManager.getAllRecipesFor(Registration.ABILITY_CONVERT_RECIPE_TYPE.get());
        
        if (!abilityConvertRecipes.isEmpty()) {
            for (RecipeHolder<AbilityConvertRecipe> recipe : abilityConvertRecipes) {
                if (recipe.value().getAbilityRequirement() == ability && recipe.value().getInput().getItem() == inputItem.getItem()) {
                    return Optional.of(recipe);
                }
            }
        }
        return Optional.empty();
    }
    
    public static void smokeDrop(ServerLevel level, ItemEntity drop, ItemStack tool, LivingEntity entityLiving, boolean[] didISmoke) {
        RegistryAccess registryAccess = level.registryAccess();
        RecipeManager recipeManager = level.getRecipeManager();
        didISmoke[0] = false;
        
        // Check if item is allowed first to improve performance
        if (!drop.getItem().is(JustDireItemTags.AUTO_SMOKE_DENY)) {
        	ItemStack smokedResults = ItemStack.EMPTY;
        	
        	// Check for ability recipes first
        	Optional<RecipeHolder<AbilityConvertRecipe>> abilityConvertRecipe = getAbilityConvertRecipe(Ability.SMOKER, drop.getItem(), level);

        	if (abilityConvertRecipe.isPresent()) {
        		smokedResults = abilityConvertRecipe.get().value().getOutput();
        	} else {
        		// Fallback to checking smoker recipes
        		Optional<RecipeHolder<SmokingRecipe>> smokingRecipe = recipeManager.getRecipeFor(RecipeType.SMOKING, new SimpleContainer(drop.getItem()), level);

        		if (smokingRecipe.isPresent()) {        			
        			smokedResults = smokingRecipe.get().value().getResultItem(registryAccess);
        		}
        	}

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
        CompoundTag tag = stack.getOrCreateTag();
        if (isInLava(entity)) {
            tag.putInt("floatingTicks", 0);
            tag.put("lavaBlockLoc", NbtUtils.writeBlockPos(entity.blockPosition()));
            entity.setPickUpDelay(85);
            entity.setOnGround(true);
        }
        if (!validateLava(stack, entity)) {
            tag.remove("floatingTicks");
            tag.remove("lavaBlockLoc");
            entity.setOnGround(false);
        }
        if (tag.contains("floatingTicks")) {
            if (entity.position().y - getLavaPos(stack).getY() < 3)
                entity.setDeltaMovement(new Vec3(0, 0.05, 0)); // Slower floating effect
            else
                entity.setDeltaMovement(new Vec3(0, 0.005, 0)); // Slower floating effect
            entity.move(MoverType.SELF, entity.getDeltaMovement());
            tag.putInt("floatingTicks", tag.getInt("floatingTicks") + 1);
            entity.setOnGround(true);
            if (tag.getInt("floatingTicks") >= 80) {
                turnLavaIntoObsidian(stack, entity);
                repairItem(stack);
                tag.remove("floatingTicks"); // Reset the counter
                tag.remove("lavaBlockLoc");
                entity.setOnGround(false);
            }
            if (!entity.level().isClientSide && tag.getInt("floatingTicks") < 40)
                doParticles(stack, entity);
        }
        return false; // Return false to allow normal item update processing
    }

    private static BlockPos getLavaPos(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return NbtUtils.readBlockPos(tag.getCompound("lavaBlockLoc"));
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
        return entity.level().getBlockState(entity.blockPosition()).is(Blocks.LAVA);
    }

    private static void repairItem(ItemStack stack) {
        stack.setDamageValue(0);
    }

    private static boolean validateLava(ItemStack stack, ItemEntity entity) {
        return entity.level().getBlockState(getLavaPos(stack)).is(Blocks.LAVA);
    }

    private static void turnLavaIntoObsidian(ItemStack stack, ItemEntity entity) {
        BlockPos lavaPos = getLavaPos(stack);
        entity.level().setBlock(lavaPos, Blocks.OBSIDIAN.defaultBlockState(), 3); // Remove the lava block
        entity.level().playSound(null, lavaPos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1F, 1.0F);
    }
}
