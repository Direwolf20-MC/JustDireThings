package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import com.direwolf20.justdirethings.common.items.interfaces.BaseToggleableTool;
import com.direwolf20.justdirethings.common.items.interfaces.LeftClickableTool;

public class BlazejetWand extends BaseToggleableTool implements LeftClickableTool {
    public BlazejetWand(Properties pProperties) {
        super(pProperties);
        registerAbility(Ability.LAVAREPAIR);
        registerAbility(Ability.AIRBURST, new AbilityParams(1, 2, 1, 2));
    }
}
