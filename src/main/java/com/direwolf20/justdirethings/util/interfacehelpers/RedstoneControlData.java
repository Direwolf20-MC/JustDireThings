package com.direwolf20.justdirethings.util.interfacehelpers;

import com.direwolf20.justdirethings.util.MiscHelpers;

import java.util.Objects;

public class RedstoneControlData {
    public boolean receivingRedstone = false;
    public boolean checkedRedstone = false;
    public boolean pulsed = false;
    public MiscHelpers.RedstoneMode redstoneMode = MiscHelpers.RedstoneMode.IGNORED;

    public RedstoneControlData() {

    }

    public RedstoneControlData(MiscHelpers.RedstoneMode redstoneMode) {
        this.redstoneMode = redstoneMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(receivingRedstone, pulsed, redstoneMode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RedstoneControlData that = (RedstoneControlData) o;
        return receivingRedstone == that.receivingRedstone &&
                pulsed == that.pulsed &&
                redstoneMode == that.redstoneMode;
    }
}
