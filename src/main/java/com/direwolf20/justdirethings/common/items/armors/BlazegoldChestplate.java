package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseChestplate;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.Helpers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;

public class BlazegoldChestplate extends BaseChestplate {
    public BlazegoldChestplate() {
        super(new Item.Properties()
                .humanoidArmor(ArmorTiers.BLAZEGOLD, ArmorType.CHESTPLATE)
                .fireResistant()
                .durability(ArmorType.CHESTPLATE.getDurability(25)));
        registerAbility(Ability.INVULNERABILITY, new AbilityParams(1, 1, 1, 1, 200, 1200));
        registerAbility(Ability.LAVAREPAIR);
        registerAbility(Ability.EXTINGUISH, new AbilityParams(1, 1, 1, 1, 0, 200));
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (canUseAbility(stack, Ability.LAVAREPAIR))
            return Helpers.doLavaRepair(stack, entity);
        return false;
    }
}
