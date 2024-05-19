package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.items.PortalGun;
import com.direwolf20.justdirethings.common.network.data.PortalGunLeftClickPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PortalGunLeftClickPacket {
    public static final PortalGunLeftClickPacket INSTANCE = new PortalGunLeftClickPacket();

    public static PortalGunLeftClickPacket get() {
        return INSTANCE;
    }

    public void handle(final PortalGunLeftClickPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            ItemStack itemStack = sender.getMainHandItem();
            if (!(itemStack.getItem() instanceof PortalGun))
                itemStack = sender.getOffhandItem();
            if (!(itemStack.getItem() instanceof PortalGun))
                return;

            PortalGun.spawnProjectile(sender.level(), sender, itemStack, true);

        });
    }
}
