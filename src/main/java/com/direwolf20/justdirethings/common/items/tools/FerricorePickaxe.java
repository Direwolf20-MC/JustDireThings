package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.tools.basetools.BasePickaxe;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.item.Item;

public class FerricorePickaxe extends BasePickaxe {
    public FerricorePickaxe() {
        super(GooTier.FERRICORE, 1, -2.8F, new Item.Properties());
        registerAbility(Ability.ORESCANNER);
        registerAbility(Ability.OREMINER);
    }
}
