package com.direwolf20.justdirethings.common.items.tools.basetools;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import javax.annotation.Nullable;
import java.util.Optional;

public class BasePaxel extends BasePickaxe {
    public BasePaxel(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (playerHasBlockingItemUseIntent(context)) {
            return InteractionResult.PASS;
        }

        BlockState oldState = level.getBlockState(pos);

        Optional<BlockState> axeResult = evaluateAxeState(level, pos, player, oldState, context);
        if (axeResult.isPresent()) {
            ItemStack itemInHand = context.getItemInHand();
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, itemInHand);
            }
            level.setBlock(pos, axeResult.get(), 11);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, axeResult.get()));
            if (player != null) {
                itemInHand.hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
            }
            return InteractionResult.SUCCESS;
        }

        if (context.getClickedFace() != Direction.DOWN) {
            BlockState flattened = oldState.getToolModifiedState(context, ItemAbilities.SHOVEL_FLATTEN, false);
            BlockState updatedState = null;
            if (flattened != null && level.getBlockState(pos.above()).isAir()) {
                level.playSound(player, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                updatedState = flattened;
            } else {
                BlockState doused = oldState.getToolModifiedState(context, ItemAbilities.SHOVEL_DOUSE, false);
                if (doused != null) {
                    if (!level.isClientSide()) {
                        level.levelEvent(null, 1009, pos, 0);
                    }
                    updatedState = doused;
                }
            }

            if (updatedState != null) {
                if (!level.isClientSide()) {
                    level.setBlock(pos, updatedState, 11);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, updatedState));
                    if (player != null) {
                        context.getItemInHand().hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }

        return super.useOn(context);
    }

    private static boolean playerHasBlockingItemUseIntent(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return false;
        return context.getHand().equals(InteractionHand.MAIN_HAND)
                && player.getOffhandItem().has(DataComponents.BLOCKS_ATTACKS)
                && !player.isSecondaryUseActive();
    }

    private Optional<BlockState> evaluateAxeState(Level level, BlockPos pos, @Nullable Player player, BlockState oldState, UseOnContext context) {
        BlockState stripped = oldState.getToolModifiedState(context, ItemAbilities.AXE_STRIP, false);
        if (stripped != null) {
            level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return Optional.of(stripped);
        }
        BlockState scraped = oldState.getToolModifiedState(context, ItemAbilities.AXE_SCRAPE, false);
        if (scraped != null) {
            spawnSoundAndParticle(level, pos, player, oldState, SoundEvents.AXE_SCRAPE, 3005);
            return Optional.of(scraped);
        }
        BlockState waxOff = oldState.getToolModifiedState(context, ItemAbilities.AXE_WAX_OFF, false);
        if (waxOff != null) {
            spawnSoundAndParticle(level, pos, player, oldState, SoundEvents.AXE_WAX_OFF, 3004);
            return Optional.of(waxOff);
        }
        return Optional.empty();
    }

    private static void spawnSoundAndParticle(Level level, BlockPos pos, @Nullable Player player, BlockState oldState, SoundEvent soundEvent, int particle) {
        level.playSound(player, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.levelEvent(player, particle, pos, 0);
        if (oldState.getBlock() instanceof ChestBlock && oldState.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
            BlockPos neighborPos = ChestBlock.getConnectedBlockPos(pos, oldState);
            level.gameEvent(GameEvent.BLOCK_CHANGE, neighborPos, GameEvent.Context.of(player, level.getBlockState(neighborPos)));
            level.levelEvent(player, particle, neighborPos, 0);
        }
    }

    @Override
    public boolean canPerformAction(ItemInstance stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility) ||
                ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return (state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_SHOVEL) || state.is(BlockTags.MINEABLE_WITH_AXE));
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState state) {
        return (state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_SHOVEL) || state.is(BlockTags.MINEABLE_WITH_AXE)) ? super.getDestroySpeed(pStack, Blocks.COBBLESTONE.defaultBlockState()) : 1.0F;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        if (oldStack.is(newStack.getItem())) return false;
        return super.shouldCauseBlockBreakReset(oldStack, newStack);
    }
}
