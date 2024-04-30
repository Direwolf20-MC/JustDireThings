package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.ClickerT1BE;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.network.data.ClickerPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClickerPacket {
    public static final ClickerPacket INSTANCE = new ClickerPacket();

    public static ClickerPacket get() {
        return INSTANCE;
    }

    public void handle(final ClickerPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof BaseMachineContainer baseMachineContainer && baseMachineContainer.baseMachineBE instanceof ClickerT1BE clicker) {
                clicker.setClickerSettings(payload.clickType(), payload.clickTarget(), payload.sneaking(), payload.showFakePlayer(), payload.maxHoldTicks());
            }
        });
    }
}
