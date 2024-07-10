package com.direwolf20.justdirethings.common.events;

import com.direwolf20.justdirethings.common.items.TotemOfDeathRecall;
import com.direwolf20.justdirethings.common.items.interfaces.*;
import com.direwolf20.justdirethings.setup.Registration;
import com.direwolf20.justdirethings.util.NBTHelpers;
import com.direwolf20.justdirethings.util.UsefulFakePlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Iterator;
import java.util.Map;


public class LivingEntityEvents {

    @SubscribeEvent
    public static void blockDamage(EntityInvulnerabilityCheckEvent e) {
        Entity target = e.getEntity();
        if (target instanceof Player player) {
            ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
            if (chestplate.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbility(chestplate, Ability.ELYTRA)) {
                if (e.getSource().is(DamageTypes.FLY_INTO_WALL))
                    e.setInvulnerable(true);
            } else if (chestplate.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.hasAbility(Ability.INVULNERABILITY)) {
                int activeCooldown = ToggleableTool.getCooldown(chestplate, Ability.INVULNERABILITY, true);
                if (activeCooldown == -1) return;
                player.playNotifySound(SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
                e.setInvulnerable(true);
            }
        }
    }

    @SubscribeEvent
    public static void changeTargets(LivingChangeTargetEvent e) {
        LivingEntity source = e.getEntity();
        LivingEntity target = e.getOriginalAboutToBeSetTarget();
        if (target instanceof Player player) {
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            if (helmet.getItem() instanceof ToggleableTool toggleableTool) {
                if (toggleableTool.canUseAbilityAndDurability(helmet, Ability.STUPEFY) && ToggleableTool.getCooldown(helmet, Ability.STUPEFY, true) != -1 && AbilityMethods.getStupefyTargets(helmet).contains(source.getStringUUID())) {
                    e.setCanceled(true);
                } else if (toggleableTool.canUseAbilityAndDurability(helmet, Ability.MINDFOG)) {
                    double distance = source.position().distanceTo(target.position());
                    double defaultRange = source.getAttributes().hasAttribute(Attributes.FOLLOW_RANGE) ? source.getAttribute(Attributes.FOLLOW_RANGE).getValue() : 16;
                    if (distance > (defaultRange / 2))
                        e.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void jumpEvent(LivingEvent.LivingJumpEvent e) {
        if (e.getEntity() instanceof Player player) {
            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
            if (boots.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(boots, Ability.JUMPBOOST))
                Ability.JUMPBOOST.action.execute(player.level(), player, boots);
        }
    }

    @SubscribeEvent
    public static void blockJoin(EntityJoinLevelEvent e) {
        if (e.getEntity() instanceof UsefulFakePlayer)
            e.setCanceled(true);
    }

    @SubscribeEvent
    public static void LivingFallDamage(LivingFallEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack heldItem = player.getMainHandItem();
            if (heldItem.getItem() instanceof ToggleableTool toggleableTool) {
                if (toggleableTool.hasAbility(Ability.AIRBURST)) {
                    event.setDistance(0.0f);
                    return;
                }
            }
            heldItem = player.getOffhandItem();
            if (heldItem.getItem() instanceof ToggleableTool toggleableTool) {
                if (toggleableTool.hasAbility(Ability.AIRBURST)) {
                    event.setDistance(0.0f);
                    return;
                }
            }
            heldItem = player.getItemBySlot(EquipmentSlot.FEET);
            if (heldItem.getItem() instanceof ToggleableTool toggleableTool) {
                if (toggleableTool.canUseAbilityAndDurability(heldItem, Ability.NEGATEFALLDAMAGE)) {
                    int distance = (int) event.getDistance();
                    Helpers.damageTool(heldItem, player, Ability.NEGATEFALLDAMAGE, distance);
                    event.setDistance(0.0f);
                    return;
                }
            }
            if (heldItem.getItem() instanceof ToggleableTool toggleableTool) {
                if (toggleableTool.canUseAbilityAndDurability(heldItem, Ability.JUMPBOOST)) {
                    int jumpBoost = ToggleableTool.getToolValue(heldItem, Ability.JUMPBOOST.getName());
                    event.setDistance(event.getDistance() - jumpBoost);
                    return;
                }
            }
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
                            ToggleableTool.smokerParticles((ServerLevel) player.level(), itemEntity.blockPosition(), itemEntity.getItem().getCount());
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
    public static void onArmorDamage(ArmorHurtEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Map<EquipmentSlot, ArmorHurtEvent.ArmorEntry> armorEntries = event.getArmorMap();
        for (Map.Entry<EquipmentSlot, ArmorHurtEvent.ArmorEntry> entry : armorEntries.entrySet()) {
            if (entry.getKey() == EquipmentSlot.MAINHAND || entry.getKey() == EquipmentSlot.OFFHAND) continue;
            if (entry.getValue().armorItemStack.getItem() instanceof PoweredItem)
                entry.getValue().newDamage = entry.getValue().originalDamage * 100;
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
            NBTHelpers.GlobalVec3 boundTo = NBTHelpers.nbtToGlobalVec3(deathData.getCompound("direDeathData"));
            ItemStack totemStack = new ItemStack(Registration.TotemOfDeathRecall.get());
            TotemOfDeathRecall.setBoundTo(totemStack, boundTo);
            newPlayer.getInventory().add(totemStack);
        }
    }
}
