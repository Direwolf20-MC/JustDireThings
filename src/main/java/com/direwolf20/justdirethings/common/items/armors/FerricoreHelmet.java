package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseHelmet;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;

public class FerricoreHelmet extends BaseHelmet {
    public FerricoreHelmet() {
        super(ArmorTiers.FERRICORE, new Properties());
        registerAbility(Ability.MINDFOG);
    }
}
