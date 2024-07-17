package com.direwolf20.justdirethings.common.items.interfaces;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
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
            AbilityMethods::scanForMobScanner, false, Registration.UPGRADE_MOBSCANNER),
    OREMINER(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_OREMINER),
    ORESCANNER(SettingType.TOGGLE, 10, 500, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::scanForOreScanner, false, Registration.UPGRADE_ORESCANNER),
    LAWNMOWER(SettingType.TOGGLE, 1, 50, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::lawnmower, false, Registration.UPGRADE_LAWNMOWER),
    SKYSWEEPER(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_SKYSWEEPER),
    TREEFELLER(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_TREEFELLER),
    LEAFBREAKER(SettingType.TOGGLE, 1, 50, UseType.USE_ON, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::leafbreaker, false, Registration.UPGRADE_LEAFBREAKER),
    RUNSPEED(SettingType.SLIDER, 1, 5, UseType.PASSIVE_TICK, BindingType.CUSTOM_ONLY,
            AbilityMethods::runSpeed, false, Registration.UPGRADE_RUNSPEED),
    WALKSPEED(SettingType.SLIDER, 1, 5, UseType.PASSIVE_TICK, BindingType.CUSTOM_ONLY,
            AbilityMethods::walkSpeed, false, Registration.UPGRADE_WALKSPEED),
    STEPHEIGHT(SettingType.TOGGLE, 1, 5, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_STEPHEIGHT),
    JUMPBOOST(SettingType.SLIDER, 1, 5, UseType.PASSIVE, BindingType.CUSTOM_ONLY,
            AbilityMethods::jumpBoost, false, Registration.UPGRADE_JUMPBOOST),
    MINDFOG(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_MINDFOG),
    INVULNERABILITY(SettingType.SLIDER, 25, 5000, UseType.USE_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::invulnerability, false,
            ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/invulnerability.png"), Registration.UPGRADE_INVULNERABILITY),
    POTIONARROW(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_POTIONARROW),

    //Tier 2
    SMELTER(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_SMELTER),
    SMOKER(SettingType.TOGGLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_SMOKER),
    HAMMER(SettingType.CYCLE, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_HAMMER),
    LAVAREPAIR(SettingType.TOGGLE, 0, 0, UseType.PASSIVE, BindingType.CUSTOM_ONLY),
    CAUTERIZEWOUNDS(SettingType.TOGGLE, 30, 1500, UseType.USE_COOLDOWN, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::cauterizeWounds, false,
            ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/cauterizewounds.png"), Registration.UPGRADE_CAUTERIZEWOUNDS),
    AIRBURST(SettingType.SLIDER, 1, 250, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::airBurst, false),
    SWIMSPEED(SettingType.SLIDER, 1, 5, UseType.PASSIVE_TICK, BindingType.CUSTOM_ONLY,
            AbilityMethods::swimSpeed, false, Registration.UPGRADE_SWIMSPEED),
    GROUNDSTOMP(SettingType.SLIDER, 25, 5000, UseType.USE_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::groundstomp, false,
            ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/groundstomp.png"), Registration.UPGRADE_GROUNDSTOMP),
    EXTINGUISH(SettingType.SLIDER, 25, 5000, UseType.PASSIVE_TICK_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::extinguish, false,
            ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/extinguish.png"), Registration.UPGRADE_EXTINGUISH),
    STUPEFY(SettingType.SLIDER, 25, 5000, UseType.USE_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::stupefy, false,
            ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/stupefy.png"), Registration.UPGRADE_STUPEFY),
    SPLASH(SettingType.TOGGLE, 20, 250, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_SPLASH),

    //Tier 3
    DROPTELEPORT(SettingType.TOGGLE, 2, 100, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_DROPTELEPORT),
    VOIDSHIFT(SettingType.SLIDER, 1, 50, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::voidShift, true), //FE Per block traveled
    NEGATEFALLDAMAGE(SettingType.SLIDER, 1, 50, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_NEGATEFALLDAMAGE),
    NIGHTVISION(SettingType.SLIDER, 1, 25, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_NIGHTVISION),
    ELYTRA(SettingType.SLIDER, 1, 1000, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_ELYTRA),
    DECOY(SettingType.SLIDER, 25, 5000, UseType.USE_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::decoy, false,
            ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/decoy.png"), Registration.UPGRADE_DECOY),
    LINGERING(SettingType.TOGGLE, 50, 1000, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_LINGERING),
    HOMING(SettingType.TOGGLE, 50, 2000, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_HOMING),


    //Tier 4
    OREXRAY(SettingType.TOGGLE, 100, 5000, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::scanForOreXRAY, false, Registration.UPGRADE_OREXRAY),
    GLOWING(SettingType.TOGGLE, 100, 5000, UseType.USE, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::glowing, false, Registration.UPGRADE_GLOWING),
    INSTABREAK(SettingType.TOGGLE, 2, 250, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_INSTABREAK),
    ECLIPSEGATE(SettingType.SLIDER, 1, 250, UseType.USE_ON, BindingType.LEFT_AND_CUSTOM,
            AbilityMethods::eclipseGate, false), //FE Per block Removed
    DEATHPROTECTION(SettingType.SLIDER, 25, 450000, UseType.PASSIVE_COOLDOWN, BindingType.CUSTOM_ONLY,
            false,
            ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/deathprotection.png"), Registration.UPGRADE_DEATHPROTECTION),
    DEBUFFREMOVER(SettingType.SLIDER, 25, 50000, UseType.USE_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::debuffRemover, false,
            ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/debuffremover.png"), Registration.UPGRADE_DEBUFFREMOVER),
    EARTHQUAKE(SettingType.SLIDER, 25, 50000, UseType.USE_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::earthquake, false,
            ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/earthquake.png"), Registration.UPGRADE_EARTHQUAKE),
    NOAI(SettingType.SLIDER, 25, 100000, UseType.USE_COOLDOWN, BindingType.CUSTOM_ONLY,
            AbilityMethods::noAI, false,
            ResourceLocation.fromNamespaceAndPath(JustDireThings.MODID, "textures/gui/overlay/noai.png"), Registration.UPGRADE_NOAI),
    FLIGHT(SettingType.SLIDER, 1, 100, UseType.PASSIVE_TICK, BindingType.CUSTOM_ONLY,
            AbilityMethods::flight, false, Registration.UPGRADE_FLIGHT),
    LAVAIMMUNITY(SettingType.SLIDER, 1, 1000, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_LAVAIMMUNITY),
    PHASE(SettingType.SLIDER, 1, 1000, UseType.PASSIVE, BindingType.CUSTOM_ONLY, Registration.UPGRADE_PHASE);


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
    final ResourceLocation iconLocation;
    final int durabilityCost;
    final int feCost;
    final BindingType bindingType;
    final boolean renderButton;
    final UseType useType;
    private Holder<Item> upgradeItem;
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

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, Holder<Item> upgradeItem) {
        this(settingType, durabilityCost, feCost, useType, bindingType, false);
        this.upgradeItem = upgradeItem;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, AbilityAction action, boolean renderButton) {
        this(settingType, durabilityCost, feCost, useType, bindingType, renderButton);
        this.action = action;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, AbilityAction action, boolean renderButton, Holder<Item> upgradeItem) {
        this(settingType, durabilityCost, feCost, useType, bindingType, renderButton);
        this.action = action;
        this.upgradeItem = upgradeItem;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, boolean renderButton, ResourceLocation cooldownIcon) {
        this(settingType, durabilityCost, feCost, useType, bindingType, renderButton);
        this.cooldownIcon = cooldownIcon;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, boolean renderButton, ResourceLocation cooldownIcon, Holder<Item> upgradeItem) {
        this(settingType, durabilityCost, feCost, useType, bindingType, renderButton);
        this.cooldownIcon = cooldownIcon;
        this.upgradeItem = upgradeItem;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, AbilityAction action, boolean renderButton, ResourceLocation cooldownIcon) {
        this(settingType, durabilityCost, feCost, useType, bindingType, renderButton);
        this.action = action;
        this.cooldownIcon = cooldownIcon;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, AbilityAction action, boolean renderButton, ResourceLocation cooldownIcon, Holder<Item> upgradeItem) {
        this(settingType, durabilityCost, feCost, useType, bindingType, renderButton);
        this.action = action;
        this.cooldownIcon = cooldownIcon;
        this.upgradeItem = upgradeItem;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, UseOnAbilityAction useOnAction, boolean renderButton) {
        this(settingType, durabilityCost, feCost, useType, bindingType, renderButton);
        this.useOnAction = useOnAction;
    }

    Ability(SettingType settingType, int durabilityCost, int feCost, UseType useType, BindingType bindingType, UseOnAbilityAction useOnAction, boolean renderButton, Holder<Item> upgradeItem) {
        this(settingType, durabilityCost, feCost, useType, bindingType, renderButton);
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
