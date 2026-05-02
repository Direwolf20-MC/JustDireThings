package com.direwolf20.justdirethings.common.blockentities.basebe;

import com.direwolf20.justdirethings.util.interfacehelpers.AreaAffectingData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

public interface AreaAffectingBE {
    double maxRadius = 5;
    int maxOffset = 9;

    BlockEntity getBlockEntity();

    AreaAffectingData getAreaAffectingData();

    default AABB getAABBOffsetOnly(BlockPos relativePos) {
        double xOffset = getAreaAffectingData().xOffset;
        double yOffset = getAreaAffectingData().yOffset;
        double zOffset = getAreaAffectingData().zOffset;
        if (getAreaAffectingData().xRadius != Math.floor(getAreaAffectingData().xRadius))
            xOffset = xOffset + 0.75;
        if (getAreaAffectingData().yRadius != Math.floor(getAreaAffectingData().yRadius))
            yOffset = yOffset + 0.75;
        if (getAreaAffectingData().zRadius != Math.floor(getAreaAffectingData().zRadius))
            zOffset = zOffset + 0.75;
        return new AABB(relativePos.getX() + xOffset, relativePos.getY() + yOffset, relativePos.getZ() + zOffset, relativePos.getX() + xOffset + (getAreaAffectingData().xRadius != Math.floor(getAreaAffectingData().xRadius) ? 0.5 : 1), relativePos.getY() + yOffset + (getAreaAffectingData().yRadius != Math.floor(getAreaAffectingData().yRadius) ? 0.5 : 1), relativePos.getZ() + zOffset + (getAreaAffectingData().zRadius != Math.floor(getAreaAffectingData().zRadius) ? 0.5 : 1));
    }

    default AreaAffectingData getDefaultAreaData() {
        return new AreaAffectingData();
    }

    default AreaAffectingData getDefaultAreaData(Direction facing) {
        return new AreaAffectingData(facing);
    }

    default void handleRotate(Direction oldDirection, Direction newDirection) {
        if (oldDirection == newDirection)
            return;
        AreaAffectingData areaAffectingData = getAreaAffectingData();
        AreaAffectingData defaultData = getDefaultAreaData(oldDirection);
        defaultData.renderArea = areaAffectingData.renderArea; //Still rotate if the renderArea's don't match
        if (!areaAffectingData.equals(defaultData))
            return;
        AreaAffectingData newAreaData = new AreaAffectingData(newDirection);
        setAreaSettings(newAreaData.xRadius, newAreaData.yRadius, newAreaData.zRadius, newAreaData.xOffset, newAreaData.yOffset, newAreaData.zOffset, areaAffectingData.renderArea);
    }

    default AABB getAABB(BlockPos relativePos) {
        if (getAreaAffectingData().area == null) {
            double xOffset = getAreaAffectingData().xOffset;
            double yOffset = getAreaAffectingData().yOffset;
            double zOffset = getAreaAffectingData().zOffset;
            if (getAreaAffectingData().xRadius != Math.floor(getAreaAffectingData().xRadius))
                xOffset = xOffset + 0.5;
            if (getAreaAffectingData().yRadius != Math.floor(getAreaAffectingData().yRadius))
                yOffset = yOffset + 0.5;
            if (getAreaAffectingData().zRadius != Math.floor(getAreaAffectingData().zRadius))
                zOffset = zOffset + 0.5;
            getAreaAffectingData().area = new AABB(relativePos.getX() + xOffset, relativePos.getY() + yOffset, relativePos.getZ() + zOffset, relativePos.getX() + xOffset + 1, relativePos.getY() + yOffset + 1, relativePos.getZ() + zOffset + 1).inflate(getAreaAffectingData().xRadius, getAreaAffectingData().yRadius, getAreaAffectingData().zRadius);
        }
        return getAreaAffectingData().area;
    }

    default void setAreaSettings(double x, double y, double z, int xo, int yo, int zo, boolean renderArea) {
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

    default void saveAreaSettings(ValueOutput output) {
        output.putDouble("xRadiusDouble", getAreaAffectingData().xRadius);
        output.putDouble("yRadiusDouble", getAreaAffectingData().yRadius);
        output.putDouble("zRadiusDouble", getAreaAffectingData().zRadius);
        output.putInt("xOffset", getAreaAffectingData().xOffset);
        output.putInt("yOffset", getAreaAffectingData().yOffset);
        output.putInt("zOffset", getAreaAffectingData().zOffset);
        output.putBoolean("renderArea", getAreaAffectingData().renderArea);
    }

    default void saveAreaOnly(ValueOutput output) {
        output.putDouble("xRadiusDouble", getAreaAffectingData().xRadius);
        output.putDouble("yRadiusDouble", getAreaAffectingData().yRadius);
        output.putDouble("zRadiusDouble", getAreaAffectingData().zRadius);
    }

    default void saveOffsetOnly(ValueOutput output) {
        output.putInt("xOffset", getAreaAffectingData().xOffset);
        output.putInt("yOffset", getAreaAffectingData().yOffset);
        output.putInt("zOffset", getAreaAffectingData().zOffset);
    }

    default void loadAreaSettings(ValueInput input) {
        AreaAffectingData data = getAreaAffectingData();
        data.xRadius = input.getDoubleOr("xRadiusDouble", data.xRadius);
        data.yRadius = input.getDoubleOr("yRadiusDouble", data.yRadius);
        data.zRadius = input.getDoubleOr("zRadiusDouble", data.zRadius);
        data.xOffset = input.getIntOr("xOffset", data.xOffset);
        data.yOffset = input.getIntOr("yOffset", data.yOffset);
        data.zOffset = input.getIntOr("zOffset", data.zOffset);
        data.renderArea = input.getBooleanOr("renderArea", data.renderArea);
    }

    default void loadAreaOnly(ValueInput input) {
        AreaAffectingData data = getAreaAffectingData();
        data.xRadius = input.getDoubleOr("xRadiusDouble", data.xRadius);
        data.yRadius = input.getDoubleOr("yRadiusDouble", data.yRadius);
        data.zRadius = input.getDoubleOr("zRadiusDouble", data.zRadius);
    }

    default void loadOffsetOnly(ValueInput input) {
        AreaAffectingData data = getAreaAffectingData();
        data.xOffset = input.getIntOr("xOffset", data.xOffset);
        data.yOffset = input.getIntOr("yOffset", data.yOffset);
        data.zOffset = input.getIntOr("zOffset", data.zOffset);
    }
}
