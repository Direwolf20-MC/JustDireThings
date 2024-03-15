package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.tools.basetools.BasePickaxe;
import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.AbilityParams;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import com.direwolf20.justdirethings.common.items.tools.utils.PoweredItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CelestigemPickaxe extends BasePickaxe implements PoweredItem {
    public CelestigemPickaxe() {
        super(GooTier.CELESTIGEM, 1, -2.8F, new Properties());
        registerAbility(Ability.ORESCANNER);
        registerAbility(Ability.OREMINER);
        registerAbility(Ability.SMELTER);
        registerAbility(Ability.HAMMER, new AbilityParams(3, 5, 2));
        registerAbility(Ability.DROPTELEPORT);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        scanFor(level, player, hand, Ability.ORESCANNER);
        return super.use(level, player, hand);
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
