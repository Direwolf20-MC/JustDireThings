package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.basebe.FilterableBE;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.network.data.FilterSettingPayload;
import com.direwolf20.justdirethings.util.interfacehelpers.FilterData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class FilterSettingPacket {
    public static final FilterSettingPacket INSTANCE = new FilterSettingPacket();

    public static FilterSettingPacket get() {
        return INSTANCE;
    }

    public void handle(final FilterSettingPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof BaseMachineContainer baseMachineContainer && baseMachineContainer.baseMachineBE instanceof FilterableBE filterableBE) {
                filterableBE.setFilterSettings(new FilterData(payload.allowList(), payload.compareNBT(), payload.blockItemFilter()));
            }
        });
    }
}
