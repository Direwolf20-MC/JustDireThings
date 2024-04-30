package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseBoots;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;

public class FerricoreBoots extends BaseBoots {
    public FerricoreBoots() {
        super(ArmorTiers.FERRICORE, new Item.Properties()
                .durability(ArmorItem.Type.BOOTS.getDurability(15)));
        registerAbility(Ability.STEPHEIGHT);
        registerAbility(Ability.JUMPBOOST, new AbilityParams(1, 1, 1, 1));
    }
}
