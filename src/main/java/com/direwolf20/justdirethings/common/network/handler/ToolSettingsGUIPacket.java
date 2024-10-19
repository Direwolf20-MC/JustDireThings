package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.containers.ToolSettingContainer;
import com.direwolf20.justdirethings.common.network.data.ToolSettingsGUIPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ToolSettingsGUIPacket {
    public static final ToolSettingsGUIPacket INSTANCE = new ToolSettingsGUIPacket();

    public static ToolSettingsGUIPacket get() {
        return INSTANCE;
    }

    public void handle(final ToolSettingsGUIPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            sender.openMenu(new SimpleMenuProvider(
                    (windowId, playerInventory, playerEntity) -> new ToolSettingContainer(windowId, playerInventory, sender), Component.translatable("")));

        });
    }
}
