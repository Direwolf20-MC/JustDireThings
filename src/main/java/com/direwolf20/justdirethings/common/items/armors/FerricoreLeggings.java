package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseLeggings;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;

public class FerricoreLeggings extends BaseLeggings {
    public FerricoreLeggings() {
        super(new Item.Properties().humanoidArmor(ArmorTiers.FERRICORE, ArmorType.LEGGINGS));
        registerAbility(Ability.RUNSPEED, new AbilityParams(1, 1, 1));
        registerAbility(Ability.WALKSPEED, new AbilityParams(1, 1, 1));
    }
}
