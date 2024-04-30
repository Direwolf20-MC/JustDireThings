package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.BlockSwapperT1BE;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.network.data.SwapperPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SwapperPacket {
    public static final SwapperPacket INSTANCE = new SwapperPacket();

    public static SwapperPacket get() {
        return INSTANCE;
    }

    public void handle(final SwapperPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof BaseMachineContainer baseMachineContainer && baseMachineContainer.baseMachineBE instanceof BlockSwapperT1BE swapper) {
                swapper.setSwapperSettings(payload.swapBlocks(), payload.swap_entity_type());
            }
        });
    }
}
