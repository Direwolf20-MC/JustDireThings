package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseBow;

public class BlazegoldBow extends BaseBow {
    public BlazegoldBow() {
        super(new Properties().durability(450));
        registerAbility(Ability.SPLASH);
        registerAbility(Ability.LAVAREPAIR);
    }

    public float getMaxDraw() {
        return 15;
    }
}
