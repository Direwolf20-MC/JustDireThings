package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.GooTieredItem;
import com.direwolf20.justdirethings.common.items.interfaces.Helpers;
import com.direwolf20.justdirethings.common.items.tools.basetools.BasePickaxe;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BlazegoldPickaxe extends BasePickaxe implements GooTieredItem {
    public BlazegoldPickaxe() {
        super(new Item.Properties()
                .pickaxe(GooTier.BLAZEGOLD.material(), 1.0F, -2.8F)
                .fireResistant());
        registerAbility(Ability.ORESCANNER);
        registerAbility(Ability.OREMINER);
        registerAbility(Ability.SMELTER);
        registerAbility(Ability.HAMMER, new AbilityParams(3, 3, 2));
        registerAbility(Ability.LAVAREPAIR);
    }

    @Override
    public GooTier getGooTier() {
        return GooTier.BLAZEGOLD;
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (canUseAbility(stack, Ability.LAVAREPAIR))
            return Helpers.doLavaRepair(stack, entity);
        return false;
    }
}
