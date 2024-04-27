package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.items.interfaces.*;

public class VoidshiftWand extends BaseToggleableTool implements PoweredItem, LeftClickableTool {
    public VoidshiftWand() {
        super(new Properties()
                .defaultDurability(200)
                .fireResistant());
        registerAbility(Ability.AIRBURST, new AbilityParams(1, 5, 1, 1));
        registerAbility(Ability.VOIDSHIFT, new AbilityParams(1, 15, 1, 15));
    }
}
