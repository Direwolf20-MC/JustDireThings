package com.direwolf20.justdirethings.common.items.tools;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;

public class FerricoreHoe extends HoeItem implements FerricoreItem {
    public FerricoreHoe() {
        super(Tiers.IRON, -2, -1.0F, new Item.Properties());
    }
}
