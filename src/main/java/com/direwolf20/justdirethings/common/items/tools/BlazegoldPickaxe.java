package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.tools.basetools.BasePickaxe;
import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.AbilityParams;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;

public class BlazegoldPickaxe extends BasePickaxe {
    public BlazegoldPickaxe() {
        super(GooTier.BLAZEGOLD, 1, -2.8F, new Properties());
        registerAbility(Ability.ORESCANNER);
        registerAbility(Ability.OREMINER);
        registerAbility(Ability.SMELTER);
        registerAbility(Ability.HAMMER, new AbilityParams(3, 3, 2));
    }
}
