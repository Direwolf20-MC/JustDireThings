package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseBow;

public class FerricoreBow extends BaseBow {
    public FerricoreBow(Properties pProperties) {
        super(pProperties);
        registerAbility(Ability.POTIONARROW);
    }
}
