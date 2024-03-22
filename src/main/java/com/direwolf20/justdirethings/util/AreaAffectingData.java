package com.direwolf20.justdirethings.util;

import net.minecraft.world.phys.AABB;

public class AreaAffectingData {
    public int xRadius = 3, yRadius = 3, zRadius = 3;
    public int xOffset = 0, yOffset = 0, zOffset = 0;
    public boolean renderArea = false;
    public boolean receivingRedstone = false;
    public boolean checkedRedstone = false;
    public boolean pulsed = false;
    public MiscHelpers.RedstoneMode redstoneMode = MiscHelpers.RedstoneMode.IGNORED;
    public AABB area;

    public AreaAffectingData() {

    }
}
