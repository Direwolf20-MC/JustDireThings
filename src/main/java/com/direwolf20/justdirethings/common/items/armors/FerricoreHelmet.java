package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseHelmet;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;

public class FerricoreHelmet extends BaseHelmet {
    public FerricoreHelmet() {
        super(new Item.Properties().humanoidArmor(ArmorTiers.FERRICORE, ArmorType.HELMET));
        registerAbility(Ability.MINDFOG);
    }
}
