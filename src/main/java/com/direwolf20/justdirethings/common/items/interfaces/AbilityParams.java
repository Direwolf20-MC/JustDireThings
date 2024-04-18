package com.direwolf20.justdirethings.common.items.interfaces;

public class AbilityParams {
    public final int minSlider, maxSlider, increment;

    public AbilityParams(int minSlider, int maxSlider, int increment) {
        this.minSlider = minSlider;
        this.maxSlider = maxSlider;
        this.increment = increment;
    }
}
