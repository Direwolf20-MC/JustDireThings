package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.ExperienceHolderBE;
import com.direwolf20.justdirethings.common.containers.ExperienceHolderContainer;
import com.direwolf20.justdirethings.common.network.data.ExperienceHolderPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ExperienceHolderPacket {
    public static final ExperienceHolderPacket INSTANCE = new ExperienceHolderPacket();

    public static ExperienceHolderPacket get() {
        return INSTANCE;
    }

    public void handle(final ExperienceHolderPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof ExperienceHolderContainer experienceHolderContainer && experienceHolderContainer.baseMachineBE instanceof ExperienceHolderBE experienceHolderBE) {
                if (payload.add())
                    experienceHolderBE.storeExp(sender, payload.levels());
                else
                    experienceHolderBE.extractExp(sender, payload.levels());
            }
        });
    }
}
