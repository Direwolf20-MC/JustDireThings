package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.ParadoxMachineBE;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.network.data.ParadoxRenderPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ParadoxRenderPacket {
    public static final ParadoxRenderPacket INSTANCE = new ParadoxRenderPacket();

    public static ParadoxRenderPacket get() {
        return INSTANCE;
    }

    public void handle(final ParadoxRenderPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof BaseMachineContainer baseMachineContainer && baseMachineContainer.baseMachineBE instanceof ParadoxMachineBE paradoxMachineBE) {
                paradoxMachineBE.setRenderParadox(payload.renderParadox());
            }
        });
    }
}
