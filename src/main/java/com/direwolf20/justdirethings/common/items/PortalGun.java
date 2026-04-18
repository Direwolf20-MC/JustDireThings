package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.entities.PortalEntity;
import com.direwolf20.justdirethings.common.entities.PortalProjectile;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.interfaces.BasePoweredItem;
import com.direwolf20.justdirethings.common.items.interfaces.PoweredItem;
import com.direwolf20.justdirethings.setup.Config;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class PortalGun extends BasePoweredItem implements PoweredItem {
    public PortalGun(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getMaxEnergy() {
        return Config.PORTAL_GUN_V1_RF_CAPACITY.get();
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            if (!player.isShiftKeyDown())
                spawnProjectile(level, player, itemStack, false);
            else
                closeMyPortals((ServerLevel) level, itemStack, player);
        }
        return InteractionResult.FAIL;
    }

    public static void closeMyPortals(ServerLevel level, ItemStack itemStack, Player player) {
        UUID portalGunUUID = getUUID(itemStack);
        MinecraftServer server = level.getServer();
        for (ServerLevel serverLevel : server.getAllLevels()) {
            List<? extends PortalEntity> customEntities = serverLevel.getEntities(JDTRegistration.PortalEntity.get(), k -> k.getOwner() == player.getUUID() || k.getPortalGunUUID().equals(portalGunUUID));

            for (PortalEntity entity : customEntities) {
                entity.setDying();
            }
        }
    }

    public static void spawnProjectile(Level level, Player player, ItemStack itemStack, boolean isPrimaryType) {
        if (!PoweredItem.consumeEnergy(itemStack, Config.PORTAL_GUN_V1_RF_COST.get())) {
            player.sendOverlayMessage(Component.translatable("justdirethings.lowenergy"));
            player.playSound(SoundEvents.VAULT_INSERT_ITEM_FAIL, 1.0F, 1.0F);
            return;
        }
        PortalProjectile projectile = new PortalProjectile(level, player, getUUID(itemStack), isPrimaryType, false);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1F, 1.0F);
        level.addFreshEntity(projectile);
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.NONE;
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
}
