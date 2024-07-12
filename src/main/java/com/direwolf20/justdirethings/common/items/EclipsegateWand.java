package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.items.interfaces.*;

public class EclipsegateWand extends BaseToggleableTool implements PoweredItem, LeftClickableTool {
    public EclipsegateWand() {
        super(new Properties()
                .durability(200)
                .fireResistant());
        registerAbility(Ability.AIRBURST, new AbilityParams(1, 8, 1, 8));
        registerAbility(Ability.VOIDSHIFT, new AbilityParams(1, 30, 1, 30));
        registerAbility(Ability.ECLIPSEGATE, new AbilityParams(1, 20, 1, 20));
    }

    @Override
    public int getMaxEnergy() {
        return 100000;
    }
}
