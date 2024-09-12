package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.common.blockentities.ExperienceHolderBE;
import com.direwolf20.justdirethings.common.containers.ExperienceHolderContainer;
import com.direwolf20.justdirethings.common.network.data.ExperienceHolderPayload;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class ExperienceHolderScreen extends BaseMachineScreen<ExperienceHolderContainer> {
    private ExperienceHolderBE experienceHolderBE;
    private int exp;
    public ExperienceHolderScreen(ExperienceHolderContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        if (container.baseMachineBE instanceof ExperienceHolderBE experienceHolderBE) {
            this.experienceHolderBE = experienceHolderBE;
            this.exp = experienceHolderBE.exp;
        }
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.STOREEXPBUTTON(getGuiLeft() + 62, topSectionTop + 62, true, b -> {
            int amt = 1;
            if (Screen.hasControlDown())
                amt = -1;
            else if (Screen.hasShiftDown())
                amt = amt * 10;
            PacketDistributor.sendToServer(new ExperienceHolderPayload(true, amt));
        }));
        addRenderableWidget(ToggleButtonFactory.EXTRACTEXPBUTTON(getGuiLeft() + 102, topSectionTop + 62, true, b -> {
            int amt = 1;
            if (Screen.hasControlDown())
                amt = -1;
            else if (Screen.hasShiftDown())
                amt = amt * 10;
            PacketDistributor.sendToServer(new ExperienceHolderPayload(false, amt));
        }));
    }
}
