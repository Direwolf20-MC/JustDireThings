package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseLeggings;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import net.minecraft.world.item.Item;

public class FerricoreLeggings extends BaseLeggings {
    public FerricoreLeggings(Item.Properties pProperties) {
        super(pProperties);
        registerAbility(Ability.RUNSPEED, new AbilityParams(1, 1, 1));
        registerAbility(Ability.WALKSPEED, new AbilityParams(1, 1, 1));
    }
}
