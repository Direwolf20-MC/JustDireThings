package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.tools.basetools.BasePickaxe;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import com.direwolf20.justdirethings.common.items.tools.utils.ToolAbility;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

import java.util.function.Predicate;

public class BlazegoldPickaxe extends BasePickaxe {
    public BlazegoldPickaxe() {
        super(GooTier.BLAZEGOLD, 1, -2.8F, new Properties());
        registerAbility(ToolAbility.ORESCANNER);
        registerAbility(ToolAbility.OREMINER);
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        Predicate<BlockState> oreCondition = s -> s.getTags().anyMatch(tag -> tag.equals(Tags.Blocks.ORES));
        if (mineBlocksAbility(pStack, pLevel, pState, pPos, pEntityLiving, ToolAbility.OREMINER, null, oreCondition))
            return true;
        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        scanFor(level, player, hand, ToolAbility.ORESCANNER);
        return super.use(level, player, hand);
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
