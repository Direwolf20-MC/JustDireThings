package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.tools.basetools.BaseShovel;
import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.item.Item;

public class FerricoreShovel extends BaseShovel {
    public FerricoreShovel() {
        super(GooTier.FERRICORE, 1.5F, -3.0F, new Item.Properties());
        registerAbility(Ability.SKYSWEEPER);
        registerAbility(Ability.LAWNMOWER);
    }

}
