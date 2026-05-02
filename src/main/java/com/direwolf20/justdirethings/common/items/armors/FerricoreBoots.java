package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseBoots;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import net.minecraft.world.item.Item;

public class FerricoreBoots extends BaseBoots {
    public FerricoreBoots(Item.Properties pProperties) {
        super(pProperties);
        registerAbility(Ability.STEPHEIGHT);
        registerAbility(Ability.JUMPBOOST, new AbilityParams(1, 1, 1, 1));
    }
}
