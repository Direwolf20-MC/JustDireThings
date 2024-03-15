package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.tools.basetools.BaseSword;
import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;

public class BlazegoldSword extends BaseSword {
    public BlazegoldSword() {
        super(GooTier.BLAZEGOLD, 3, -2.0F, new Properties());
        registerAbility(Ability.MOBSCANNER);
    }
}
