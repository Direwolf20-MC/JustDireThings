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
    MOBSCANNER(SettingType.TOGGLE, 10, 500, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::scanForMobScanner, false),
    OREMINER(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY),
    ORESCANNER(SettingType.TOGGLE, 10, 500, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::scanForOreScanner, false),
    LAWNMOWER(SettingType.TOGGLE, 1, 50, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::lawnmower, false),
    SKYSWEEPER(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY),
    TREEFELLER(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY),
    LEAFBREAKER(SettingType.TOGGLE, 1, 50, UseType.USE_ON, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::leafbreaker, false),
    RUNSPEED(SettingType.SLIDER, 1, 5, UseType.PASSIVE_TICK, BindingType.CUSTOM_ONLY,
            AbilityMethods::runSpeed, false),
    WALKSPEED(SettingType.SLIDER, 1, 5, UseType.PASSIVE_TICK, BindingType.CUSTOM_ONLY,
            AbilityMethods::walkSpeed, false),
    STEPHEIGHT(SettingType.TOGGLE, 1, 5, UseType.PASSIVE, BindingType.CUSTOM_ONLY),
    JUMPBOOST(SettingType.SLIDER, 1, 5, UseType.PASSIVE, BindingType.CUSTOM_ONLY,
            AbilityMethods::jumpBoost, false),
    MINDFOG(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY),
    INVULNERABILITY(SettingType.SLIDER, 25, 5000, UseType.USE_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::invulnerability, false,
            ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/invulnerability.png")),

    //Tier 2
    SMELTER(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY),
    SMOKER(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY),
    HAMMER(SettingType.CYCLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY),
    LAVAREPAIR(SettingType.TOGGLE, 0, 0, UseType.PASSIVE, BindingType.CUSTOM_ONLY),
    CAUTERIZEWOUNDS(SettingType.TOGGLE, 30, 1500, UseType.USE_COOLDOWN, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::cauterizeWounds, false,
            ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/cauterizewounds.png")),
    AIRBURST(SettingType.SLIDER, 1, 250, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::airBurst, false),
    SWIMSPEED(SettingType.SLIDER, 1, 5, UseType.PASSIVE_TICK, BindingType.CUSTOM_ONLY,
            AbilityMethods::swimSpeed, false),
    GROUNDSTOMP(SettingType.SLIDER, 25, 5000, UseType.USE_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::groundstomp, false,
            ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/groundstomp.png")),

    //Tier 3
    DROPTELEPORT(SettingType.TOGGLE, 2, 100, UseType.PASSIVE, BindingType.CUSTOM_ONLY),
    VOIDSHIFT(SettingType.SLIDER, 1, 50, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::voidShift, true), //FE Per block traveled

    //Tier 4
    OREXRAY(SettingType.TOGGLE, 100, 5000, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::scanForOreXRAY, false),
    GLOWING(SettingType.TOGGLE, 100, 5000, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::glowing, false),
    INSTABREAK(SettingType.TOGGLE, 2, 250, UseType.PASSIVE, BindingType.CUSTOM_ONLY),
    ECLIPSEGATE(SettingType.TOGGLE, 1, 250, UseType.USE_ON, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::eclipseGate, false); //FE Per block Removed

    public enum SettingType {
        TOGGLE,
        SLIDER,
        CYCLE
    }

    public enum UseType {
        USE,
        USE_ON,
        USE_COOLDOWN,
        PASSIVE,
        PASSIVE_TICK
    }

    public enum BindingType {
        NONE,
        CUSTOM_ONLY,
        LEFT_AND_CUSTOM
    }

    final String name;
    final String localization;
    final SettingType settingType;
    final ResourceLocation iconLocation;
    final int durabilityCost;
    final int feCost;
    final BindingType bindingType;
    final boolean renderButton;
    final UseType useType;
    // Dynamic parameter map
    private static final Map<Ability, AbilityParams> dynamicParams = new EnumMap<>(Ability.class);
    public AbilityAction action;  // Functional interface for action
    public UseOnAbilityAction useOnAction;  // Additional functional interface for use-on action
    private ResourceLocation cooldownIcon;


    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, boolean renderButton) {
        this.name = this.name().toLowerCase(Locale.ROOT);
        this.settingType = settingType;
        this.localization = "justdirethings.ability." + name;
        this.iconLocation = ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/buttons/" + name + ".png");
        this.durabilityCost = durabilityCost;
        this.feCost = feCost;
        this.bindingType = bindingType;
        this.renderButton = renderButton;
        this.useType = useType;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType) {
        this(settingType, durabilityCost, feCost, useType, bindingType, false);
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, AbilityAction action, boolean renderButton) {
        this(settingType, durabilityCost, feCost, useType, bindingType, renderButton);
        this.action = action;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, AbilityAction action, boolean renderButton, ResourceLocation cooldownIcon) {
        this(settingType, durabilityCost, feCost, useType, bindingType, renderButton);
        this.action = action;
        this.cooldownIcon = cooldownIcon;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, UseOnAbilityAction useOnAction, boolean renderButton) {
        this(settingType, durabilityCost, feCost, useType, bindingType, renderButton);
        this.useOnAction = useOnAction;
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

    public boolean isBindable() {
        return bindingType != BindingType.NONE;
    }

    public BindingType getBindingType() {
        return bindingType;
    }

    public boolean hasRenderButton() {
        return renderButton;
    }

    public static Ability byName(String name) {
        return Ability.valueOf(name.toUpperCase(Locale.ROOT));
    }

    public ResourceLocation getCooldownIcon() {
        return cooldownIcon;
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
