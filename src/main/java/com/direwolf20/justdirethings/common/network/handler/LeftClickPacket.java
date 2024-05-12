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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.direwolf20.justdirethings.util.MiscTools.getHitResult;

public class LeftClickPacket {
    public static final LeftClickPacket INSTANCE = new LeftClickPacket();

    public static LeftClickPacket get() {
        return INSTANCE;
    }

    public void handle(final LeftClickPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();

            ItemStack toggleableItem = ItemStack.EMPTY;
            if (payload.inventorySlot() == -1)
                toggleableItem = ToggleableItem.getToggleableItem(sender);
            else
                toggleableItem = sender.getInventory().getItem(payload.inventorySlot());
            if (toggleableItem.getItem() instanceof LeftClickableTool && toggleableItem.getItem() instanceof ToggleableTool toggleableTool) {
                if (payload.keyCode() == -1) {//left Click
                    InteractionHand hand = payload.mainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                    if (payload.clickType() == 0) { //Air
                        toggleableTool.useAbility(sender.level(), sender, hand, false);
                    } else if (payload.clickType() == 1) { //Block
                        UseOnContext useoncontext = new UseOnContext(sender.level(), sender, hand, toggleableItem, new BlockHitResult(Vec3.atCenterOf(payload.blockPos()), Direction.values()[payload.direction()], payload.blockPos(), false));
                        toggleableTool.useOnAbility(useoncontext, false);
                    }
                } else { //Key Binding
                    toggleableTool.useAbility(sender.level(), sender, toggleableItem, payload.keyCode(), payload.isMouse());
                    BlockHitResult blockHitResult = getHitResult(sender);
                    if (blockHitResult.getType() == HitResult.Type.BLOCK) {
                        UseOnContext useoncontext = new UseOnContext(sender.level(), sender, InteractionHand.MAIN_HAND, toggleableItem, blockHitResult);
                        toggleableTool.useOnAbility(useoncontext, toggleableItem, payload.keyCode(), payload.isMouse());
                    }
                }
            }

        });
    }
}
