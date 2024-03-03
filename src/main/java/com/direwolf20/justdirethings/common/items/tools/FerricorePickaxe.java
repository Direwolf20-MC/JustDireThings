package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.client.renderactions.ThingFinder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

import java.util.Set;

public class FerricorePickaxe extends PickaxeItem implements TieredGooItem {
    public FerricorePickaxe() {
        super(GooTier.FERRICORE, 1, -2.8F, new Item.Properties());
    }

    @Override
    public GooTier gooTier() {
        return (GooTier) this.getTier();
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            if (pState.getTags().anyMatch(tag -> tag.equals(Tags.Blocks.ORES)) && pStack.isCorrectToolForDrops(pState)) {
                Set<BlockPos> alsoBreakSet = findLikeBlocks(pLevel, pState, pPos, 16, 2); //Todo: Balance and Config?
                for (BlockPos breakPos : alsoBreakSet) {
                    breakBlocks((ServerLevel) pLevel, breakPos, pEntityLiving, pStack, pPos);
                    pStack.hurtAndBreak(1, pEntityLiving, p_40992_ -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                }
            } else {
                pStack.hurtAndBreak(1, pEntityLiving, p_40992_ -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
        }
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (itemStack.getItem() instanceof TieredGooItem tieredGooItem)
                ThingFinder.discoverOres(player, itemStack, tieredGooItem.getGooTier());
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        float defaultSpeed = super.getDestroySpeed(stack, state);
        if (state.getTags().anyMatch(tag -> tag.equals(Tags.Blocks.ORES))) {
            return defaultSpeed;
        }
        return defaultSpeed + defaultSpeed / 2;
    }
}
