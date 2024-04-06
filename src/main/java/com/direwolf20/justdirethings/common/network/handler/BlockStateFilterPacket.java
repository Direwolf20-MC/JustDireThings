package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.blockentities.SensorT1BE;
import com.direwolf20.justdirethings.common.containers.basecontainers.BaseMachineContainer;
import com.direwolf20.justdirethings.common.network.data.BlockStateFilterPayload;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Map;
import java.util.Optional;

public class BlockStateFilterPacket {
    public static final BlockStateFilterPacket INSTANCE = new BlockStateFilterPacket();

    public static BlockStateFilterPacket get() {
        return INSTANCE;
    }

    public void handle(final BlockStateFilterPayload payload, final PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            Optional<Player> senderOptional = context.player();
            if (senderOptional.isEmpty())
                return;
            Player sender = senderOptional.get();
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof BaseMachineContainer baseMachineContainer && baseMachineContainer.baseMachineBE instanceof SensorT1BE sensor) {
                ListTag listTag = payload.compoundTag().getList("tagList", 10); // 10 for CompoundTag type
                ItemStack stateStack = sensor.getFilterHandler().getStackInSlot(payload.slot());
                Map<Property<?>, Comparable<?>> propertiesList = SensorT1BE.loadBlockStateProperty(listTag, stateStack);
                sensor.addBlockStateProperty(payload.slot(), propertiesList);
            }
        });
    }
}
