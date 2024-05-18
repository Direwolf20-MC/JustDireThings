package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.entities.PortalEntity;
import com.direwolf20.justdirethings.common.entities.PortalProjectile;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.List;

public class PortalGun extends Item {
    public PortalGun() {
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
            if (!player.isShiftKeyDown())
                spawnProjectile(level, player);
            else
                closeMyPortals((ServerLevel) level, player);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    public void closeMyPortals(ServerLevel level, Player player) {
        List<? extends PortalEntity> customEntities = level.getEntities(Registration.PortalEntity.get(), k -> k.getOwnerUUID().equals(player.getUUID()));

        for (PortalEntity entity : customEntities) {
            entity.discard();
        }
    }

    public void spawnProjectile(Level level, Player player) {
        PortalProjectile projectile = new PortalProjectile(level, player);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.5F, 1.0F);
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
}
