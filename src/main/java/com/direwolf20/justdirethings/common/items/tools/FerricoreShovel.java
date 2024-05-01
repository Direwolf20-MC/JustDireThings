package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseShovel;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;

public class FerricoreShovel extends BaseShovel {
    public FerricoreShovel() {
        super(GooTier.FERRICORE, new Item.Properties()
                .attributes(ShovelItem.createAttributes(GooTier.FERRICORE, 1.5F, -3.0F)));
        registerAbility(Ability.SKYSWEEPER);
        registerAbility(Ability.LAWNMOWER);
    }

}
