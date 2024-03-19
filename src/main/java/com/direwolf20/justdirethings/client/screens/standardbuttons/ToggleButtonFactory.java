package com.direwolf20.justdirethings.client.screens.standardbuttons;

import com.direwolf20.justdirethings.JustDireThings;
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
    private static List<TextureLocalization> REDSTONE_TEXTURES = List.of(
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/redstoneignore.png"), Component.translatable("justdirethings.screen.ignored")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/redstonelow.png"), Component.translatable("justdirethings.screen.low")),
            new TextureLocalization(new ResourceLocation(JustDireThings.MODID, "textures/gui/buttons/redstonehigh.png"), Component.translatable("justdirethings.screen.high"))

    );

    public static ToggleButton createStandardToggleButton(int x, int y, int startingValue, Button.OnPress onPress) {
        return new ToggleButton(x, y, STANDARD_WIDTH, STANDARD_HEIGHT, REDSTONE_TEXTURES, startingValue, onPress);
    }
}
