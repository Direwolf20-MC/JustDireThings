package com.direwolf20.justdirethings.common.network;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.network.data.*;
import com.direwolf20.justdirethings.common.network.handler.*;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class PacketHandler {
    public static void registerNetworking(final RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(JustDireThings.MODID);

        //Going to Server
        registrar.play(ToggleToolPayload.ID, ToggleToolPayload::new, handler -> handler.server(ToggleToolPacket.get()::handle));
        registrar.play(AreaAffectingPayload.ID, AreaAffectingPayload::new, handler -> handler.server(AreaAffectingPacket.get()::handle));
        registrar.play(BlockStateFilterPayload.ID, BlockStateFilterPayload::new, handler -> handler.server(BlockStateFilterPacket.get()::handle));
        registrar.play(ClickerPayload.ID, ClickerPayload::new, handler -> handler.server(ClickerPacket.get()::handle));
        registrar.play(FilterSettingPayload.ID, FilterSettingPayload::new, handler -> handler.server(FilterSettingPacket.get()::handle));
        registrar.play(DirectionSettingPayload.ID, DirectionSettingPayload::new, handler -> handler.server(DirectionSettingPacket.get()::handle));
        registrar.play(RedstoneSettingPayload.ID, RedstoneSettingPayload::new, handler -> handler.server(RedstoneSettingPacket.get()::handle));
        registrar.play(SensorPayload.ID, SensorPayload::new, handler -> handler.server(SensorPacket.get()::handle));
        registrar.play(TickSpeedPayload.ID, TickSpeedPayload::new, handler -> handler.server(TickSpeedPacket.get()::handle));
        registrar.play(ToggleToolSlotPayload.ID, ToggleToolSlotPayload::new, handler -> handler.server(ToggleToolSlotPacket.get()::handle));
        registrar.play(GhostSlotPayload.ID, GhostSlotPayload::new, handler -> handler.server(GhostSlotPacket.get()::handle));


        //Going to Client
        //registrar.play(DurabilitySyncPayload.ID, DurabilitySyncPayload::new, handler -> handler.client(PacketDurabilitySync.get()::handle));
    }
}
