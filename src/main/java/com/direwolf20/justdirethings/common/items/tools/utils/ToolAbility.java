package com.direwolf20.justdirethings.common.items.tools.utils;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.resources.ResourceLocation;

public enum ToolAbility {
    MOBSCANNER("mobscanner", SettingType.TOGGLE, 10, 100), //TODO Configs
    OREMINER("oreminer", SettingType.TOGGLE, 1, 0),
    ORESCANNER("orescanner", SettingType.TOGGLE, 10, 100),
    LAWNMOWER("lawnmower", SettingType.TOGGLE, 1, 10),
    SKYSWEEPER("skysweeper", SettingType.TOGGLE, 1, 0),
    TREEFELLER("treefeller", SettingType.TOGGLE, 1, 0),
    LEAFBREAKER("leafbreaker", SettingType.TOGGLE, 1, 10),
    SMELTER("smelter", SettingType.TOGGLE, 1, 10);

    public enum SettingType {
        TOGGLE,
        SLIDER
    }

    final String name;
    final String localization;
    final SettingType settingType;
    final ResourceLocation iconLocation;
    final int durabilityCost;
    final int feCost;
    int minSlider;
    int maxSlider;

    ToolAbility(String name, SettingType settingType, int durabilityCost, int feCost) {
        this(name, settingType, "justdirethings.ability." + name, name + ".png", durabilityCost, feCost);
    }

    ToolAbility(String name, SettingType settingType, String localization, String iconFileName, int durabilityCost, int feCost) {
        this.name = name;
        this.settingType = settingType;
        this.localization = localization;
        this.iconLocation = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/" + iconFileName);
        this.durabilityCost = durabilityCost;
        this.feCost = feCost;
    }

    ToolAbility(String name, SettingType settingType, int minSlider, int maxSlider, int durabilityCost, int feCost) {
        this(name, settingType, durabilityCost, feCost);
        this.minSlider = minSlider;
        this.maxSlider = maxSlider;
    }

    ToolAbility(String name, SettingType settingType, String localization, String iconFileName, int minSlider, int maxSlider, int durabilityCost, int feCost) {
        this(name, settingType, localization, iconFileName, durabilityCost, feCost);
        this.minSlider = minSlider;
        this.maxSlider = maxSlider;
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
}
