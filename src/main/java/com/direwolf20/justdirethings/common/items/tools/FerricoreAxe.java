package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseAxe;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.item.Item;

public class FerricoreAxe extends BaseAxe {
    public FerricoreAxe() {
        super(GooTier.FERRICORE, 7.0F, -2.5F, new Item.Properties());
        registerAbility(Ability.TREEFELLER);
        registerAbility(Ability.LEAFBREAKER);
    }
}
