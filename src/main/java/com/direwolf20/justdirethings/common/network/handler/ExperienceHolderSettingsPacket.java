package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.ExperienceHolderBE;
import com.direwolf20.justdirethings.common.containers.ExperienceHolderContainer;
import com.direwolf20.justdirethings.common.network.data.ExperienceHolderSettingsPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ExperienceHolderSettingsPacket {
    public static final ExperienceHolderSettingsPacket INSTANCE = new ExperienceHolderSettingsPacket();

    public static ExperienceHolderSettingsPacket get() {
        return INSTANCE;
    }

    public void handle(final ExperienceHolderSettingsPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof ExperienceHolderContainer experienceHolderContainer && experienceHolderContainer.baseMachineBE instanceof ExperienceHolderBE experienceHolderBE) {
                experienceHolderBE.changeSettings(payload.targetExp());
            }
        });
    }
}
