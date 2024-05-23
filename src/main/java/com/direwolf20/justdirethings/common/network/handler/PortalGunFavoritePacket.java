package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.items.PortalGunV2;
import com.direwolf20.justdirethings.common.network.data.PortalGunFavoritePayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PortalGunFavoritePacket {
    public static final PortalGunFavoritePacket INSTANCE = new PortalGunFavoritePacket();

    public static PortalGunFavoritePacket get() {
        return INSTANCE;
    }

    public void handle(final PortalGunFavoritePayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            ItemStack itemStack = sender.getMainHandItem();
            if (!(itemStack.getItem() instanceof PortalGunV2))
                itemStack = sender.getOffhandItem();
            if (!(itemStack.getItem() instanceof PortalGunV2))
                return;

            PortalGunV2.setFavoritePosition(itemStack, payload.favorite());

        });
    }
}
