package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.tools.basetools.BasePickaxe;
import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.AbilityParams;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BlazegoldPickaxe extends BasePickaxe {
    public BlazegoldPickaxe() {
        super(GooTier.BLAZEGOLD, 1, -2.8F, new Properties());
        registerAbility(Ability.ORESCANNER);
        registerAbility(Ability.OREMINER);
        registerAbility(Ability.SMELTER);
        registerAbility(Ability.HAMMER, new AbilityParams(3, 3, 2));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        scanFor(level, player, hand, Ability.ORESCANNER);
        return super.use(level, player, hand);
    }
}
