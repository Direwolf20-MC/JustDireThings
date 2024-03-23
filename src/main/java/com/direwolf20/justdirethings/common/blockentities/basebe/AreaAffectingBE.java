package com.direwolf20.justdirethings.common.blockentities.basebe;

import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

public interface AreaAffectingBE {
    int maxRadius = 5;
    int maxOffset = 9;

    BlockEntity getBlockEntity();

    AreaAffectingData getAreaAffectingData();

    default AABB getAABB(BlockPos relativePos) {
        if (getAreaAffectingData().area == null)
            getAreaAffectingData().area = new AABB(relativePos.offset(getAreaAffectingData().xOffset, getAreaAffectingData().yOffset, getAreaAffectingData().zOffset)).inflate(getAreaAffectingData().xRadius, getAreaAffectingData().yRadius, getAreaAffectingData().zRadius);
        return getAreaAffectingData().area;
    }

    default void setAreaSettings(int x, int y, int z, int xo, int yo, int zo, boolean renderArea) {
        getAreaAffectingData().xRadius = Math.max(0, Math.min(x, maxRadius));
        getAreaAffectingData().yRadius = Math.max(0, Math.min(y, maxRadius));
        getAreaAffectingData().zRadius = Math.max(0, Math.min(z, maxRadius));
        getAreaAffectingData().xOffset = Math.max(-maxOffset, Math.min(xo, maxOffset));
        getAreaAffectingData().yOffset = Math.max(-maxOffset, Math.min(yo, maxOffset));
        getAreaAffectingData().zOffset = Math.max(-maxOffset, Math.min(zo, maxOffset));
        getAreaAffectingData().renderArea = renderArea;
        getAreaAffectingData().area = null;
        if (getBlockEntity() instanceof BaseMachineBE baseMachineBE)
            baseMachineBE.markDirtyClient();
    }

    default void tickClient() {
    }

    default void saveAreaSettings(CompoundTag tag) {
        tag.putInt("xRadius", getAreaAffectingData().xRadius);
        tag.putInt("yRadius", getAreaAffectingData().yRadius);
        tag.putInt("zRadius", getAreaAffectingData().zRadius);
        tag.putInt("xOffset", getAreaAffectingData().xOffset);
        tag.putInt("yOffset", getAreaAffectingData().yOffset);
        tag.putInt("zOffset", getAreaAffectingData().zOffset);
        tag.putBoolean("renderArea", getAreaAffectingData().renderArea);
    }

    default void loadAreaSettings(CompoundTag tag) {
        if (tag.contains("xRadius")) { //Assume all the others are there too...
            getAreaAffectingData().xRadius = tag.getInt("xRadius");
            getAreaAffectingData().yRadius = tag.getInt("yRadius");
            getAreaAffectingData().zRadius = tag.getInt("zRadius");
            getAreaAffectingData().xOffset = tag.getInt("xOffset");
            getAreaAffectingData().yOffset = tag.getInt("yOffset");
            getAreaAffectingData().zOffset = tag.getInt("zOffset");
            getAreaAffectingData().renderArea = tag.getBoolean("renderArea");
        }
    }
}
