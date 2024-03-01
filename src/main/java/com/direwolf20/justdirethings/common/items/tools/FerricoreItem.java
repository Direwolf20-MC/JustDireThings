package com.direwolf20.justdirethings.common.items.tools;

public interface FerricoreItem extends TieredGooItem {
    default int getGooTier() {
        return 1;
    }
}
