package com.direwolf20.justdirethings.common.network;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.network.data.ToggleToolPayload;
import com.direwolf20.justdirethings.common.network.handler.ToggleToolPacket;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class PacketHandler {
    public static void registerNetworking(final RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(JustDireThings.MODID);

        //Going to Server
        registrar.play(ToggleToolPayload.ID, ToggleToolPayload::new, handler -> handler.server(ToggleToolPacket.get()::handle));


        //Going to Client
        //registrar.play(DurabilitySyncPayload.ID, DurabilitySyncPayload::new, handler -> handler.client(PacketDurabilitySync.get()::handle));
    }
}
