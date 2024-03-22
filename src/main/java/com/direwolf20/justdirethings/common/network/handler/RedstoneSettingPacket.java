package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.basebe.RedstoneControlledBE;
import com.direwolf20.justdirethings.common.containers.basecontainers.AreaAffectingContainer;
import com.direwolf20.justdirethings.common.network.data.RedstoneSettingPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

public class RedstoneSettingPacket {
    public static final RedstoneSettingPacket INSTANCE = new RedstoneSettingPacket();

    public static RedstoneSettingPacket get() {
        return INSTANCE;
    }

    public void handle(final RedstoneSettingPayload payload, final PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            Optional<Player> senderOptional = context.player();
            if (senderOptional.isEmpty())
                return;
            Player sender = senderOptional.get();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof AreaAffectingContainer areaAffectingContainer) {
                if (areaAffectingContainer.areaAffectingBE instanceof RedstoneControlledBE redstoneControlledBE) {
                    redstoneControlledBE.setRedstoneSettings(payload.redstoneMode());
                }
            }
        });
    }
}
