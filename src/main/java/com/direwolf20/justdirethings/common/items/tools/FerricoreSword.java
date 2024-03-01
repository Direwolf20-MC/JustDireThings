package com.direwolf20.justdirethings.common.items.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class FerricoreSword extends SwordItem implements FerricoreItem {
    public FerricoreSword() {
        super(Tiers.IRON, 3, -2.0F, new Item.Properties());
    }

}
