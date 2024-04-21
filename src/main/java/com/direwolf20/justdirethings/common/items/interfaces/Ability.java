package com.direwolf20.justdirethings.common.items.interfaces;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public enum Ability {
    //Tier 1
    MOBSCANNER(SettingType.TOGGLE, 10, 500, UseType.USE, //TODO Configs
            AbilityMethods::scanForMobScanner),
    OREMINER(SettingType.TOGGLE, 1, 50),
    ORESCANNER(SettingType.TOGGLE, 10, 500, UseType.USE,
            AbilityMethods::scanForOreScanner),
    LAWNMOWER(SettingType.TOGGLE, 1, 50, UseType.USE,
            AbilityMethods::lawnmower),
    SKYSWEEPER(SettingType.TOGGLE, 1, 50),
    TREEFELLER(SettingType.TOGGLE, 1, 50),
    LEAFBREAKER(SettingType.TOGGLE, 1, 50, UseType.USE_ON,
            AbilityMethods::leafbreaker),

    //Tier 2
    SMELTER(SettingType.TOGGLE, 1, 50),
    SMOKER(SettingType.TOGGLE, 1, 50),
    HAMMER(SettingType.CYCLE, 1, 50),
    LAVAREPAIR(SettingType.TOGGLE, 0, 0),
    CAUTERIZEWOUNDS(SettingType.TOGGLE, 30, 1500, UseType.USE,
            AbilityMethods::cauterizeWounds),
    AIRBURST(SettingType.SLIDER, 1, 250, UseType.USE,
            AbilityMethods::airBurst),

    //Tier 3
    DROPTELEPORT(SettingType.TOGGLE, 2, 100),
    VOIDSHIFT(SettingType.SLIDER, 1, 50, UseType.USE,
            AbilityMethods::voidShift), //FE Per block traveled

    //Tier 4
    OREXRAY(SettingType.TOGGLE, 100, 5000, UseType.USE,
            AbilityMethods::scanForOreXRAY),
    GLOWING(SettingType.TOGGLE, 100, 5000, UseType.USE,
            AbilityMethods::glowing),
    INSTABREAK(SettingType.TOGGLE, 2, 250),
    ECLIPSEGATE(SettingType.TOGGLE, 1, 250, UseType.USE_ON,
            AbilityMethods::eclipseGate); //FE Per block Removed

    public enum SettingType {
        TOGGLE,
        SLIDER,
        CYCLE
    }

    public enum UseType {
        USE,
        USE_ON
    }

    final String name;
    final String localization;
    final SettingType settingType;
    final ResourceLocation iconLocation;
    final int durabilityCost;
    final int feCost;
    UseType useType;
    // Dynamic parameter map
    private static final Map<Ability, AbilityParams> dynamicParams = new EnumMap<>(Ability.class);
    public AbilityAction action;  // Functional interface for action
    public UseOnAbilityAction useOnAction;  // Additional functional interface for use-on action


    Ability(SettingType settingType, int durabilityCost, int feCost) {
        this.name = this.name().toLowerCase(Locale.ROOT);
        this.settingType = settingType;
        this.localization = "justdirethings.ability." + name;
        this.iconLocation = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/" + name + ".png");
        this.durabilityCost = durabilityCost;
        this.feCost = feCost;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, AbilityAction action) {
        this(settingType, durabilityCost, feCost);
        this.action = action;
        this.useType = useType;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, UseOnAbilityAction useOnAction) {
        this(settingType, durabilityCost, feCost);
        this.useOnAction = useOnAction;
        this.useType = useType;
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

    @FunctionalInterface
    public interface AbilityAction {
        boolean execute(Level level, Player player, ItemStack itemStack);
    }

    @FunctionalInterface
    public interface UseOnAbilityAction {
        boolean execute(UseOnContext context);
    }
}
