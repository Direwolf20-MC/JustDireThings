package com.direwolf20.justdirethings.util.interfacehelpers;

import com.direwolf20.justdirethings.util.MiscHelpers;

public class RedstoneControlData {
    public boolean receivingRedstone = false;
    public boolean checkedRedstone = false;
    public boolean pulsed = false;
    public MiscHelpers.RedstoneMode redstoneMode = MiscHelpers.RedstoneMode.IGNORED;

    public RedstoneControlData() {

    }
}
