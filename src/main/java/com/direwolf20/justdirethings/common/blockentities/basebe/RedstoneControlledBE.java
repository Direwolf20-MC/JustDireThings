package com.direwolf20.justdirethings.common.blockentities.basebe;

import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.interfacehelpers.RedstoneControlData;
import net.minecraft.nbt.CompoundTag;

public interface RedstoneControlledBE extends BaseBEInterface {
    RedstoneControlData getRedstoneControlData();

    default void setRedstoneSettings(int redstoneMode) {
        getRedstoneControlData().redstoneMode = MiscHelpers.RedstoneMode.values()[redstoneMode];
        markDirtyClient();
    }

    default void tickServer() {
        if (!getRedstoneControlData().checkedRedstone)
            evaluateRedstone();
    }

    default void evaluateRedstone() {
        boolean newRedstoneSignal = getBlockEntity().getLevel().hasNeighborSignal(getBlockEntity().getBlockPos());
        if (getRedstoneControlData().redstoneMode.equals(MiscHelpers.RedstoneMode.PULSE) && !getRedstoneControlData().receivingRedstone && newRedstoneSignal)
            getRedstoneControlData().pulsed = true;
        getRedstoneControlData().receivingRedstone = newRedstoneSignal;
        getRedstoneControlData().checkedRedstone = true;
    }

    default boolean isActive() {
        if (getRedstoneControlData().redstoneMode.equals(MiscHelpers.RedstoneMode.IGNORED))
            return true;
        if (getRedstoneControlData().redstoneMode.equals(MiscHelpers.RedstoneMode.LOW))
            return !getRedstoneControlData().receivingRedstone;
        if (getRedstoneControlData().redstoneMode.equals(MiscHelpers.RedstoneMode.HIGH))
            return getRedstoneControlData().receivingRedstone;
        if (getRedstoneControlData().redstoneMode.equals(MiscHelpers.RedstoneMode.PULSE) && getRedstoneControlData().pulsed) {
            getRedstoneControlData().pulsed = false;
            return true;
        }
        return false;
    }

    default void saveRedstoneSettings(CompoundTag tag) {
        tag.putInt("redstoneMode", getRedstoneControlData().redstoneMode.ordinal());
    }

    default void loadRedstoneSettings(CompoundTag tag) {
        if (tag.contains("redstoneMode")) { //Assume all the others are there too...
            getRedstoneControlData().redstoneMode = MiscHelpers.RedstoneMode.values()[tag.getInt("redstoneMode")];
        }
    }
}
