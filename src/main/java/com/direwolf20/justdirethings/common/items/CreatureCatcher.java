package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.entities.CreatureCatcherEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CreatureCatcher extends Item {
    public CreatureCatcher() {
        super(new Properties()
                .stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        pLevel.playSound(
                null,
                pPlayer.getX(),
                pPlayer.getY(),
                pPlayer.getZ(),
                SoundEvents.SNOWBALL_THROW,
                SoundSource.NEUTRAL,
                0.5F,
                0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!pLevel.isClientSide) {
            CreatureCatcherEntity creatureCatcherEntity = new CreatureCatcherEntity(pLevel, pPlayer);
            creatureCatcherEntity.setItem(itemStack);
            creatureCatcherEntity.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
            pLevel.addFreshEntity(creatureCatcherEntity);
        }

        if (!pPlayer.getAbilities().instabuild) {
            itemStack.shrink(1);
        }


        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }

    public static boolean hasEntity(ItemStack itemStack) {
        return !itemStack.getOrCreateTag().isEmpty();  //Any tag will be an entity
    }
}
