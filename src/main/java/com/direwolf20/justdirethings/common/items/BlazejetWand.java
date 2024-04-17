package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.BaseToggleableTool;

public class BlazejetWand extends BaseToggleableTool {
    public BlazejetWand() {
        super(new Properties()
                .fireResistant()
                .defaultDurability(100));
        registerAbility(Ability.LAVAREPAIR);
        registerAbility(Ability.AIRBURST);
    }
}
