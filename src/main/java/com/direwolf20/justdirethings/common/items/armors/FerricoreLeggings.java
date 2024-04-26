package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseLeggings;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;

public class FerricoreLeggings extends BaseLeggings {
    public FerricoreLeggings() {
        super(ArmorTiers.FERRICORE, new Properties());
        registerAbility(Ability.RUNSPEED, new AbilityParams(1, 3, 1));
    }
}
