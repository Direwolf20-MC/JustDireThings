package com.direwolf20.justdirethings.common.items.tools.utils;

public enum ToolAbility {
    MOBSCANNER("mobscanner", SettingType.TOGGLE),
    OREMINER("oreminer", SettingType.TOGGLE),
    ORESCANNER("orescanner", SettingType.TOGGLE);

    public enum SettingType {
        TOGGLE,
        SLIDER
    }

    String name;
    SettingType settingType;
    int minSlider;
    int maxSlider;

    ToolAbility(String name, SettingType settingType) {
        this.name = name;
        this.settingType = settingType;
    }

    ToolAbility(String name, SettingType settingType, int minSlider, int maxSlider) {
        this.name = name;
        this.settingType = settingType;
        this.minSlider = minSlider;
        this.maxSlider = maxSlider;
    }

}
