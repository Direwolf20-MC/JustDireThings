package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.tools.basetools.BaseHoe;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.item.Item;

public class FerricoreHoe extends BaseHoe {
    public FerricoreHoe() {
        super(GooTier.FERRICORE, -2, -1.0F, new Item.Properties());
    }
}
