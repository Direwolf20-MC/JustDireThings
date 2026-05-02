package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseHelmet;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import net.minecraft.world.item.Item;

public class FerricoreHelmet extends BaseHelmet {
    public FerricoreHelmet(Item.Properties pProperties) {
        super(pProperties);
        registerAbility(Ability.MINDFOG);
    }
}
