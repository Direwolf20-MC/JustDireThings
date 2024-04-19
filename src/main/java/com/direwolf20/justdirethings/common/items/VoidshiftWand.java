package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.BaseToggleableTool;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredItem;

public class VoidshiftWand extends BaseToggleableTool implements PoweredItem {
    public VoidshiftWand() {
        super(new Properties()
                .defaultDurability(200)
                .fireResistant());
        registerAbility(Ability.AIRBURST, new AbilityParams(1, 5, 1, 1));
        registerAbility(Ability.VOIDSHIFT);
    }
}
