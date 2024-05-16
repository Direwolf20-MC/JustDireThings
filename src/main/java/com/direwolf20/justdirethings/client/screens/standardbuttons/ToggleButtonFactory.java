package com.direwolf20.justdirethings.client.screens.standardbuttons;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.client.screens.widgets.NumberButton;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.setup.Config;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToggleButtonFactory {
    public record TextureLocalization(ResourceLocation texture, MutableComponent localization) {
    }

    private static final int STANDARD_WIDTH = 16; // Example width
    private static final int STANDARD_HEIGHT = 16; // Example height

    private static final Map<Ability, List<TextureLocalization>> abilityTextureMap = new HashMap<>() {{
        put(Ability.HAMMER, HAMMER_TEXTURES);
    }};

    /** Redstone Button **/
    private static final List<TextureLocalization> REDSTONE_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/redstoneignore.png"), Component.translatable("justdirethings.screen.ignored")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/redstonelow.png"), Component.translatable("justdirethings.screen.low")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/redstonehigh.png"), Component.translatable("justdirethings.screen.high")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/redstonepulse.png"), Component.translatable("justdirethings.screen.pulse"))
    );

    public static ToggleButton REDSTONEBUTTON(int x, int y, int startingValue, Button.OnPress onPress) {
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, REDSTONE_TEXTURES, startingValue, onPress);
    }

    /** Allow List Button **/
    private static final List<TextureLocalization> ALLOW_LIST_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/allowlistfalse.png"), Component.translatable("justdirethings.screen.denylist")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/allowlisttrue.png"), Component.translatable("justdirethings.screen.allowlist"))
    );

    public static ToggleButton ALLOWLISTBUTTON(int x, int y, boolean startingValue, Button.OnPress onPress) {
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, ALLOW_LIST_TEXTURES, startingValue, onPress);
    }

    private static final ResourceLocation COMPARE_NBT = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/matchnbttrue.png");
    private static final Component COMPARE_NBT_LOCALIZATION = Component.translatable("justdirethings.screen.comparenbt");

    public static GrayscaleButton COMPARENBTBUTTON(int x, int y, boolean startingValue, Button.OnPress onPress) {
        return new GrayscaleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, COMPARE_NBT, COMPARE_NBT_LOCALIZATION, startingValue, onPress);
    }

    private static final ResourceLocation RENDER_AREA = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/area.png");
    private static final Component RENDER_AREA_LOCALIZATION = Component.translatable("justdirethings.screen.renderarea");

    public static GrayscaleButton RENDERAREABUTTON(int x, int y, boolean startingValue, Button.OnPress onPress) {
        return new GrayscaleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, RENDER_AREA, RENDER_AREA_LOCALIZATION, startingValue, onPress);
    }

    /** Direction Button **/
    private static final List<TextureLocalization> DIRECTION_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/direction-down.png"), Component.translatable("justdirethings.screen.direction-down")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/direction-up.png"), Component.translatable("justdirethings.screen.direction-up")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/direction-north.png"), Component.translatable("justdirethings.screen.direction-north")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/direction-south.png"), Component.translatable("justdirethings.screen.direction-south")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/direction-west.png"), Component.translatable("justdirethings.screen.direction-west")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/direction-east.png"), Component.translatable("justdirethings.screen.direction-east"))
    );

    public static ToggleButton DIRECTIONBUTTON(int x, int y, int startingValue, Button.OnPress onPress) {
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, DIRECTION_TEXTURES, startingValue, onPress);
    }

    /** Direction With None Button **/
    private static final List<TextureLocalization> DIRECTION_NONE_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/direction-none.png"), Component.translatable("justdirethings.screen.direction-none")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/direction-down.png"), Component.translatable("justdirethings.screen.direction-down")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/direction-up.png"), Component.translatable("justdirethings.screen.direction-up")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/direction-north.png"), Component.translatable("justdirethings.screen.direction-north")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/direction-south.png"), Component.translatable("justdirethings.screen.direction-south")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/direction-west.png"), Component.translatable("justdirethings.screen.direction-west")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/direction-east.png"), Component.translatable("justdirethings.screen.direction-east"))
    );

    public static ToggleButton DIRECTIONNONEBUTTON(int x, int y, int startingValue, Button.OnPress onPress) {
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, DIRECTION_NONE_TEXTURES, startingValue, onPress);
    }

    /** FilterBlockItem Button **/
    private static final List<TextureLocalization> FILTERBLOCKITEM_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/filter-block.png"), Component.translatable("justdirethings.screen.filter-block")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/filter-item.png"), Component.translatable("justdirethings.screen.filter-item"))
    );

    public static ToggleButton FILTERBLOCKITEMBUTTON(int x, int y, int startingValue, Button.OnPress onPress) {
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, FILTERBLOCKITEM_TEXTURES, startingValue, onPress);
    }

    /** Hammer Button **/
    private static final List<TextureLocalization> HAMMER_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/ignore.png"), Component.translatable("justdirethings.ability.hammer_off")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/hammer3.png"), Component.translatable("justdirethings.ability.hammer_3")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/hammer5.png"), Component.translatable("justdirethings.ability.hammer_5")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/hammer7.png"), Component.translatable("justdirethings.ability.hammer_7"))
    );

    public static ToggleButton ABILITYCYCLEBUTTON(int x, int y, Ability ability, int startingValue, int maxValue, Button.OnPress onPress) {
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, HAMMER_TEXTURES, startingValue, maxValue, onPress);
    }

    private static Component ticksButtonLocalization = Component.translatable("justdirethings.screen.tickspeed");

    public static NumberButton TICKSPEEDBUTTON(int x, int y, int value, Button.OnPress onPress) {
        return new NumberButton(x, y, 24, 12, value, Config.MINIMUM_MACHINE_TICK_SPEED.get(), 1200, ticksButtonLocalization, onPress);
    }

    public static NumberButton TICKSPEEDBUTTON(int x, int y, int value, int min, Button.OnPress onPress) {
        return new NumberButton(x, y, 24, 12, value, min, 1200, ticksButtonLocalization, onPress);
    }

    /** LEFT or RIGHT Click Button **/
    private static final List<TextureLocalization> LEFT_RIGHT_CLICK_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/click-right.png"), Component.translatable("justdirethings.screen.click-right")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/click-left.png"), Component.translatable("justdirethings.screen.click-left")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/click-hold.png"), Component.translatable("justdirethings.screen.click-hold"))
    );

    public static ToggleButton LEFTRIGHTCLICKBUTTON(int x, int y, int startingValue, Button.OnPress onPress) {
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, LEFT_RIGHT_CLICK_TEXTURES, startingValue, onPress);
    }

    /** Click Target Button **/
    private static final List<TextureLocalization> CLICK_TARGET_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/filter-block.png"), Component.translatable("justdirethings.screen.target-block")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/filter-air.png"), Component.translatable("justdirethings.screen.target-air")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/mobscanner.png"), Component.translatable("justdirethings.screen.target-hostile")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/passivemob.png"), Component.translatable("justdirethings.screen.target-passive")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/passivemob-adult.png"), Component.translatable("justdirethings.screen.target-adult")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/passivemob-child.png"), Component.translatable("justdirethings.screen.target-child")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/player.png"), Component.translatable("justdirethings.screen.target-player")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/glowing.png"), Component.translatable("justdirethings.screen.target-living"))
    );

    public static ToggleButton CLICKTARGETBUTTON(int x, int y, int startingValue, Button.OnPress onPress) {
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, CLICK_TARGET_TEXTURES, startingValue, onPress);
    }

    /** Sneak Click button **/
    private static final ResourceLocation SNEAK_BUTTON = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/click-sneak.png");
    private static final Component SNEAK_LOCALIZATION = Component.translatable("justdirethings.screen.sneak-click");

    public static GrayscaleButton SNEAKCLICKBUTTON(int x, int y, boolean startingValue, Button.OnPress onPress) {
        return new GrayscaleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, SNEAK_BUTTON, SNEAK_LOCALIZATION, startingValue, onPress);
    }

    /** Show Fake Player button **/
    private static final ResourceLocation SHOWFAKEPLAYER_BUTTON = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/showfakeplayer.png");
    private static final Component SHOWFAKEPLAYER_LOCALIZATION = Component.translatable("justdirethings.screen.showfakeplayer");

    public static GrayscaleButton SHOWFAKEPLAYERBUTTON(int x, int y, boolean startingValue, Button.OnPress onPress) {
        return new GrayscaleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, SHOWFAKEPLAYER_BUTTON, SHOWFAKEPLAYER_LOCALIZATION, startingValue, onPress);
    }

    /** Sensor Target Button **/
    private static final List<TextureLocalization> SENSOR_TARGET_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/filter-block.png"), Component.translatable("justdirethings.screen.target-block")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/filter-air.png"), Component.translatable("justdirethings.screen.target-air")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/mobscanner.png"), Component.translatable("justdirethings.screen.target-hostile")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/passivemob.png"), Component.translatable("justdirethings.screen.target-passive")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/passivemob-adult.png"), Component.translatable("justdirethings.screen.target-adult")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/passivemob-child.png"), Component.translatable("justdirethings.screen.target-child")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/player.png"), Component.translatable("justdirethings.screen.target-player")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/glowing.png"), Component.translatable("justdirethings.screen.target-living")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/item.png"), Component.translatable("justdirethings.screen.target-item"))
    );

    public static ToggleButton SENSORTARGETBUTTON(int x, int y, int startingValue, Button.OnPress onPress) {
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, SENSOR_TARGET_TEXTURES, startingValue, onPress);
    }

    /** Strong or Weak Redstone **/
    private static final List<TextureLocalization> STRONG_WEAK_REDSTONE_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/redstonelow.png"), Component.translatable("justdirethings.screen.redstone-weak")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/redstonehigh.png"), Component.translatable("justdirethings.screen.redstone-strong"))
    );

    public static ToggleButton STRONGWEAKREDSTONEBUTTON(int x, int y, int startingValue, Button.OnPress onPress) {
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, STRONG_WEAK_REDSTONE_TEXTURES, startingValue, onPress);
    }

    /** Equality **/
    private static final List<TextureLocalization> EQUALITY_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/greaterthan.png"), Component.translatable("justdirethings.screen.greaterthan")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/lessthan.png"), Component.translatable("justdirethings.screen.lessthan")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/equals.png"), Component.translatable("justdirethings.screen.equals"))
    );

    public static ToggleButton EQUALSBUTTON(int x, int y, int startingValue, Button.OnPress onPress) {
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, EQUALITY_TEXTURES, startingValue, onPress);
    }

    /** Show Particles button **/
    private static final ResourceLocation SHOW_PARTICLES_BUTTON = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/showfakeplayer.png");
    private static final Component SHOW_PARTICLES_LOCALIZATION = Component.translatable("justdirethings.screen.showparticles");

    public static GrayscaleButton SHOWPARTICLESBUTTON(int x, int y, boolean startingValue, Button.OnPress onPress) {
        return new GrayscaleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, SHOW_PARTICLES_BUTTON, SHOW_PARTICLES_LOCALIZATION, startingValue, onPress);
    }

    /** Swapper Target Button **/
    private static final List<TextureLocalization> SWAPPER_ENTITY_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/entity-none.png"), Component.translatable("justdirethings.screen.entity-none")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/mobscanner.png"), Component.translatable("justdirethings.screen.target-hostile")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/passivemob.png"), Component.translatable("justdirethings.screen.target-passive")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/passivemob-adult.png"), Component.translatable("justdirethings.screen.target-adult")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/passivemob-child.png"), Component.translatable("justdirethings.screen.target-child")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/player.png"), Component.translatable("justdirethings.screen.target-player")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/glowing.png"), Component.translatable("justdirethings.screen.target-living")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/item.png"), Component.translatable("justdirethings.screen.target-item")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/entity-all.png"), Component.translatable("justdirethings.screen.entity-all"))
    );

    public static ToggleButton SWAPPERENTITYBUTTON(int x, int y, int startingValue, Button.OnPress onPress) {
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, SWAPPER_ENTITY_TEXTURES, startingValue, onPress);
    }

    /** Swapper Block Button **/
    private static final List<TextureLocalization> SWAPPER_BLOCK_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/filter-block.png"), Component.translatable("justdirethings.screen.target-block")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/filter-air.png"), Component.translatable("justdirethings.screen.target-noblock"))
    );

    public static ToggleButton SWAPPERBLOCKBUTTON(int x, int y, int startingValue, Button.OnPress onPress) {
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, SWAPPER_BLOCK_TEXTURES, startingValue, onPress);
    }

    /** Inventory Connection Button **/
    private static final List<TextureLocalization> INVENTORY_CONNECTION_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/inv-normal.png"), Component.translatable("justdirethings.screen.inv-normal")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/inv-armor.png"), Component.translatable("justdirethings.screen.inv-armor")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/inv-offhand.png"), Component.translatable("justdirethings.screen.inv-offhand"))
    );

    public static ToggleButton INVENTORYCONNECTIONBUTTON(int x, int y, MutableComponent component, int startingValue, Button.OnPress onPress) {
        List<TextureLocalization> textureLocalizations = new ArrayList<>();
        for (TextureLocalization textureLocalization : INVENTORY_CONNECTION_TEXTURES) {
            textureLocalizations.add(new TextureLocalization(textureLocalization.texture, Component.literal("").append(component).append(Component.literal(": ")).append(textureLocalization.localization)));
        }
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, textureLocalizations, startingValue, onPress);
    }

    private static final List<TextureLocalization> LEFT_RIGHT_ONLY_CLICK_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/click-right.png"), Component.translatable("justdirethings.screen.click-right")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/click-left.png"), Component.translatable("justdirethings.screen.click-left")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/click-sneak.png"), Component.translatable("justdirethings.screen.click-custom"))
    );

    public static ToggleButton LEFTRIGHTCUSTOMCLICKBUTTON(int x, int y, int startingValue, Button.OnPress onPress) {
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, LEFT_RIGHT_ONLY_CLICK_TEXTURES, startingValue, onPress);
    }

    private static final List<TextureLocalization> CUSTOM_ONLY_CLICK_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/click-sneak.png"), Component.translatable("justdirethings.screen.click-custom"))
    );

    public static ToggleButton CUSTOMCLICKBUTTON(int x, int y, int startingValue, Button.OnPress onPress) {
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, CUSTOM_ONLY_CLICK_TEXTURES, startingValue, onPress);
    }

    /** Bind Hotkey button **/
    private static final ResourceLocation BIND_BUTTON = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/click-hold.png");
    private static final Component BIND_LOCALIZATION = Component.translatable("justdirethings.screen.setbinding");

    public static GrayscaleButton KEYBIND_BUTTON(int x, int y, boolean startingValue, Button.OnPress onPress) {
        return new GrayscaleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, BIND_BUTTON, BIND_LOCALIZATION, startingValue, onPress);
    }

    /** Hide Render button **/
    private static final ResourceLocation HIDE_RENDER_BUTTON = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/showfakeplayer.png");
    private static final Component HIDE_RENDER_LOCALIZATION = Component.translatable("justdirethings.screen.showrender");

    public static GrayscaleButton HIDE_RENDER_ABILITY_BUTTON(int x, int y, boolean startingValue, Button.OnPress onPress) {
        return new GrayscaleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, HIDE_RENDER_BUTTON, HIDE_RENDER_LOCALIZATION, startingValue, onPress);
    }

    private static final ResourceLocation COPY_AREA_BUTTON = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/area.png");
    private static final Component COPY_AREA_LOCALIZATION = Component.translatable("justdirethings.screen.copy_area");

    public static GrayscaleButton COPY_AREA_BUTTON(int x, int y, boolean startingValue, Button.OnPress onPress) {
        return new GrayscaleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, COPY_AREA_BUTTON, COPY_AREA_LOCALIZATION, startingValue, onPress);
    }

    private static final ResourceLocation COPY_OFFSET_BUTTON = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/stepheight.png");
    private static final Component COPY_OFFSET_LOCALIZATION = Component.translatable("justdirethings.screen.copy_offset");

    public static GrayscaleButton COPY_OFFSET_BUTTON(int x, int y, boolean startingValue, Button.OnPress onPress) {
        return new GrayscaleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, COPY_OFFSET_BUTTON, COPY_OFFSET_LOCALIZATION, startingValue, onPress);
    }

    private static final ResourceLocation COPY_FILTER_BUTTON = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/matchnbttrue.png");
    private static final Component COPY_FILTER_LOCALIZATION = Component.translatable("justdirethings.screen.copy_filter");

    public static GrayscaleButton COPY_FILTER_BUTTON(int x, int y, boolean startingValue, Button.OnPress onPress) {
        return new GrayscaleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, COPY_FILTER_BUTTON, COPY_FILTER_LOCALIZATION, startingValue, onPress);
    }

    private static final ResourceLocation COPY_REDSTONE_BUTTON = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/redstonepulse.png");
    private static final Component COPY_REDSTONE_LOCALIZATION = Component.translatable("justdirethings.screen.copy_redstone");

    public static GrayscaleButton COPY_REDSTONE_BUTTON(int x, int y, boolean startingValue, Button.OnPress onPress) {
        return new GrayscaleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, COPY_REDSTONE_BUTTON, COPY_REDSTONE_LOCALIZATION, startingValue, onPress);
    }

}
