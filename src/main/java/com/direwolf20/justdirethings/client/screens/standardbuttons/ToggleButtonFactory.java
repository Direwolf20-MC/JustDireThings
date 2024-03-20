package com.direwolf20.justdirethings.client.screens.standardbuttons;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.client.screens.widgets.ToggleButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ToggleButtonFactory {
    public record TextureLocalization(ResourceLocation texture, Component localization) {
    }

    private static final int STANDARD_WIDTH = 16; // Example width
    private static final int STANDARD_HEIGHT = 16; // Example height

    /** Redstone Button **/
    private static final List<TextureLocalization> REDSTONE_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/redstoneignore.png"), Component.translatable("justdirethings.screen.ignored")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/redstonelow.png"), Component.translatable("justdirethings.screen.low")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/redstonehigh.png"), Component.translatable("justdirethings.screen.high"))

    );

    public static ToggleButton REDSTONEBUTTON(int x, int y, int startingValue, Button.OnPress onPress) {
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, REDSTONE_TEXTURES, startingValue, onPress);
    }

    private static final ResourceLocation ALLOW_LIST = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/allowlisttrue.png");
    private static final Component ALLOW_LIST_LOCALIZATION = Component.translatable("justdirethings.screen.allowlist");
    private static final Component DENY_LIST_LOCALIZATION = Component.translatable("justdirethings.screen.denylist");

    public static GrayscaleButton ALLOWLISTBUTTON(int x, int y, boolean startingValue, Button.OnPress onPress) {
        return new GrayscaleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, ALLOW_LIST, ALLOW_LIST_LOCALIZATION, DENY_LIST_LOCALIZATION, startingValue, onPress);
    }

    private static final ResourceLocation COMPARE_NBT = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/matchnbttrue.png");
    private static final Component COMPARE_NBT_LOCALIZATION = Component.translatable("justdirethings.screen.comparenbt");

    public static GrayscaleButton COMPARENBTBUTTON(int x, int y, boolean startingValue, Button.OnPress onPress) {
        return new GrayscaleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, COMPARE_NBT, COMPARE_NBT_LOCALIZATION, startingValue, onPress);
    }

    private static final ResourceLocation RENDER_AREA = new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/mobscanner.png");
    private static final Component RENDER_AREA_LOCALIZATION = Component.translatable("justdirethings.screen.renderarea");

    public static GrayscaleButton RENDERAREABUTTON(int x, int y, boolean startingValue, Button.OnPress onPress) {
        return new GrayscaleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, RENDER_AREA, RENDER_AREA_LOCALIZATION, startingValue, onPress);
    }
}
