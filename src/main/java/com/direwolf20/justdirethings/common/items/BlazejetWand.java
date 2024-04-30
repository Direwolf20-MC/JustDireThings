package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.BaseToggleableTool;
import com.direwolf20.justdirethings.common.items.interfaces.LeftClickableTool;

public class BlazejetWand extends BaseToggleableTool implements LeftClickableTool {
    public BlazejetWand() {
        super(new Properties()
                .fireResistant()
                .durability(200));
        registerAbility(Ability.LAVAREPAIR);
        registerAbility(Ability.AIRBURST, new AbilityParams(1, 3, 1, 1));
    }
}
