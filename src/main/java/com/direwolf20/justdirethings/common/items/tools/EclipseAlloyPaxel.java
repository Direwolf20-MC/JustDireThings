package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredTool;
import com.direwolf20.justdirethings.common.items.tools.basetools.BasePickaxe;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbility;

public class EclipseAlloyPaxel extends BasePickaxe implements PoweredTool {
    public EclipseAlloyPaxel() {
        super(GooTier.ECLIPSEALLOY, new Properties()
                .attributes(PickaxeItem.createAttributes(GooTier.ECLIPSEALLOY, 1.0F, -2.8F))
                .fireResistant());
        registerAbility(Ability.OREXRAY);
        registerAbility(Ability.OREMINER);
        registerAbility(Ability.SKYSWEEPER);
        registerAbility(Ability.LAWNMOWER);
        registerAbility(Ability.TREEFELLER);
        registerAbility(Ability.LEAFBREAKER);
        registerAbility(Ability.SMELTER);
        registerAbility(Ability.HAMMER, new AbilityParams(3, 7, 2));
        registerAbility(Ability.DROPTELEPORT);
        registerAbility(Ability.INSTABREAK);
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

    @Override
    public int getMaxEnergy() {
        return 500000;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return net.neoforged.neoforge.common.ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility) ||
                net.neoforged.neoforge.common.ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility) ||
                net.neoforged.neoforge.common.ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return (state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_SHOVEL) || state.is(BlockTags.MINEABLE_WITH_AXE));
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState state) {
        return (state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_SHOVEL) || state.is(BlockTags.MINEABLE_WITH_AXE)) ? super.getDestroySpeed(pStack, Blocks.COBBLESTONE.defaultBlockState()) : 1.0F; //Possible hacky way to do this? :)
    }
}
