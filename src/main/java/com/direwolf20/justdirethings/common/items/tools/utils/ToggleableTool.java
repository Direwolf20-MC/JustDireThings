package com.direwolf20.justdirethings.common.items.tools.utils;

import com.direwolf20.justdirethings.client.renderactions.ThingFinder;
import com.direwolf20.justdirethings.common.containers.ToolSettingContainer;
import com.direwolf20.justdirethings.datagen.JustDireBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.function.Predicate;

import static com.direwolf20.justdirethings.common.items.tools.utils.Helpers.*;

public interface ToggleableTool {
    EnumSet<ToolAbility> getAbilities();

    default void openSettings(Player player) {
        player.openMenu(new SimpleMenuProvider(
                (windowId, playerInventory, playerEntity) -> new ToolSettingContainer(windowId, playerInventory, player), Component.translatable("")));

    }

    default void registerAbility(ToolAbility ability) {
        getAbilities().add(ability);
    }

    default boolean hasAbility(ToolAbility ability) {
        return getAbilities().contains(ability);
    }

    default boolean canUseAbility(ItemStack itemStack, ToolAbility toolAbility) {
        return hasAbility(toolAbility) && getEnabled(itemStack) && getSetting(itemStack, toolAbility.name);
    }

    //Abilities
    default boolean mineBlocksAbility(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving, ToolAbility toolAbility, Direction direction, Predicate<BlockState> condition) {
        if (!pLevel.isClientSide && pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            Set<BlockPos> breakBlockPositions = new HashSet<>();
            List<ItemStack> drops = new ArrayList<>();
            if (canUseAbility(pStack, toolAbility) && condition.test(pState) && pStack.isCorrectToolForDrops(pState)) {
                breakBlockPositions.addAll(findLikeBlocks(pLevel, pState, pPos, direction, 64, 2)); //Todo: Balance and Config?
            } else {
                breakBlockPositions.add(pPos);
            }

            for (BlockPos breakPos : breakBlockPositions) {
                Helpers.combineDrops(drops, breakBlocks((ServerLevel) pLevel, breakPos, pEntityLiving, pStack));
                pStack.hurtAndBreak(toolAbility.getDurabilityCost(), pEntityLiving, p_40992_ -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
            if (canUseAbility(pStack, ToolAbility.SMELTER)) {
                boolean[] smeltedItemsFlag = new boolean[1]; // Array to hold the smelting flag
                drops = smeltDrops((ServerLevel) pLevel, drops, pStack, pEntityLiving, smeltedItemsFlag);
                if (smeltedItemsFlag[0])
                    smelterParticles((ServerLevel) pLevel, breakBlockPositions);
            }
            if (!drops.isEmpty()) {
                Helpers.dropDrops(drops, (ServerLevel) pLevel, pPos);
            }
            return true;
        }
        return false;
    }

    default void smelterParticles(ServerLevel level, Set<BlockPos> oreBlocksList) {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            for (BlockPos pos : oreBlocksList) {
                double d0 = (double) pos.getX() + random.nextDouble();
                double d1 = (double) pos.getY() + random.nextDouble();
                double d2 = (double) pos.getZ() + random.nextDouble();
                level.sendParticles(ParticleTypes.FLAME, d0, d1, d2, 1, 0.0, 0.0, 0.0, 0);
            }
        }
    }

    /*default boolean treefeller(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            if (canUseAbility(pStack, ToolAbility.TREEFELLER) && pState.getTags().anyMatch(tag -> tag.equals(BlockTags.LOGS)) && pStack.isCorrectToolForDrops(pState)) {
                Set<BlockPos> alsoBreakSet = findLikeBlocks(pLevel, pState, pPos, 64, 2); //Todo: Balance and Config?
                for (BlockPos breakPos : alsoBreakSet) {
                    breakBlocks((ServerLevel) pLevel, breakPos, pEntityLiving, pStack, pPos);
                    pStack.hurtAndBreak(ToolAbility.TREEFELLER.getDurabilityCost(), pEntityLiving, p_40992_ -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                }
                return true;
            }
        }
        return false;
    }

    default boolean oreMiner(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            if (canUseAbility(pStack, ToolAbility.OREMINER) && pState.getTags().anyMatch(tag -> tag.equals(Tags.Blocks.ORES)) && pStack.isCorrectToolForDrops(pState)) {
                Set<BlockPos> alsoBreakSet = findLikeBlocks(pLevel, pState, pPos, 64, 2); //Todo: Balance and Config?
                for (BlockPos breakPos : alsoBreakSet) {
                    breakBlocks((ServerLevel) pLevel, breakPos, pEntityLiving, pStack, pPos);
                    pStack.hurtAndBreak(ToolAbility.OREMINER.getDurabilityCost(), pEntityLiving, p_40992_ -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                }
                return true;
            }
        }
        return false;
    }

    default boolean skySweeper(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            if (canUseAbility(pStack, ToolAbility.SKYSWEEPER) && pState.getBlock() instanceof FallingBlock && pStack.isCorrectToolForDrops(pState)) {
                Set<BlockPos> alsoBreakSet = findLikeBlocks(pLevel, pState, pPos, 24, Direction.UP, 24); //Todo: Balance and Config?
                for (BlockPos breakPos : alsoBreakSet) {
                    breakBlocks((ServerLevel) pLevel, breakPos, pEntityLiving, pStack, pPos);
                    pStack.hurtAndBreak(ToolAbility.SKYSWEEPER.getDurabilityCost(), pEntityLiving, p_40992_ -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                }
                return true;
            }
        }
        return false;
    }*/

    default boolean scanFor(Level level, Player player, InteractionHand hand, ToolAbility toolAbility) {
        if (!player.isShiftKeyDown()) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (canUseAbility(itemStack, toolAbility)) {
                if (level.isClientSide) {
                    if (itemStack.getItem() instanceof TieredGooItem tieredGooItem) {
                        ThingFinder.discover(player, tieredGooItem.getGooTier(), toolAbility);
                    }
                } else { //ServerSide
                    itemStack.hurtAndBreak(toolAbility.getDurabilityCost(), player, p_40992_ -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                }
            }
        }
        return false;
    }

    default boolean leafbreaker(UseOnContext pContext) {
        Level pLevel = pContext.getLevel();
        BlockPos pPos = pContext.getClickedPos();
        BlockState pState = pLevel.getBlockState(pPos);
        LivingEntity pEntityLiving = pContext.getPlayer();
        ItemStack pStack = pContext.getItemInHand();
        if (!pLevel.isClientSide && canUseAbility(pStack, ToolAbility.LEAFBREAKER)) { //TODO MineBlocks Method Above?
            if (pState.getTags().anyMatch(tag -> tag.equals(BlockTags.LEAVES))) {
                Set<BlockPos> alsoBreakSet = findLikeBlocks(pLevel, pState, pPos, null, 64, 2); //Todo: Balance and Config?
                List<ItemStack> drops = new ArrayList<>();
                for (BlockPos breakPos : alsoBreakSet) {
                    Helpers.combineDrops(drops, breakBlocks((ServerLevel) pLevel, breakPos, pEntityLiving, pStack));
                    pLevel.sendBlockUpdated(breakPos, pState, pLevel.getBlockState(breakPos), 3); // I have NO IDEA why this is necessary
                    if (Math.random() < 0.1) //10% chance to damage tool
                        pStack.hurtAndBreak(ToolAbility.LEAFBREAKER.getDurabilityCost(), pEntityLiving, p_40992_ -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                }
                if (!drops.isEmpty()) {
                    Helpers.dropDrops(drops, (ServerLevel) pLevel, pPos);
                }
                return true;
            }
        }
        return false;
    }

    default boolean lawnmower(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!level.isClientSide && canUseAbility(itemStack, ToolAbility.LAWNMOWER)) {
            List<TagKey<Block>> tags = new ArrayList<>();
            tags.add(JustDireBlockTags.LAWNMOWERABLE);
            Set<BlockPos> breakBlocks = findTaggedBlocks(level, tags, player.getOnPos(), 64, 5); //TODO Balance/Config?
            for (BlockPos breakPos : breakBlocks) {
                breakBlocks((ServerLevel) level, breakPos);
                if (Math.random() < 0.1) //10% chance to damage tool
                    itemStack.hurtAndBreak(ToolAbility.LAWNMOWER.getDurabilityCost(), player, p_40992_ -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
            return true;
        }
        return false;
    }

    static ItemStack getToggleableTool(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        if (mainHand.getItem() instanceof ToggleableTool)
            return mainHand;
        ItemStack offHand = player.getOffhandItem();
        if (offHand.getItem() instanceof ToggleableTool)
            return offHand;
        return ItemStack.EMPTY;
    }

    static boolean toggleSetting(ItemStack stack, String setting) {
        CompoundTag tagCompound = stack.getOrCreateTag();
        tagCompound.putBoolean(setting, !getSetting(stack, setting));
        return tagCompound.getBoolean(setting);
    }

    static boolean getSetting(ItemStack stack, String setting) {
        CompoundTag tagCompound = stack.getOrCreateTag();
        return !tagCompound.contains(setting) || tagCompound.getBoolean(setting); //Enabled by default
    }

    static boolean getEnabled(ItemStack stack) {
        CompoundTag tagCompound = stack.getOrCreateTag();
        return getSetting(stack, "enabled");
    }

    static boolean toggleEnabled(ItemStack stack) {
        return toggleSetting(stack, "enabled");
    }

    static void setToolValue(ItemStack stack, int value, String valueName) {
        stack.getOrCreateTag().putInt(valueName, value);
    }

    static int getToolValue(ItemStack stack, String valueName) {
        return stack.getOrCreateTag().getInt(valueName);
    }
}
