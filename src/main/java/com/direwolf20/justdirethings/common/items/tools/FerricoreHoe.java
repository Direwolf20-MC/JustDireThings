package com.direwolf20.justdirethings.common.items.tools;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;

public class FerricoreHoe extends HoeItem implements TieredGooItem {
    public FerricoreHoe() {
        super(GooTier.FERRICORE, -2, -1.0F, new Item.Properties());
    }

    @Override
    public GooTier gooTier() {
        return (GooTier) this.getTier();
    }

}
