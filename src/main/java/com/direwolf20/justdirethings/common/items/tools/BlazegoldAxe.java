package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.tools.basetools.BaseAxe;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import com.direwolf20.justdirethings.common.items.tools.utils.ToolAbility;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlazegoldAxe extends BaseAxe {
    public BlazegoldAxe() {
        super(GooTier.BLAZEGOLD, 7.0F, -2.5F, new Properties());
        registerAbility(ToolAbility.TREEFELLER);
        registerAbility(ToolAbility.LEAFBREAKER);
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (mineBlocksAbility(pStack, pLevel, pState, pPos, pEntityLiving, ToolAbility.TREEFELLER, null, oreCondition))
            return true;
        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        leafbreaker(pContext);
        return super.useOn(pContext);
    }
}
