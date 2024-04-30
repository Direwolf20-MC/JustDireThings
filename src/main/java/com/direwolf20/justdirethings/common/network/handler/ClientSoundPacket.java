package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.client.OurSounds;
import com.direwolf20.justdirethings.common.network.data.ClientSoundPayload;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientSoundPacket {
    public static final ClientSoundPacket INSTANCE = new ClientSoundPacket();

    public static ClientSoundPacket get() {
        return INSTANCE;
    }

    public void handle(final ClientSoundPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(payload.soundEvent());
            if (soundEvent != null) {
                OurSounds.playSound(soundEvent, payload.pitch(), payload.volume());
            }
        });
    }
}
