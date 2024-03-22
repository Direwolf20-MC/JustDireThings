package com.direwolf20.justdirethings.common.network;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.network.data.GhostSlotPayload;
import com.direwolf20.justdirethings.common.network.data.ItemCollectorPayload;
import com.direwolf20.justdirethings.common.network.data.ToggleToolPayload;
import com.direwolf20.justdirethings.common.network.data.ToggleToolSlotPayload;
import com.direwolf20.justdirethings.common.network.handler.GhostSlotPacket;
import com.direwolf20.justdirethings.common.network.handler.ItemCollectorPacket;
import com.direwolf20.justdirethings.common.network.handler.ToggleToolPacket;
import com.direwolf20.justdirethings.common.network.handler.ToggleToolSlotPacket;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class PacketHandler {
    public static void registerNetworking(final RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(JustDireThings.MODID);

        //Going to Server
        registrar.play(ToggleToolPayload.ID, ToggleToolPayload::new, handler -> handler.server(ToggleToolPacket.get()::handle));
        registrar.play(ItemCollectorPayload.ID, ItemCollectorPayload::new, handler -> handler.server(ItemCollectorPacket.get()::handle));
        registrar.play(ToggleToolSlotPayload.ID, ToggleToolSlotPayload::new, handler -> handler.server(ToggleToolSlotPacket.get()::handle));
        registrar.play(GhostSlotPayload.ID, GhostSlotPayload::new, handler -> handler.server(GhostSlotPacket.get()::handle));


        //Going to Client
        //registrar.play(DurabilitySyncPayload.ID, DurabilitySyncPayload::new, handler -> handler.client(PacketDurabilitySync.get()::handle));
    }
}
