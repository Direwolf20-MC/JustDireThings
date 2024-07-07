package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseHelmet;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.Helpers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public class BlazegoldHelmet extends BaseHelmet {
    public BlazegoldHelmet() {
        super(ArmorTiers.BLAZEGOLD, new Properties()
                .fireResistant()
                .durability(Type.HELMET.getDurability(25)));
        registerAbility(Ability.MINDFOG);
        registerAbility(Ability.LAVAREPAIR);
        registerAbility(Ability.STUPEFY, new AbilityParams(1, 1, 1, 1, 100, 600));
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (canUseAbility(stack, Ability.LAVAREPAIR))
            return Helpers.doLavaRepair(stack, entity);
        return false;
    }
}
