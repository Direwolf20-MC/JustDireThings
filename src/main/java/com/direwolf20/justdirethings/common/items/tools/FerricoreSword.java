package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.GooTieredItem;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseSword;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.item.Item;

public class FerricoreSword extends BaseSword implements GooTieredItem {
    public FerricoreSword() {
        super(new Item.Properties().sword(GooTier.FERRICORE.material(), 3, -2.0F));
        registerAbility(Ability.MOBSCANNER);
    }

    @Override
    public GooTier getGooTier() {
        return GooTier.FERRICORE;
    }
}
