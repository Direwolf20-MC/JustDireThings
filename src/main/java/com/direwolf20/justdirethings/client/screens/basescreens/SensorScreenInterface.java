package com.direwolf20.justdirethings.client.screens.basescreens;

import net.minecraft.client.gui.Font;
import net.minecraft.world.level.block.state.properties.Property;

public interface SensorScreenInterface {
    Comparable<?> getValue(Property<?> property);

    void setPropertyValue(Property<?> property, Comparable<?> comparable, boolean isAny);

    public Font getFontRenderer();
}
