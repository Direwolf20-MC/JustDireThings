package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.items.interfaces.*;

public class VoidshiftWand extends BaseToggleableTool implements PoweredItem, LeftClickableTool {
    public VoidshiftWand() {
        super(new Properties()
                .durability(200)
                .fireResistant());
        registerAbility(Ability.AIRBURST, new AbilityParams(1, 4, 1, 4));
        registerAbility(Ability.VOIDSHIFT, new AbilityParams(1, 15, 1, 15));
    }
}
