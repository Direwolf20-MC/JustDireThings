package com.direwolf20.justdirethings.common.items.tools.utils;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.resources.ResourceLocation;

public enum ToolAbility {
    MOBSCANNER("mobscanner", SettingType.TOGGLE, "justdirethings.ability.mobscanner", "mobscanner.png"),
    OREMINER("oreminer", SettingType.TOGGLE, "justdirethings.ability.oreminer", "mobscanner.png"),
    ORESCANNER("orescanner", SettingType.TOGGLE, "justdirethings.ability.orescanner", "mobscanner.png");

    public enum SettingType {
        TOGGLE,
        SLIDER
    }

    final String name;
    final String localization;
    final SettingType settingType;
    final ResourceLocation iconLocation;
    int minSlider;
    int maxSlider;

    ToolAbility(String name, SettingType settingType, String localization, String iconFileName) {
        this.name = name;
        this.settingType = settingType;
        this.localization = localization;
        this.iconLocation = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/" + iconFileName);
    }

    ToolAbility(String name, SettingType settingType, String localization, String iconFileName, int minSlider, int maxSlider) {
        this(name, settingType, localization, iconFileName);
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
}
