package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.tools.basetools.BaseAxe;
import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import com.direwolf20.justdirethings.common.items.tools.utils.PoweredItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CelestigemAxe extends BaseAxe implements PoweredItem {
    public CelestigemAxe() {
        super(GooTier.CELESTIGEM, 7.0F, -2.5F, new Properties());
        registerAbility(Ability.TREEFELLER);
        registerAbility(Ability.LEAFBREAKER);
        registerAbility(Ability.SMELTER);
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (mineBlocksAbility(pStack, pLevel, pState, pPos, pEntityLiving, Ability.TREEFELLER, null, oreCondition))
            return true;
        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        leafbreaker(pContext);
        return super.useOn(pContext);
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
