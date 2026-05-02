package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.util.NBTHelpers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

public class TotemOfDeathRecall extends Item {
    public TotemOfDeathRecall(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (getBoundTo(itemStack) == null) return InteractionResult.PASS;
        player.startUsingItem(hand);
        return InteractionResult.SUCCESS.heldItemTransformedTo(player.getItemInHand(hand));
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack pStack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level world, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            int usedDuration = this.getUseDuration(stack, entityLiving) - timeLeft;
            if (usedDuration >= 20) {  // 60 ticks = 3 seconds
                // Retrieve death location from NBT and teleport
                if (!world.isClientSide()) {
                    if (stack.has(JustDireDataComponents.BOUND_GLOBAL_VEC3)) {
                        NBTHelpers.GlobalVec3 globalPos = getBoundTo(stack);
                        if (globalPos == null) return false;
                        Vec3 position = globalPos.position();
                        ServerLevel targetLevel = world.getServer().getLevel(globalPos.dimension());
                        if (targetLevel != null) {
                            player.teleportTo(targetLevel, position.x(), position.y(), position.z(), new HashSet<>(), player.getYRot(), player.getXRot(), false);
                            stack.shrink(1);
                            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity livingEntity) {
        return 72000;  // Max duration (arbitrary large number)
    }

    public boolean isFoil(ItemStack pStack) {
        return getBoundTo(pStack) != null;
    }

    public static NBTHelpers.GlobalVec3 getBoundTo(ItemStack stack) {
        if (stack.has(JustDireDataComponents.BOUND_GLOBAL_VEC3))
            return stack.get(JustDireDataComponents.BOUND_GLOBAL_VEC3);
        return null;
    }

    public static void setBoundTo(ItemStack stack, NBTHelpers.GlobalVec3 globalPos) {
        stack.set(JustDireDataComponents.BOUND_GLOBAL_VEC3, globalPos);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, display, tooltip, flagIn);
        Level level = context.level();
        if (level == null) {
            return;
        }
        NBTHelpers.GlobalVec3 boundPos = getBoundTo(stack);
        ChatFormatting chatFormatting = ChatFormatting.DARK_PURPLE;
        if (boundPos != null) {
            List<Component> buffer = new ArrayList<>();
            buffer.add(Component.translatable("justdirethings.boundto", I18n.get(boundPos.dimension().identifier().getPath()), boundPos.toVec3ShortString()).withStyle(chatFormatting));
            buffer.forEach(tooltip);
        }
    }
}
