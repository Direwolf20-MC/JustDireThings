package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.items.interfaces.*;

public class EclipsegateWand extends BaseToggleableTool implements PoweredItem, LeftClickableTool {
    public EclipsegateWand() {
        super(new Properties()
                .defaultDurability(200)
                .fireResistant());
        registerAbility(Ability.AIRBURST, new AbilityParams(1, 10, 1, 1));
        registerAbility(Ability.VOIDSHIFT, new AbilityParams(1, 30, 1, 30));
        registerAbility(Ability.ECLIPSEGATE);
    }

    @Override
    public int getMaxEnergy() {
        return 100000;
    }
}
