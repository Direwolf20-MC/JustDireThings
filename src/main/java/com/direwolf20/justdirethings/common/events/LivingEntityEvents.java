package com.direwolf20.justdirethings.common.events;

import com.direwolf20.justdirethings.common.items.tools.utils.Ability;
import com.direwolf20.justdirethings.common.items.tools.utils.Helpers;
import com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool;
import com.direwolf20.justdirethings.util.MiscHelpers;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Iterator;

import static com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool.getBoundInventory;
import static com.direwolf20.justdirethings.common.items.tools.utils.ToggleableTool.getBoundInventorySide;

public class LivingEntityEvents {

    @SubscribeEvent
    public static void LivingDropsEvent(LivingDropsEvent event) {
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof Player player) {
            ItemStack mainHand = player.getMainHandItem();
            if (mainHand.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbility(mainHand, Ability.DROPTELEPORT)) {
                GlobalPos globalPos = getBoundInventory(mainHand);
                if (globalPos != null) {
                    IItemHandler handler = MiscHelpers.getAttachedInventory(player.level().getServer().getLevel(globalPos.dimension()), globalPos.pos(), getBoundInventorySide(mainHand));
                    if (handler != null) {
                        Iterator<ItemEntity> iterator = event.getDrops().iterator();
                        while (iterator.hasNext()) {
                            ItemEntity itemEntity = iterator.next();
                            ItemStack stack = itemEntity.getItem();

                            ItemStack leftover = Helpers.teleportDrop(stack, handler, mainHand, player);

                            if (leftover.isEmpty()) {
                                // If the stack is now empty, remove the ItemEntity from the collection
                                iterator.remove(); // Optionally remove from your collection if it's being directly manipulated
                            } else {
                                // Otherwise, update the ItemEntity with the modified stack
                                itemEntity.setItem(leftover);
                            }
                        }
                        if (event.getDrops().isEmpty()) { //Only spawn particles if we teleported everything - not perfect but better than exhaustive testing
                            toggleableTool.teleportParticles((ServerLevel) player.level(), event.getEntity().getPosition(0f));
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }
}
