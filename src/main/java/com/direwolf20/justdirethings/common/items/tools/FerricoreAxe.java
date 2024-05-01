package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseAxe;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;

public class FerricoreAxe extends BaseAxe {
    public FerricoreAxe() {
        super(GooTier.FERRICORE, new Item.Properties()
                .attributes(AxeItem.createAttributes(GooTier.FERRICORE, 7.0F, -2.5F)));
        registerAbility(Ability.TREEFELLER);
        registerAbility(Ability.LEAFBREAKER);
    }
}
