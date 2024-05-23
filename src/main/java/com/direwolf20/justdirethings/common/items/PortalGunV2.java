package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.entities.PortalProjectile;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.util.NBTHelpers;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PortalGunV2 extends Item {
    public static final int MAX_FAVORITES = 12;

    public PortalGunV2() {
        super(new Properties()
                .stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.SNOWBALL_THROW,
                SoundSource.NEUTRAL,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!level.isClientSide) {
            spawnProjectile(level, player, itemStack, false);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    public static void spawnProjectile(Level level, Player player, ItemStack itemStack, boolean isPrimaryType) {
        NBTHelpers.PortalDestination portalDestination = getSelectedFavorite(itemStack);
        if (portalDestination == null || portalDestination.equals(NBTHelpers.PortalDestination.EMPTY)) return;
        PortalProjectile projectile = new PortalProjectile(level, player, getUUID(itemStack), isPrimaryType, true, portalDestination);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1F, 1.0F);
        level.addFreshEntity(projectile);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    public static UUID setUUID(ItemStack itemStack) {
        UUID uuid = UUID.randomUUID();
        itemStack.set(JustDireDataComponents.PORTALGUN_UUID, uuid);
        return uuid;
    }

    public static UUID getUUID(ItemStack itemStack) {
        if (!itemStack.has(JustDireDataComponents.PORTALGUN_UUID))
            return setUUID(itemStack);
        return itemStack.get(JustDireDataComponents.PORTALGUN_UUID);
    }

    public static NBTHelpers.PortalDestination getSelectedFavorite(ItemStack itemStack) {
        List<NBTHelpers.PortalDestination> favoritesList = new ArrayList<>(getFavorites(itemStack));
        if (favoritesList.isEmpty()) return null;
        return favoritesList.get(getFavoritePosition(itemStack));
    }

    public static NBTHelpers.PortalDestination getFavorite(ItemStack itemStack, int slot) {
        List<NBTHelpers.PortalDestination> favoritesList = new ArrayList<>(getFavorites(itemStack));
        if (favoritesList.isEmpty()) return null;
        return favoritesList.get(slot);
    }

    public static int getFavoritePosition(ItemStack itemStack) {
        return itemStack.getOrDefault(JustDireDataComponents.PORTALGUN_FAVORITE, 0);
    }

    public static void setFavoritePosition(ItemStack itemStack, int favorite) {
        itemStack.set(JustDireDataComponents.PORTALGUN_FAVORITE, favorite);
    }

    public static List<NBTHelpers.PortalDestination> getFavorites(ItemStack itemStack) {
        return itemStack.getOrDefault(JustDireDataComponents.PORTAL_GUN_FAVORITES, new ArrayList<>());
    }

    public static void setFavorites(ItemStack itemStack, List<NBTHelpers.PortalDestination> favorites) {
        itemStack.set(JustDireDataComponents.PORTAL_GUN_FAVORITES, favorites);
    }

    public static void addFavorite(ItemStack itemStack, int position, NBTHelpers.PortalDestination portalDestination) {
        if (!itemStack.has(JustDireDataComponents.PORTAL_GUN_FAVORITES)) {
            List<NBTHelpers.PortalDestination> list = new ArrayList<>(MAX_FAVORITES);
            for (int i = 0; i < MAX_FAVORITES; i++) {
                list.add(NBTHelpers.PortalDestination.EMPTY); //Prefill List
            }
            setFavorites(itemStack, list);
        }
        List<NBTHelpers.PortalDestination> favoritesList = new ArrayList<>(getFavorites(itemStack));
        favoritesList.set(position, portalDestination);
        setFavorites(itemStack, favoritesList);
    }

    public static void removeFavorite(ItemStack itemStack, int position) {
        List<NBTHelpers.PortalDestination> favoritesList = new ArrayList<>(getFavorites(itemStack));
        if (favoritesList.isEmpty()) return;
        favoritesList.set(position, NBTHelpers.PortalDestination.EMPTY);
        setFavorites(itemStack, favoritesList);
    }

    public static ItemStack getPortalGunv2(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        if (mainHand.getItem() instanceof PortalGunV2)
            return mainHand;
        ItemStack offHand = player.getOffhandItem();
        if (offHand.getItem() instanceof PortalGunV2)
            return offHand;
        return ItemStack.EMPTY;
    }
}
