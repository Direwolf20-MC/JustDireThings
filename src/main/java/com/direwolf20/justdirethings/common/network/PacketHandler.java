package com.direwolf20.justdirethings.common.network;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.common.network.data.*;
import com.direwolf20.justdirethings.common.network.handler.*;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketHandler {
    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(JustDireThings.MODID);

        //Going to Server
        registrar.playToServer(AreaAffectingPayload.TYPE, AreaAffectingPayload.STREAM_CODEC, AreaAffectingPacket.get()::handle);
        registrar.playToServer(BlockStateFilterPayload.TYPE, BlockStateFilterPayload.STREAM_CODEC, BlockStateFilterPacket.get()::handle);
        registrar.playToServer(ClickerPayload.TYPE, ClickerPayload.STREAM_CODEC, ClickerPacket.get()::handle);
        registrar.playToServer(CopyMachineSettingsPayload.TYPE, CopyMachineSettingsPayload.STREAM_CODEC, CopyMachineSettingsPacket.get()::handle);
        registrar.playToServer(DirectionSettingPayload.TYPE, DirectionSettingPayload.STREAM_CODEC, DirectionSettingPacket.get()::handle);
        registrar.playToServer(DropperSettingPayload.TYPE, DropperSettingPayload.STREAM_CODEC, DropperSettingPacket.get()::handle);
        registrar.playToServer(EnergyTransmitterSettingPayload.TYPE, EnergyTransmitterSettingPayload.STREAM_CODEC, EnergyTransmitterPacket.get()::handle);
        registrar.playToServer(FilterSettingPayload.TYPE, FilterSettingPayload.STREAM_CODEC, FilterSettingPacket.get()::handle);
        registrar.playToServer(GhostSlotPayload.TYPE, GhostSlotPayload.STREAM_CODEC, GhostSlotPacket.get()::handle);
        registrar.playToServer(LeftClickPayload.TYPE, LeftClickPayload.STREAM_CODEC, LeftClickPacket.get()::handle);
        registrar.playToServer(PlayerAccessorPayload.TYPE, PlayerAccessorPayload.STREAM_CODEC, PlayerAccessorPacket.get()::handle);
        registrar.playToServer(RedstoneSettingPayload.TYPE, RedstoneSettingPayload.STREAM_CODEC, RedstoneSettingPacket.get()::handle);
        registrar.playToServer(SensorPayload.TYPE, SensorPayload.STREAM_CODEC, SensorPacket.get()::handle);
        registrar.playToServer(SwapperPayload.TYPE, SwapperPayload.STREAM_CODEC, SwapperPacket.get()::handle);
        registrar.playToServer(TickSpeedPayload.TYPE, TickSpeedPayload.STREAM_CODEC, TickSpeedPacket.get()::handle);
        registrar.playToServer(ToggleToolLeftRightClickPayload.TYPE, ToggleToolLeftRightClickPayload.STREAM_CODEC, ToggleToolLeftRightClickPacket.get()::handle);
        registrar.playToServer(ToggleToolPayload.TYPE, ToggleToolPayload.STREAM_CODEC, ToggleToolPacket.get()::handle);
        registrar.playToServer(ToggleToolSlotPayload.TYPE, ToggleToolSlotPayload.STREAM_CODEC, ToggleToolSlotPacket.get()::handle);

        //Going to Client
        registrar.playToClient(ClientSoundPayload.TYPE, ClientSoundPayload.STREAM_CODEC, ClientSoundPacket.get()::handle);
    }
}
