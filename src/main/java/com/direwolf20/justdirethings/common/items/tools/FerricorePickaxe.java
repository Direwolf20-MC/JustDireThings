package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.GooTieredItem;
import com.direwolf20.justdirethings.common.items.tools.basetools.BasePickaxe;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.item.Item;

public class FerricorePickaxe extends BasePickaxe implements GooTieredItem {
    public FerricorePickaxe(Item.Properties pProperties) {
        super(pProperties);
        registerAbility(Ability.ORESCANNER);
        registerAbility(Ability.OREMINER);
    }

    @Override
    public GooTier getGooTier() {
        return GooTier.FERRICORE;
    }
}
