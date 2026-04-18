package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.GooTieredItem;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseShovel;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.item.Item;

public class FerricoreShovel extends BaseShovel implements GooTieredItem {
    public FerricoreShovel(Item.Properties pProperties) {
        super(GooTier.FERRICORE.material(), 1.5F, -3.0F, pProperties);
        registerAbility(Ability.SKYSWEEPER);
        registerAbility(Ability.LAWNMOWER);
    }

    @Override
    public GooTier getGooTier() {
        return GooTier.FERRICORE;
    }
}
