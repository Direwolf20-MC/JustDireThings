package com.direwolf20.justdirethings.common.items.resources;

import net.minecraft.world.item.Item;

public class Coal_T4 extends Coal_T1 {
    public Coal_T4(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getBurnSpeedMultiplier() {
        return 16;
    }
}
