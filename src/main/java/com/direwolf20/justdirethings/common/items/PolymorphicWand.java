package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.BaseToggleableTool;
import com.direwolf20.justdirethings.common.items.interfaces.LeftClickableTool;

public class PolymorphicWand extends BaseToggleableTool implements LeftClickableTool {
    public PolymorphicWand() {
        super(new Properties()
                .fireResistant()
                .durability(200));
        registerAbility(Ability.LAVAREPAIR);
        //registerAbility(Ability.AIRBURST, new AbilityParams(1, 2, 1, 2));
    }
}
