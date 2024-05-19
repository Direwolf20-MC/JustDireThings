package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.items.MachineSettingsCopier;
import com.direwolf20.justdirethings.common.network.data.CopyMachineSettingsPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class CopyMachineSettingsPacket {
    public static final CopyMachineSettingsPacket INSTANCE = new CopyMachineSettingsPacket();

    public static CopyMachineSettingsPacket get() {
        return INSTANCE;
    }

    public void handle(final CopyMachineSettingsPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            ItemStack itemStack = sender.getMainHandItem();
            if (!(itemStack.getItem() instanceof MachineSettingsCopier))
                itemStack = sender.getOffhandItem();
            if (!(itemStack.getItem() instanceof MachineSettingsCopier))
                return;

            MachineSettingsCopier.setSettings(itemStack, payload.area(), payload.offset(), payload.filter(), payload.redstone());

        });
    }
}
