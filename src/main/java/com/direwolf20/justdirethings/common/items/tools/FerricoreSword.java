package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseSword;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;

public class FerricoreSword extends BaseSword {
    public FerricoreSword() {
        super(GooTier.FERRICORE, new Item.Properties()
                .attributes(SwordItem.createAttributes(GooTier.FERRICORE, 3, -2.0F)));
        registerAbility(Ability.MOBSCANNER);
    }
}
