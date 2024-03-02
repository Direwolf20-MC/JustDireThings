package com.direwolf20.justdirethings.common.items.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;

public class FerricoreShovel extends ShovelItem implements TieredGooItem {
    public FerricoreShovel() {
        super(GooTier.FERRICORE, 1.5F, -3.0F, new Item.Properties());
    }

    @Override
    public GooTier gooTier() {
        return (GooTier) this.getTier();
    }
}
