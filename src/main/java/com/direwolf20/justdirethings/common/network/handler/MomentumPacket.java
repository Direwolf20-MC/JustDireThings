package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.entities.PortalEntity;
import com.direwolf20.justdirethings.common.network.data.MomentumPayload;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

import static com.direwolf20.justdirethings.common.entities.PortalEntity.transformMotion;

public class MomentumPacket {
    public static final MomentumPacket INSTANCE = new MomentumPacket();

    public static MomentumPacket get() {
        return INSTANCE;
    }

    public void handle(final MomentumPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            ServerLevel serverLevel = (ServerLevel) sender.level();

            List<? extends PortalEntity> portalEntities = serverLevel.getEntities(Registration.PortalEntity.get(), k -> k.getUUID().equals(payload.portalUUID()));
            if (portalEntities.isEmpty()) return;
            PortalEntity fromPortal = portalEntities.getFirst();

            if (fromPortal == null) return;
            PortalEntity toPortal = fromPortal.findPartnerPortal(serverLevel.getServer());
            if (toPortal == null) return;

            Vec3 newMotion = transformMotion(payload.momentum(), fromPortal.getDirection(), toPortal.getDirection().getOpposite());

            //System.out.println(payload.momentum() + ":" + newMotion);
            if (payload.entityUUID().equals(sender.getUUID())) {
                sender.setDeltaMovement(newMotion);
                sender.hasImpulse = true;
                ((ServerPlayer) sender).connection.send(new ClientboundSetEntityMotionPacket(sender));
            } else {
                Entity entity = serverLevel.getEntities().get(payload.entityUUID());
                if (entity == null || entity instanceof Player) return; //Don't push other players I guess :)

                entity.setDeltaMovement(newMotion);
                entity.hasImpulse = true;
                ((ServerLevel) entity.level()).getChunkSource().broadcast(entity, new ClientboundSetEntityMotionPacket(entity));
            }
        });
    }
}
