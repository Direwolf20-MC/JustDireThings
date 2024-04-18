package com.direwolf20.justdirethings.common.events;

import com.direwolf20.justdirethings.common.items.TotemOfDeathRecall;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.Helpers;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.NBTHelpers;
import com.direwolf20.justdirethings.util.UsefulFakePlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Iterator;

import static com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool.getToggleableTool;


public class LivingEntityEvents {

    @SubscribeEvent
    public static void blockJoin(EntityJoinLevelEvent e) {
        if (e.getEntity() instanceof UsefulFakePlayer)
            e.setCanceled(true);
    }

    @SubscribeEvent
    public static void LivingFallDamage(LivingFallEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack toggleableTool = getToggleableTool(player);
            if (toggleableTool.isEmpty()) return;
            if (((ToggleableTool) toggleableTool.getItem()).hasAbility(Ability.AIRBURST))
                event.setDistance(0.0f);
        }
    }

    @SubscribeEvent
    public static void LivingDropsEvent(LivingDropsEvent event) {
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof Player player) {
            ItemStack mainHand = player.getMainHandItem();
            if (mainHand.getItem() instanceof ToggleableTool toggleableTool) {
                if (toggleableTool.canUseAbility(mainHand, Ability.SMOKER)) {
                    Iterator<ItemEntity> iterator = event.getDrops().iterator();
                    
                    while (iterator.hasNext()) {
                        ItemEntity itemEntity = iterator.next();
                        boolean[] dropSmoked = new boolean[1];
                        Helpers.smokeDrop((ServerLevel) player.level(), itemEntity, mainHand, event.getEntity(), dropSmoked);
                        
                        if (dropSmoked[0]) {
                            toggleableTool.smokerParticles((ServerLevel) player.level(), itemEntity.blockPosition(), itemEntity.getItem().getCount());
                        }
                    }
                }
                if (toggleableTool.canUseAbility(mainHand, Ability.DROPTELEPORT)) {
                    IItemHandler handler = ToggleableTool.getBoundHandler((ServerLevel) player.level(), mainHand);
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
                            ToggleableTool.teleportParticles((ServerLevel) player.level(), event.getEntity().getPosition(0f));
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // Check player's inventory for the totem
            ItemStack totemStack = findTotem(player);
            if (!totemStack.isEmpty()) {
                CompoundTag deathData = new CompoundTag();
                deathData.put("direDeathData", NBTHelpers.globalVec3ToNBT(player.level().dimension(), player.position()));
                player.setData(Registration.DEATH_DATA, deathData);
                totemStack.shrink(1);
            }
        }
    }

    private static ItemStack findTotem(ServerPlayer player) {
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.getItem() == Registration.TotemOfDeathRecall.get() && TotemOfDeathRecall.getBoundTo(itemStack) == null) {
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        ServerPlayer oldPlayer = (ServerPlayer) event.getOriginal();
        if (oldPlayer.level().isClientSide || !event.isWasDeath()) return;
        ServerPlayer newPlayer = (ServerPlayer) event.getEntity();
        CompoundTag deathData = oldPlayer.getData(Registration.DEATH_DATA);

        if (deathData.contains("direDeathData")) {
            //GlobalPos boundTo = NBTHelpers.nbtToGlobalPos(deathData.getCompound("direDeathData"));
            NBTHelpers.GlobalVec3 boundTo = NBTHelpers.nbtToGlobalVec3(deathData.getCompound("direDeathData"));
            ItemStack totemStack = new ItemStack(Registration.TotemOfDeathRecall.get());
            TotemOfDeathRecall.setBoundTo(totemStack, boundTo);
            newPlayer.getInventory().add(totemStack);
        }
    }
}
