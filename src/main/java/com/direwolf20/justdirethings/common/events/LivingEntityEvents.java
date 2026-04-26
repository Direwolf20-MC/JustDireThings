package com.direwolf20.justdirethings.common.events;

import com.direwolf20.justdirethings.common.items.TotemOfDeathRecall;
import com.direwolf20.justdirethings.common.items.armors.utils.ArmorTiers;
import com.direwolf20.justdirethings.common.items.interfaces.*;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import com.direwolf20.justdirethings.util.NBTHelpers;
import com.direwolf20.justdirethings.util.UsefulFakePlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;


public class LivingEntityEvents {

    @SubscribeEvent
    public static void blockDamage(EntityInvulnerabilityCheckEvent e) {
        Entity target = e.getEntity();
        if (target instanceof Player player) {
            ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
            if (chestplate.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(chestplate, Ability.ELYTRA)) {
                if (e.getSource().is(DamageTypes.FLY_INTO_WALL)) {
                    e.setInvulnerable(true);
                    Helpers.damageTool(chestplate, player, Ability.ELYTRA);
                    return;
                }
            }
            if (chestplate.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(chestplate, Ability.LAVAIMMUNITY)) {
                if (e.getSource().is(DamageTypes.LAVA) || e.getSource().is(DamageTypes.IN_FIRE) || e.getSource().is(DamageTypes.ON_FIRE)) {
                    e.setInvulnerable(true);
                    Helpers.damageTool(chestplate, player, Ability.LAVAIMMUNITY);
                    return;
                }
            }
            if (chestplate.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.hasAbility(Ability.INVULNERABILITY)) {
                int activeCooldown = ToggleableTool.getCooldown(chestplate, Ability.INVULNERABILITY, true);
                if (activeCooldown == -1) return;
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
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
                    int denominator = 2;
                    Equippable equippable = helmet.get(DataComponents.EQUIPPABLE);
                    if (equippable != null) {
                        ResourceKey<EquipmentAsset> assetId = equippable.assetId().orElse(null);
                        if (assetId != null) {
                            if (assetId.equals(ArmorTiers.BLAZEGOLD.assetId()))
                                denominator = 3;
                            else if (assetId.equals(ArmorTiers.CELESTIGEM.assetId()))
                                denominator = 4;
                            else if (assetId.equals(ArmorTiers.ECLIPSEALLOY.assetId()))
                                denominator = 5;
                        }
                    }
                    if (distance > (defaultRange / denominator))
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

    // Vanilla LivingEntity#canGlideUsing only checks DataComponents.GLIDER presence, and NeoForge 26.1
    // no longer exposes canElytraFly/elytraFlightTick. Sync the GLIDER component onto the equipped
    // chestplate per server tick so the ELYTRA toggle + upgrade gate actually govern gliding; vanilla's
    // updateFallFlying drops the fall-flying flag the next tick when the component disappears.
    @SubscribeEvent
    public static void syncElytraComponent(EntityTickEvent.Post e) {
        if (e.getEntity().level().isClientSide()) return;
        if (!(e.getEntity() instanceof Player player)) return;
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!(chest.getItem() instanceof ToggleableTool toggleableTool)) return;
        if (!toggleableTool.hasAbility(Ability.ELYTRA)) return;

        boolean shouldGlide = toggleableTool.canUseAbilityAndDurability(chest, Ability.ELYTRA);
        boolean hasComponent = chest.has(DataComponents.GLIDER);
        if (shouldGlide && !hasComponent) {
            chest.set(DataComponents.GLIDER, Unit.INSTANCE);
        } else if (!shouldGlide && hasComponent) {
            chest.remove(DataComponents.GLIDER);
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

                        if (dropSmoked[0] && ToggleableTool.getCustomSetting(mainHand, Ability.SMOKER.getName()) == 0) {
                            ToggleableTool.smokerParticles((ServerLevel) player.level(), itemEntity.blockPosition(), itemEntity.getItem().getCount());
                        }
                    }
                }
                if (toggleableTool.canUseAbility(mainHand, Ability.DROPTELEPORT)) {
                    ResourceHandler<ItemResource> handler = ToggleableTool.getBoundHandler((ServerLevel) player.level(), mainHand);
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
                            if (ToggleableTool.getCustomSetting2(mainHand, Ability.DROPTELEPORT.getName()) == 0)
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
            ItemStack armorItemStack = entry.getValue().armorItemStack;
            if (armorItemStack.getItem() instanceof PoweredItem) {
                entry.getValue().newDamage = entry.getValue().originalDamage * 100;
                continue;
            } //No need for below checks for powered items, as those don't break :)
            if (armorItemStack.getItem() instanceof ToggleableTool toggleableTool && event.getEntity() instanceof Player player) {
                float newDmg = armorItemStack.getDamageValue() + entry.getValue().originalDamage;
                if (newDmg >= armorItemStack.getMaxDamage()) {
                    EnumSet<Ability> abilities = toggleableTool.getAbilities();
                    for (Ability ability : abilities) {
                        if (ToggleableTool.hasUpgrade(armorItemStack, ability) && ability.requiresUpgrade()) {
                            ItemStack upgradeStack = new ItemStack(ability.getUpgradeItem());
                            player.drop(upgradeStack, true);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
            int currentCooldown = ToggleableTool.getAnyCooldown(chestplate, Ability.DEATHPROTECTION);
            if (currentCooldown == -1) {
                if (chestplate.getItem() instanceof ToggleableTool toggleableTool && toggleableTool.canUseAbilityAndDurability(chestplate, Ability.DEATHPROTECTION)) {
                    AbilityParams abilityParams = toggleableTool.getAbilityParams(Ability.DEATHPROTECTION);
                    ToggleableTool.addCooldown(chestplate, Ability.DEATHPROTECTION, abilityParams.cooldown, false);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    Helpers.damageTool(chestplate, player, Ability.DEATHPROTECTION);
                    player.setHealth(10.0F);
                    event.setCanceled(true);
                    return;
                }
            }
            // Check player's inventory for the totem
            ItemStack totemStack = findTotem(player);
            if (!totemStack.isEmpty()) {
                CompoundTag deathData = new CompoundTag();
                deathData.put("direDeathData", NBTHelpers.globalVec3ToNBT(player.level().dimension(), player.position()));
                player.setData(JDTRegistration.DEATH_DATA, deathData);
                totemStack.shrink(1);
            }
        }
    }

    private static ItemStack findTotem(ServerPlayer player) {
        for (ItemStack itemStack : player.getInventory().getNonEquipmentItems()) {
            if (itemStack.getItem() == JDTRegistration.TotemOfDeathRecall.get() && TotemOfDeathRecall.getBoundTo(itemStack) == null) {
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        ServerPlayer oldPlayer = (ServerPlayer) event.getOriginal();
        if (oldPlayer.level().isClientSide() || !event.isWasDeath()) return;
        ServerPlayer newPlayer = (ServerPlayer) event.getEntity();
        CompoundTag deathData = oldPlayer.getData(JDTRegistration.DEATH_DATA);

        if (deathData.contains("direDeathData")) {
            NBTHelpers.GlobalVec3 boundTo = NBTHelpers.nbtToGlobalVec3(deathData.getCompoundOrEmpty("direDeathData"));
            ItemStack totemStack = new ItemStack(JDTRegistration.TotemOfDeathRecall.get());
            TotemOfDeathRecall.setBoundTo(totemStack, boundTo);
            newPlayer.getInventory().add(totemStack);
        }
    }
}
