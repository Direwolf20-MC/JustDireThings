package com.direwolf20.justdirethings.common.containers.basecontainers;

import com.direwolf20.justdirethings.common.blockentities.basebe.AreaAffectingBE;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public abstract class AreaAffectingContainer extends BaseContainer {
    public AreaAffectingBE areaAffectingBE;

    public AreaAffectingContainer(@Nullable MenuType<?> p_i50105_1_, int p_i50105_2_) {
        super(p_i50105_1_, p_i50105_2_);
    }
}
