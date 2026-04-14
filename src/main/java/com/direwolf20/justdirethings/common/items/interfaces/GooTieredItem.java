package com.direwolf20.justdirethings.common.items.interfaces;

import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;

/**
 * Marker for tools/items that are ranked by a {@link GooTier}. Replaces the old
 * {@code TieredItem} check — 26.1 deleted {@code TieredItem} in favour of
 * {@link net.minecraft.world.item.ToolMaterial}-based properties, so base tools
 * no longer expose a tier via inheritance. Concrete tool classes (Stage 9)
 * implement this to carry their {@link GooTier}.
 */
public interface GooTieredItem {
    GooTier getGooTier();
}
