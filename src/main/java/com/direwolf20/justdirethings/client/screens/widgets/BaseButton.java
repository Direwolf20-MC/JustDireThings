package com.direwolf20.justdirethings.client.screens.widgets;

import com.direwolf20.justdirethings.util.MiscTools;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class BaseButton extends Button {
    protected Component localization = Component.empty();

    protected BaseButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress, CreateNarration pCreateNarration) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pCreateNarration);
    }

    public Component getLocalization() {
        return localization;
    }

    public Component getLocalization(int mouseX, int mouseY) {
        if (MiscTools.inBounds(getX(), getY(), getWidth(), getHeight(), mouseX, mouseY))
            return getLocalization();
        return Component.empty();
    }
}
