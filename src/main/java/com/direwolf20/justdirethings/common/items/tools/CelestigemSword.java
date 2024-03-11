package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.tools.basetools.BaseSword;
import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import com.direwolf20.justdirethings.common.items.tools.utils.PoweredItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CelestigemSword extends BaseSword implements PoweredItem {
    public CelestigemSword() {
        super(GooTier.CELESTIGEM, 3, -2.0F, new Properties());
        registerAbility(Ability.MOBSCANNER);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        scanFor(level, player, hand, Ability.MOBSCANNER);
        return super.use(level, player, hand);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, p_43296_ -> p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            pStack.hurtAndBreak(2, pEntityLiving, p_43276_ -> p_43276_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }

        return true;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return isPowerBarVisible(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return getPowerBarWidth(stack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int color = getPowerBarColor(stack);
        if (color == -1)
            return super.getBarColor(stack);
        return color;
    }
}
