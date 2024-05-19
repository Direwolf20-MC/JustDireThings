package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.Helpers;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseSword;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

public class BlazegoldSword extends BaseSword {
    public BlazegoldSword() {
        super(GooTier.BLAZEGOLD, new Properties()
                .attributes(SwordItem.createAttributes(GooTier.BLAZEGOLD, 3, -2.0F))
                .fireResistant());
        registerAbility(Ability.MOBSCANNER);
        registerAbility(Ability.LAVAREPAIR);
        registerAbility(Ability.CAUTERIZEWOUNDS, new AbilityParams(1, 1, 1, 1, 0, 1200));
        registerAbility(Ability.SMOKER);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (canUseAbility(stack, Ability.LAVAREPAIR))
            return Helpers.doLavaRepair(stack, entity);
        return false;
    }
}
