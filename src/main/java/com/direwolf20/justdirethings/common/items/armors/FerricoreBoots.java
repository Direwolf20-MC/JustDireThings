package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseBoots;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import net.minecraft.world.item.Item;

public class FerricoreBoots extends BaseBoots {
    public FerricoreBoots() {
        super(ArmorTiers.FERRICORE, new Item.Properties());
        registerAbility(Ability.STEPHEIGHT);
    }
}
