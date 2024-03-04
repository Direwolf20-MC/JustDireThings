package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.tools.basetools.BaseShovel;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import com.direwolf20.justdirethings.common.items.tools.utils.ToolAbility;
import com.direwolf20.justdirethings.datagen.JustDireBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.direwolf20.justdirethings.common.items.tools.utils.Helpers.*;

public class FerricoreShovel extends BaseShovel {
    public FerricoreShovel() {
        super(GooTier.FERRICORE, 1.5F, -3.0F, new Item.Properties());
        registerAbility(ToolAbility.SKYSWEEPER);
        registerAbility(ToolAbility.LAWNMOWER);
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            if (canUseAbility(pStack, ToolAbility.SKYSWEEPER) && pState.getBlock() instanceof FallingBlock && pStack.isCorrectToolForDrops(pState)) {
                Set<BlockPos> alsoBreakSet = findLikeBlocks(pLevel, pState, pPos, 64, Direction.UP, 24); //Todo: Balance and Config?
                for (BlockPos breakPos : alsoBreakSet) {
                    breakBlocks((ServerLevel) pLevel, breakPos, pEntityLiving, pStack, pPos);
                    pStack.hurtAndBreak(ToolAbility.SKYSWEEPER.getDurabilityCost(), pEntityLiving, p_40992_ -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                }
            } else {
                pStack.hurtAndBreak(1, pEntityLiving, p_40992_ -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
        }

        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!level.isClientSide && canUseAbility(itemStack, ToolAbility.LAWNMOWER)) {
            List<TagKey<Block>> tags = new ArrayList<>();
            tags.add(JustDireBlockTags.LAWNMOWERABLE);
            Set<BlockPos> breakBlocks = findTaggedBlocks(level, tags, player.getOnPos(), 64, 5); //TODO Balance/Config?
            for (BlockPos breakPos : breakBlocks) {
                breakBlocks((ServerLevel) level, breakPos, player, itemStack);
                if (Math.random() < 0.1) //10% chance to damage tool
                    itemStack.hurtAndBreak(ToolAbility.LAWNMOWER.getDurabilityCost(), player, p_40992_ -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
        }
        return super.use(level, player, hand);
    }
}
