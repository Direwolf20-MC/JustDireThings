package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.Helpers;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseHoe;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;

public class BlazegoldHoe extends BaseHoe {
    public BlazegoldHoe() {
        super(GooTier.BLAZEGOLD, new Properties()
                .attributes(HoeItem.createAttributes(GooTier.BLAZEGOLD, -2.0F, -1.0F))
                .fireResistant());
        registerAbility(Ability.LAVAREPAIR);
        registerAbility(Ability.HAMMER, new AbilityParams(3, 3, 2));
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (canUseAbility(stack, Ability.LAVAREPAIR))
            return Helpers.doLavaRepair(stack, entity);
        return false;
    }
}
