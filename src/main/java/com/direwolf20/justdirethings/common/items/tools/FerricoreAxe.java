package com.direwolf20.justdirethings.common.items.tools;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;

public class FerricoreAxe extends AxeItem implements TieredGooItem {
    public FerricoreAxe() {
        super(GooTier.FERRICORE, 7.0F, -2.5F, new Item.Properties());
    }

    @Override
    public GooTier gooTier() {
        return (GooTier) this.getTier();
    }
}
