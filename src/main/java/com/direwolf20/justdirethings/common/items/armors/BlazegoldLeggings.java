package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseLeggings;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.Helpers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public class BlazegoldLeggings extends BaseLeggings {
    public BlazegoldLeggings() {
        super(ArmorTiers.BLAZEGOLD, new Properties()
                .fireResistant()
                .durability(Type.LEGGINGS.getDurability(25)));
        registerAbility(Ability.RUNSPEED, new AbilityParams(1, 2, 1));
        registerAbility(Ability.WALKSPEED, new AbilityParams(1, 2, 1));
        registerAbility(Ability.LAVAREPAIR);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (canUseAbility(stack, Ability.LAVAREPAIR))
            return Helpers.doLavaRepair(stack, entity);
        return false;
    }
}
