package com.direwolf20.justdirethings.common.items.tools.utils;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public enum ToolAbility {
    MOBSCANNER(SettingType.TOGGLE, 10, 100), //TODO Configs
    OREMINER(SettingType.TOGGLE, 1, 0),
    ORESCANNER(SettingType.TOGGLE, 10, 100),
    LAWNMOWER(SettingType.TOGGLE, 1, 10),
    SKYSWEEPER(SettingType.TOGGLE, 1, 0),
    TREEFELLER(SettingType.TOGGLE, 1, 0),
    LEAFBREAKER(SettingType.TOGGLE, 1, 10),
    SMELTER(SettingType.TOGGLE, 1, 10),
    HAMMER3(SettingType.CYCLE, 1, 10, 3, 3, 2),
    HAMMER5(SettingType.CYCLE, 1, 10, 3, 5, 2);

    public enum SettingType {
        TOGGLE,
        SLIDER,
        CYCLE
    }

    final String name;
    final String localization;
    final SettingType settingType;
    final ResourceLocation iconLocation;
    final int durabilityCost;
    final int feCost;
    int minSlider;
    int maxSlider;
    int increment;

    ToolAbility(SettingType settingType, int durabilityCost, int feCost) {
        this.name = this.name().toLowerCase(Locale.ROOT);
        this.settingType = settingType;
        this.localization = "justdirethings.ability." + name;
        this.iconLocation = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/" + name + ".png");
        this.durabilityCost = durabilityCost;
        this.feCost = feCost;
    }

    ToolAbility(SettingType settingType, int durabilityCost, int feCost, int minSlider, int maxSlider) {
        this(settingType, durabilityCost, feCost);
        this.minSlider = minSlider;
        this.maxSlider = maxSlider;
    }

    ToolAbility(SettingType settingType, int durabilityCost, int feCost, int minSlider, int maxSlider, int increment) {
        this(settingType, durabilityCost, feCost);
        this.minSlider = minSlider;
        this.maxSlider = maxSlider;
        this.increment = increment;
    }

    public String getLocalization() {
        return localization;
    }

    public String getName() {
        return name;
    }

    public SettingType getSettingType() {
        return settingType;
    }

    public ResourceLocation getIconLocation() {
        return iconLocation;
    }

    public int getMinSlider() {
        return minSlider;
    }

    public int getMaxSlider() {
        return maxSlider;
    }

    public int getDurabilityCost() {
        return durabilityCost;
    }

    public int getFeCost() {
        return feCost;
    }

    public int getIncrement() {
        return increment;
    }
}
