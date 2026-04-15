package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseBoots;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.Helpers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;

public class BlazegoldBoots extends BaseBoots {
    public BlazegoldBoots() {
        super(new Item.Properties()
                .humanoidArmor(ArmorTiers.BLAZEGOLD, ArmorType.BOOTS)
                .fireResistant()
                .durability(ArmorType.BOOTS.getDurability(25)));
        registerAbility(Ability.STEPHEIGHT);
        registerAbility(Ability.JUMPBOOST, new AbilityParams(1, 2, 1, 2));
        registerAbility(Ability.LAVAREPAIR);
        registerAbility(Ability.GROUNDSTOMP, new AbilityParams(1, 2, 1, 2, 0, 200));
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (canUseAbility(stack, Ability.LAVAREPAIR))
            return Helpers.doLavaRepair(stack, entity);
        return false;
    }
}
