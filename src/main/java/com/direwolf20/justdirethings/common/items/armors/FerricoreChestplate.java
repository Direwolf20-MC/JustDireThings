package com.direwolf20.justdirethings.common.items.armors;

import com.direwolf20.justdirethings.common.items.armors.basearmors.BaseChestplate;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.AbilityParams;
import net.minecraft.world.item.ArmorItem;

public class FerricoreChestplate extends BaseChestplate {
    public FerricoreChestplate() {
        super(ArmorTiers.FERRICORE, new Properties()
                .durability(ArmorItem.Type.CHESTPLATE.getDurability(15)));
        registerAbility(Ability.INVULNERABILITY, new AbilityParams(1, 1, 1, 1, 200, 1200));
    }
}
