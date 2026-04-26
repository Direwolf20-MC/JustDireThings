package com.direwolf20.justdirethings.common.items.interfaces;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public enum Ability {
    //Tier 1
    MOBSCANNER(SettingType.TOGGLE, 10, 500, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::scanForMobScanner, CustomSettingType.NONE, JDTRegistration.UPGRADE_MOBSCANNER),
    OREMINER(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, JDTRegistration.UPGRADE_OREMINER),
    ORESCANNER(SettingType.TOGGLE, 10, 500, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::scanForOreScanner, CustomSettingType.NONE, JDTRegistration.UPGRADE_ORESCANNER),
    LAWNMOWER(SettingType.TOGGLE, 1, 50, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::lawnmower, CustomSettingType.NONE, JDTRegistration.UPGRADE_LAWNMOWER),
    SKYSWEEPER(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, JDTRegistration.UPGRADE_SKYSWEEPER),
    TREEFELLER(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, JDTRegistration.UPGRADE_TREEFELLER),
    LEAFBREAKER(SettingType.TOGGLE, 1, 50, UseType.USE_ON, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::leafbreaker, CustomSettingType.NONE, JDTRegistration.UPGRADE_LEAFBREAKER),
    RUNSPEED(SettingType.SLIDER, 1, 5, UseType.PASSIVE_TICK, BindingType.CUSTOM_ONLY,
            AbilityMethods::runSpeed, CustomSettingType.NONE, JDTRegistration.UPGRADE_RUNSPEED),
    WALKSPEED(SettingType.SLIDER, 1, 5, UseType.PASSIVE_TICK, BindingType.CUSTOM_ONLY,
            AbilityMethods::walkSpeed, CustomSettingType.NONE, JDTRegistration.UPGRADE_WALKSPEED),
    STEPHEIGHT(SettingType.TOGGLE, 1, 5, UseType.PASSIVE, BindingType.CUSTOM_ONLY, JDTRegistration.UPGRADE_STEPHEIGHT),
    JUMPBOOST(SettingType.SLIDER, 1, 5, UseType.PASSIVE, BindingType.CUSTOM_ONLY,
            AbilityMethods::jumpBoost, CustomSettingType.NONE, JDTRegistration.UPGRADE_JUMPBOOST),
    MINDFOG(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, JDTRegistration.UPGRADE_MINDFOG),
    INVULNERABILITY(SettingType.SLIDER, 25, 5000, UseType.USE_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::invulnerability, CustomSettingType.NONE,
            Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/invulnerability.png"), JDTRegistration.UPGRADE_INVULNERABILITY),
    POTIONARROW(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, JDTRegistration.UPGRADE_POTIONARROW),

    //Tier 2
    SMELTER(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, CustomSettingType.PARTICLES, JDTRegistration.UPGRADE_SMELTER),
    SMOKER(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, CustomSettingType.PARTICLES, JDTRegistration.UPGRADE_SMOKER),
    HAMMER(SettingType.CYCLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, JDTRegistration.UPGRADE_HAMMER),
    LAVAREPAIR(SettingType.TOGGLE, 0, 0, UseType.PASSIVE, BindingType.CUSTOM_ONLY),
    CAUTERIZEWOUNDS(SettingType.TOGGLE, 30, 1500, UseType.USE_COOLDOWN, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::cauterizeWounds, CustomSettingType.PARTICLES,
            Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/cauterizewounds.png"), JDTRegistration.UPGRADE_CAUTERIZEWOUNDS),
    AIRBURST(SettingType.SLIDER, 1, 250, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::airBurst, CustomSettingType.NONE),
    SWIMSPEED(SettingType.SLIDER, 1, 5, UseType.PASSIVE_TICK, BindingType.CUSTOM_ONLY,
            AbilityMethods::swimSpeed, CustomSettingType.NONE, JDTRegistration.UPGRADE_SWIMSPEED),
    GROUNDSTOMP(SettingType.SLIDER, 25, 5000, UseType.USE_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::groundstomp, CustomSettingType.PARTICLES,
            Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/groundstomp.png"), JDTRegistration.UPGRADE_GROUNDSTOMP),
    EXTINGUISH(SettingType.SLIDER, 25, 5000, UseType.PASSIVE_TICK_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::extinguish, CustomSettingType.PARTICLES,
            Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/extinguish.png"), JDTRegistration.UPGRADE_EXTINGUISH),
    STUPEFY(SettingType.SLIDER, 25, 5000, UseType.USE_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::stupefy, CustomSettingType.PARTICLES,
            Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/stupefy.png"), JDTRegistration.UPGRADE_STUPEFY),
    SPLASH(SettingType.TOGGLE, 20, 250, UseType.PASSIVE, BindingType.CUSTOM_ONLY, JDTRegistration.UPGRADE_SPLASH),
    POLYMORPH_RANDOM(SettingType.TOGGLE, 10, 1000, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::polymorphRandom, CustomSettingType.PARTICLES,
            Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/polymorph_random.png")),

    //Tier 3
    DROPTELEPORT(SettingType.TOGGLE, 2, 100, UseType.PASSIVE, BindingType.CUSTOM_ONLY, CustomSettingType.RENDER, JDTRegistration.UPGRADE_DROPTELEPORT),
    VOIDSHIFT(SettingType.SLIDER, 1, 50, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::voidShift, CustomSettingType.RENDER), //FE Per block traveled
    NEGATEFALLDAMAGE(SettingType.SLIDER, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, JDTRegistration.UPGRADE_NEGATEFALLDAMAGE),
    NIGHTVISION(SettingType.SLIDER, 1, 25, UseType.PASSIVE, BindingType.CUSTOM_ONLY, JDTRegistration.UPGRADE_NIGHTVISION),
    ELYTRA(SettingType.SLIDER, 1, 1000, UseType.PASSIVE, BindingType.CUSTOM_ONLY, JDTRegistration.UPGRADE_ELYTRA),
    DECOY(SettingType.SLIDER, 25, 5000, UseType.USE_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::decoy, CustomSettingType.NONE,
            Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/decoy.png"), JDTRegistration.UPGRADE_DECOY),
    LINGERING(SettingType.TOGGLE, 50, 1000, UseType.PASSIVE, BindingType.CUSTOM_ONLY, JDTRegistration.UPGRADE_LINGERING),
    HOMING(SettingType.TOGGLE, 50, 2000, UseType.PASSIVE, BindingType.CUSTOM_ONLY, CustomSettingType.TARGET, JDTRegistration.UPGRADE_HOMING),
    WATERBREATHING(SettingType.TOGGLE, 50, 500, UseType.PASSIVE_TICK, BindingType.CUSTOM_ONLY, AbilityMethods::waterBreathing, CustomSettingType.NONE, JDTRegistration.UPGRADE_WATERBREATHING),

    //Tier 4
    OREXRAY(SettingType.TOGGLE, 100, 5000, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::scanForOreXRAY, CustomSettingType.NONE, JDTRegistration.UPGRADE_OREXRAY),
    GLOWING(SettingType.TOGGLE, 100, 5000, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::glowing, CustomSettingType.NONE, JDTRegistration.UPGRADE_GLOWING),
    INSTABREAK(SettingType.TOGGLE, 2, 250, UseType.PASSIVE, BindingType.CUSTOM_ONLY, JDTRegistration.UPGRADE_INSTABREAK),
    ECLIPSEGATE(SettingType.SLIDER, 1, 250, UseType.USE_ON, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::eclipseGate, CustomSettingType.NONE), //FE Per block Removed
    DEATHPROTECTION(SettingType.SLIDER, 25, 450000, UseType.PASSIVE_COOLDOWN, BindingType.CUSTOM_ONLY,
            CustomSettingType.NONE,
            Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/deathprotection.png"), JDTRegistration.UPGRADE_DEATHPROTECTION),
    DEBUFFREMOVER(SettingType.SLIDER, 25, 50000, UseType.USE_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::debuffRemover, CustomSettingType.NONE,
            Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/debuffremover.png"), JDTRegistration.UPGRADE_DEBUFFREMOVER),
    EARTHQUAKE(SettingType.SLIDER, 25, 50000, UseType.USE_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::earthquake, CustomSettingType.PARTICLES,
            Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/earthquake.png"), JDTRegistration.UPGRADE_EARTHQUAKE),
    NOAI(SettingType.SLIDER, 25, 100000, UseType.USE_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::noAI, CustomSettingType.PARTICLES,
            Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/noai.png"), JDTRegistration.UPGRADE_NOAI),
    FLIGHT(SettingType.SLIDER, 1, 100, UseType.PASSIVE_TICK, BindingType.CUSTOM_ONLY,
            AbilityMethods::flight, CustomSettingType.NONE, JDTRegistration.UPGRADE_FLIGHT),
    LAVAIMMUNITY(SettingType.SLIDER, 1, 1000, UseType.PASSIVE, BindingType.CUSTOM_ONLY, JDTRegistration.UPGRADE_LAVAIMMUNITY),
    PHASE(SettingType.SLIDER, 1, 50000, UseType.PASSIVE, BindingType.CUSTOM_ONLY, JDTRegistration.UPGRADE_PHASE),
    TIMEPROTECTION(SettingType.SLIDER, 1, 5000, UseType.PASSIVE, BindingType.CUSTOM_ONLY, JDTRegistration.UPGRADE_TIMEPROTECTION),
    POLYMORPH_TARGET(SettingType.TOGGLE, 10, 50000, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::polymorphTarget, CustomSettingType.PARTICLES,
            Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/polymorph_target.png")),
    EPICARROW(SettingType.SLIDER, 25, 100000, UseType.USE_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::epicArrow, CustomSettingType.NONE,
            Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/epicarrow.png"), JDTRegistration.UPGRADE_EPICARROW);


    public enum SettingType {
        TOGGLE,
        SLIDER,
        CYCLE
    }

    public enum CustomSettingType {
        NONE,
        RENDER,
        TARGET,
        PARTICLES
    }

    public enum UseType {
        USE,
        USE_ON,
        USE_COOLDOWN,
        PASSIVE,
        PASSIVE_TICK,
        PASSIVE_COOLDOWN,
        PASSIVE_TICK_COOLDOWN
    }

    public enum BindingType {
        NONE,
        CUSTOM_ONLY,
        LEFT_AND_CUSTOM
    }

    final String name;
    final String localization;
    final SettingType settingType;
    final Identifier iconLocation;
    final int durabilityCost;
    final int feCost;
    final BindingType bindingType;
    final CustomSettingType customSettingType;
    final UseType useType;
    private Holder<Item> upgradeItem;
    // Dynamic parameter map
    private static final Map<Ability, AbilityParams> dynamicParams = new EnumMap<>(Ability.class);
    public AbilityAction action;  // Functional interface for action
    public UseOnAbilityAction useOnAction;  // Additional functional interface for use-on action
    private Identifier cooldownIcon;


    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, CustomSettingType customSettingType) {
        this.name = this.name().toLowerCase(Locale.ROOT);
        this.settingType = settingType;
        this.localization = "justdirethings.ability." + name;
        this.iconLocation = Identifier.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/buttons/" + name + ".png");
        this.durabilityCost = durabilityCost;
        this.feCost = feCost;
        this.bindingType = bindingType;
        this.customSettingType = customSettingType;
        this.useType = useType;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType) {
        this(settingType, durabilityCost, feCost, useType, bindingType, CustomSettingType.NONE);
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, Holder<Item> upgradeItem) {
        this(settingType, durabilityCost, feCost, useType, bindingType, CustomSettingType.NONE);
        this.upgradeItem = upgradeItem;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, CustomSettingType customSettingType, Holder<Item> upgradeItem) {
        this(settingType, durabilityCost, feCost, useType, bindingType, customSettingType);
        this.upgradeItem = upgradeItem;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, AbilityAction action, CustomSettingType customSettingType) {
        this(settingType, durabilityCost, feCost, useType, bindingType, customSettingType);
        this.action = action;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, AbilityAction action, CustomSettingType customSettingType, Holder<Item> upgradeItem) {
        this(settingType, durabilityCost, feCost, useType, bindingType, customSettingType);
        this.action = action;
        this.upgradeItem = upgradeItem;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, CustomSettingType customSettingType, Identifier cooldownIcon) {
        this(settingType, durabilityCost, feCost, useType, bindingType, customSettingType);
        this.cooldownIcon = cooldownIcon;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, CustomSettingType customSettingType, Identifier cooldownIcon, Holder<Item> upgradeItem) {
        this(settingType, durabilityCost, feCost, useType, bindingType, customSettingType);
        this.cooldownIcon = cooldownIcon;
        this.upgradeItem = upgradeItem;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, AbilityAction action, CustomSettingType customSettingType, Identifier cooldownIcon) {
        this(settingType, durabilityCost, feCost, useType, bindingType, customSettingType);
        this.action = action;
        this.cooldownIcon = cooldownIcon;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, AbilityAction action, CustomSettingType customSettingType, Identifier cooldownIcon, Holder<Item> upgradeItem) {
        this(settingType, durabilityCost, feCost, useType, bindingType, customSettingType);
        this.action = action;
        this.cooldownIcon = cooldownIcon;
        this.upgradeItem = upgradeItem;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, UseOnAbilityAction useOnAction, CustomSettingType customSettingType) {
        this(settingType, durabilityCost, feCost, useType, bindingType, customSettingType);
        this.useOnAction = useOnAction;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, UseOnAbilityAction useOnAction, CustomSettingType customSettingType, Holder<Item> upgradeItem) {
        this(settingType, durabilityCost, feCost, useType, bindingType, customSettingType);
        this.useOnAction = useOnAction;
        this.upgradeItem = upgradeItem;
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

    public Identifier getIconLocation() {
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

    public boolean hasCustomSetting() {
        return customSettingType != CustomSettingType.NONE;
    }

    public CustomSettingType getCustomSetting() {
        return customSettingType;
    }

    public static Ability byName(String name) {
        return Ability.valueOf(name.toUpperCase(Locale.ROOT));
    }

    public Identifier getCooldownIcon() {
        return cooldownIcon;
    }

    public boolean requiresUpgrade() {
        return upgradeItem != null;
    }

    public Holder<Item> getUpgradeItem() {
        return upgradeItem;
    }

    public static Ability getAbilityFromUpgradeItem(Item item) {
        for (Ability ability : values()) {
            if (ability.getUpgradeItem() != null && ability.getUpgradeItem().value() == item) {
                return ability;
            }
        }
        return null;
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
