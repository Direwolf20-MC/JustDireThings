package com.direwolf20.justdirethings.common.items.tools.utils;

public interface TieredGooItem {
    GooTier gooTier();

    default int getGooTier() {
        return gooTier().getGooTier();
    }
}
