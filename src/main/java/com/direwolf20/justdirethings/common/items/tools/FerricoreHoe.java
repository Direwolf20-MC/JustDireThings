package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.GooTieredItem;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseHoe;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.item.Item;

public class FerricoreHoe extends BaseHoe implements GooTieredItem {
    public FerricoreHoe(Item.Properties pProperties) {
        super(GooTier.FERRICORE.material(), -2.0F, -1.0F, pProperties);
    }

    @Override
    public GooTier getGooTier() {
        return GooTier.FERRICORE;
    }
}
