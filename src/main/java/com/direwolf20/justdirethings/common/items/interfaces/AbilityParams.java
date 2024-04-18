package com.direwolf20.justdirethings.common.items.interfaces;

public class AbilityParams {
    public final int minSlider, maxSlider, increment, defaultValue;

    public AbilityParams(int minSlider, int maxSlider, int increment) {
        this.minSlider = minSlider;
        this.maxSlider = maxSlider;
        this.increment = increment;
        this.defaultValue = maxSlider; //By default, new tools have their max ability enabled, like hammer on celestigem starts out with 5.
    }

    public AbilityParams(int minSlider, int maxSlider, int increment, int defaultValue) {
        this.minSlider = minSlider;
        this.maxSlider = maxSlider;
        this.increment = increment;
        this.defaultValue = defaultValue;
    }
}
