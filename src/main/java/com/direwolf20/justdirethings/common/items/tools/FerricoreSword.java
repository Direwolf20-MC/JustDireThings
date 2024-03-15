package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.tools.basetools.BaseSword;
import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.item.Item;

public class FerricoreSword extends BaseSword {
    public FerricoreSword() {
        super(GooTier.FERRICORE, 3, -2.0F, new Item.Properties());
        registerAbility(Ability.MOBSCANNER);
    }
}
