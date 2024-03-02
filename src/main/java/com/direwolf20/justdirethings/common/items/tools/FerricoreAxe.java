package com.direwolf20.justdirethings.common.items.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FerricoreAxe extends AxeItem implements TieredGooItem {
    public FerricoreAxe() {
        super(GooTier.FERRICORE, 7.0F, -2.5F, new Item.Properties());
    }

    @Override
    public GooTier gooTier() {
        return (GooTier) this.getTier();
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            List<TagKey<Block>> extraTags = new ArrayList<>();
            extraTags.add(BlockTags.LEAVES);
            if (pState.getTags().anyMatch(tag -> tag.equals(BlockTags.LOGS))) {
                Set<BlockPos> alsoBreakSet = findLikeBlocks(pLevel, pState, pPos, 64, 2); //Todo: Balance and Config?
                for (BlockPos breakPos : alsoBreakSet) {
                    breakBlocks((ServerLevel) pLevel, breakPos, pEntityLiving, pStack);
                    pStack.hurtAndBreak(1, pEntityLiving, p_40992_ -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                }
            } else {
                pStack.hurtAndBreak(1, pEntityLiving, p_40992_ -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
        }

        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level pLevel = pContext.getLevel();
        BlockPos pPos = pContext.getClickedPos();
        BlockState pState = pLevel.getBlockState(pPos);
        LivingEntity pEntityLiving = pContext.getPlayer();
        ItemStack pStack = pContext.getItemInHand();
        if (!pLevel.isClientSide) {
            if (pState.getTags().anyMatch(tag -> tag.equals(BlockTags.LEAVES))) {
                Set<BlockPos> alsoBreakSet = findLikeBlocks(pLevel, pState, pPos, 64, 2); //Todo: Balance and Config?
                System.out.println(alsoBreakSet.size());
                for (BlockPos breakPos : alsoBreakSet) {
                    breakBlocks((ServerLevel) pLevel, breakPos, pEntityLiving, pStack);
                    pLevel.sendBlockUpdated(breakPos, pState, pLevel.getBlockState(breakPos), 3); // I have NO IDEA why this is necessary
                    if (Math.random() < 0.1) //10% chance to damage tool
                        pStack.hurtAndBreak(1, pEntityLiving, p_40992_ -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                }
            }
        }
        return super.useOn(pContext);
    }
}
