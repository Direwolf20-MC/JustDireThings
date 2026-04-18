package com.direwolf20.justdirethings.common.items.resources;

import net.minecraft.world.item.Item;

public class Coal_T1 extends Item {
    public Coal_T1(Properties pProperties) {
        super(pProperties);
    }

    public int getBurnSpeedMultiplier() {
        return 2;
    }
}
