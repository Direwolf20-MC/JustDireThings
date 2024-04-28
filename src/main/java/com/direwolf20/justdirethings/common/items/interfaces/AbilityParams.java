package com.direwolf20.justdirethings.common.items.interfaces;

public class AbilityParams {
    public final int minSlider, maxSlider, increment, defaultValue;
    public int activeCooldown = -1, cooldown = -1;

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

    public AbilityParams(int minSlider, int maxSlider, int increment, int defaultValue, int activeCooldown, int cooldown) {
        this.minSlider = minSlider;
        this.maxSlider = maxSlider;
        this.increment = increment;
        this.defaultValue = defaultValue;
        this.activeCooldown = activeCooldown;
        this.cooldown = cooldown;
    }
}
