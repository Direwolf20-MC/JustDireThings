package com.direwolf20.justdirethings.client.screens;

import com.direwolf20.justdirethings.client.screens.basescreens.BaseMachineScreen;
import com.direwolf20.justdirethings.client.screens.standardbuttons.ToggleButtonFactory;
import com.direwolf20.justdirethings.client.screens.widgets.GrayscaleButton;
import com.direwolf20.justdirethings.common.blockentities.ItemCollectorBE;
import com.direwolf20.justdirethings.common.containers.ItemCollectorContainer;
import com.direwolf20.justdirethings.common.network.data.ItemCollectorSettingsPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class ItemCollectorScreen extends BaseMachineScreen<ItemCollectorContainer> {
    public boolean respectPickupDelay = false;
    public ItemCollectorScreen(ItemCollectorContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        if (container.baseMachineBE instanceof ItemCollectorBE itemCollectorBE) {
            respectPickupDelay = itemCollectorBE.respectPickupDelay;
        }
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(ToggleButtonFactory.RESPECTPICKUPDELAY(getGuiLeft() + 116, topSectionTop + 62, respectPickupDelay, b -> {
            respectPickupDelay = !respectPickupDelay;
            ((GrayscaleButton) b).toggleActive();
            saveSettings();
        }));
    }

    @Override
    public void saveSettings() {
        super.saveSettings();
        PacketDistributor.sendToServer(new ItemCollectorSettingsPayload(respectPickupDelay));
    }
}
