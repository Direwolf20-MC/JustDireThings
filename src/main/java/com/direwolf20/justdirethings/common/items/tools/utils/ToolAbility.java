package com.direwolf20.justdirethings.common.items.tools.utils;

public enum ToolAbility {
    MOBSCANNER("mobscanner", SettingType.TOGGLE, "justdirethings.ability.mobscanner"),
    OREMINER("oreminer", SettingType.TOGGLE, "justdirethings.ability.oreminer"),
    ORESCANNER("orescanner", SettingType.TOGGLE, "justdirethings.ability.orescanner");

    public enum SettingType {
        TOGGLE,
        SLIDER
    }

    final String name;
    final String localization;
    final SettingType settingType;
    int minSlider;
    int maxSlider;

    ToolAbility(String name, SettingType settingType, String localization) {
        this.name = name;
        this.settingType = settingType;
        this.localization = localization;
    }

    ToolAbility(String name, SettingType settingType, String localization, int minSlider, int maxSlider) {
        this(name, settingType, localization);
        this.minSlider = minSlider;
        this.maxSlider = maxSlider;
    }

    public String getLocalization() {
        return localization;
    }
}
