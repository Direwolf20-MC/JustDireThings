package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.items.PortalGunV2;
import com.direwolf20.justdirethings.common.network.data.PortalGunFavoriteChangePayload;
import com.direwolf20.justdirethings.util.MiscHelpers;
import com.direwolf20.justdirethings.util.NBTHelpers;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PortalGunFavoriteChangePacket {
    public static final PortalGunFavoriteChangePacket INSTANCE = new PortalGunFavoriteChangePacket();

    public static PortalGunFavoriteChangePacket get() {
        return INSTANCE;
    }

    public void handle(final PortalGunFavoriteChangePayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            ItemStack itemStack = sender.getMainHandItem();
            if (!(itemStack.getItem() instanceof PortalGunV2))
                itemStack = sender.getOffhandItem();
            if (!(itemStack.getItem() instanceof PortalGunV2))
                return;

            if (!payload.add())
                PortalGunV2.removeFavorite(itemStack, payload.favorite());
            else {
                Level level = sender.level();
                Vec3 position = sender.position();
                Direction facing = MiscHelpers.getFacingDirection(sender);
                NBTHelpers.PortalDestination portalDestination = new NBTHelpers.PortalDestination(new NBTHelpers.GlobalVec3(level.dimension(), position), facing, payload.name());
                PortalGunV2.addFavorite(itemStack, payload.favorite(), portalDestination);
            }

        });
    }
}
