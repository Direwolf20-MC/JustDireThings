package com.direwolf20.justdirethings.common.network.handler;

import com.direwolf20.justdirethings.common.items.interfaces.LeftClickableTool;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableItem;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.common.network.data.LeftClickPayload;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class LeftClickPacket {
    public static final LeftClickPacket INSTANCE = new LeftClickPacket();

    public static LeftClickPacket get() {
        return INSTANCE;
    }

    //Thanks Soaryn!
    @NotNull
    static BlockHitResult getHitResult(Player player) {
        var playerLook = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
        var lookVec = player.getViewVector(1.0F);
        var reach = player.getBlockReach();
        var endLook = playerLook.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
        BlockHitResult hitResult = player.level().clip(new ClipContext(playerLook, endLook, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        return hitResult;
    }

    public void handle(final LeftClickPayload payload, final PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            Optional<Player> senderOptional = context.player();
            if (senderOptional.isEmpty())
                return;
            Player player = senderOptional.get();

            ItemStack toggleableItem = ItemStack.EMPTY;
            if (payload.inventorySlot() == -1)
                toggleableItem = ToggleableItem.getToggleableItem(player);
            else
                toggleableItem = player.getInventory().getItem(payload.inventorySlot());
            if (toggleableItem.getItem() instanceof LeftClickableTool && toggleableItem.getItem() instanceof ToggleableTool toggleableTool) {
                if (payload.keyCode() == -1) {//left Click
                    InteractionHand hand = payload.mainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                    if (payload.type() == 0) { //Air
                        toggleableTool.useAbility(player.level(), player, hand, false);
                    } else if (payload.type() == 1) { //Block
                        UseOnContext useoncontext = new UseOnContext(player.level(), player, hand, toggleableItem, new BlockHitResult(Vec3.atCenterOf(payload.blockPos()), Direction.values()[payload.direction()], payload.blockPos(), false));
                        toggleableTool.useOnAbility(useoncontext, false);
                    }
                } else { //Key Binding
                    toggleableTool.useAbility(player.level(), player, toggleableItem, payload.keyCode(), payload.isMouse());
                    BlockHitResult blockHitResult = getHitResult(player);
                    if (blockHitResult.getType() == HitResult.Type.BLOCK) {
                        UseOnContext useoncontext = new UseOnContext(player.level(), player, InteractionHand.MAIN_HAND, toggleableItem, blockHitResult);
                        toggleableTool.useOnAbility(useoncontext, toggleableItem, payload.keyCode(), payload.isMouse());
                    }
                }
            }

        });
    }
}
