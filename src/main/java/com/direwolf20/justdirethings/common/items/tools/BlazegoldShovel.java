package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.tools.basetools.BaseShovel;
import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.AbilityParams;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;

public class BlazegoldShovel extends BaseShovel {
    public BlazegoldShovel() {
        super(GooTier.BLAZEGOLD, 1.5F, -3.0F, new Properties());
        registerAbility(Ability.SKYSWEEPER);
        registerAbility(Ability.LAWNMOWER);
        registerAbility(Ability.SMELTER);
        registerAbility(Ability.HAMMER, new AbilityParams(3, 3, 2));
    }
}
