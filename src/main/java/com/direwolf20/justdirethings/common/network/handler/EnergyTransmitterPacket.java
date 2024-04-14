package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.EnergyTransmitterBE;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.network.data.EnergyTransmitterSettingPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

public class EnergyTransmitterPacket {
    public static final EnergyTransmitterPacket INSTANCE = new EnergyTransmitterPacket();

    public static EnergyTransmitterPacket get() {
        return INSTANCE;
    }

    public void handle(final EnergyTransmitterSettingPayload payload, final PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            Optional<Player> senderOptional = context.player();
            if (senderOptional.isEmpty())
                return;
            Player sender = senderOptional.get();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof BaseMachineContainer baseMachineContainer && baseMachineContainer.baseMachineBE instanceof EnergyTransmitterBE energyTransmitterBE) {
                energyTransmitterBE.setEnergyTransmitterSettings(payload.showParticles());
            }
        });
    }
}
