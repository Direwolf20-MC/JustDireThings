package com.direwolf20.justdirethings.common.items.interfaces;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public enum Ability {
    //Tier 1
    MOBSCANNER(SettingType.TOGGLE, 10, 500), //TODO Configs
    OREMINER(SettingType.TOGGLE, 1, 50),
    ORESCANNER(SettingType.TOGGLE, 10, 500),
    LAWNMOWER(SettingType.TOGGLE, 1, 50),
    SKYSWEEPER(SettingType.TOGGLE, 1, 50),
    TREEFELLER(SettingType.TOGGLE, 1, 50),
    LEAFBREAKER(SettingType.TOGGLE, 1, 50),
    //Tier 2
    SMELTER(SettingType.TOGGLE, 1, 50),
    HAMMER(SettingType.CYCLE, 1, 50),
    LAVAREPAIR(SettingType.TOGGLE, 0, 0),
    CAUTERIZEWOUNDS(SettingType.TOGGLE, 30, 1500),
    AIRBURST(SettingType.TOGGLE, 1, 500),
    //Tier 3
    DROPTELEPORT(SettingType.TOGGLE, 2, 100),
    //Tier 4
    OREXRAY(SettingType.TOGGLE, 100, 5000),
    GLOWING(SettingType.TOGGLE, 100, 5000),
    INSTABREAK(SettingType.TOGGLE, 2, 250);

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
    // Dynamic parameter map
    private static final Map<Ability, AbilityParams> dynamicParams = new EnumMap<>(Ability.class);

    Ability(SettingType settingType, int durabilityCost, int feCost) {
        this.name = this.name().toLowerCase(Locale.ROOT);
        this.settingType = settingType;
        this.localization = "justdirethings.ability." + name;
        this.iconLocation = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/" + name + ".png");
        this.durabilityCost = durabilityCost;
        this.feCost = feCost;
    }

    public boolean hasDynamicParams(Ability toolAbility) {
        return dynamicParams.containsKey(toolAbility);
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

    public int getDurabilityCost() {
        return durabilityCost;
    }

    public int getFeCost() {
        return feCost;
    }
}
